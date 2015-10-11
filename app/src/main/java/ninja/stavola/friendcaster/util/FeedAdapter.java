package ninja.stavola.friendcaster.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.annotation.WorkerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.model.Rss.Item;
import ninja.stavola.friendcaster.view.BottomSheetButtonView;

public class FeedAdapter extends ArrayAdapter<Item> {
    private Context context;

    public FeedAdapter(Context context, @LayoutRes int layout) {
        super(context, layout);
        this.context = context;
    }

    //TODO: Actually set up the episode card
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final EpisodeViewHolder episodeHolder;

        if (view != null) {
            episodeHolder = (EpisodeViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.card_episode, parent, false);
            episodeHolder = new EpisodeViewHolder(context, view);
            view.setTag(episodeHolder);
        }

        final Item item = getItem(position);

        //Format for the entry title is "SBFC <Episode Number>: <Episode Title>"
        episodeHolder.episodeTitle.setText(item.title.substring(5));
        episodeHolder.fileName = item.title.substring(0, item.title.indexOf(':')).replace(" ", "_") + ".mp3";

        episodeHolder.episodeDate.setText(getLocalDateTimeString(new Date(item.pubDate)));

        //Get the media file for the entry
        String url = item.enclosure.url;
        episodeHolder.episodeMediaFileUrl = url;
        episodeHolder.episodeMediaMime = item.enclosure.type;

        //Since there is no duration returned by the RSS, we populate a holder in the model
        //(with our own calculated duration) so we don't have to ever recalculate!
        if(item.durationHolder == null) {
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    return getDuration(params[0]);
                }

                @Override
                protected void onPostExecute(String s) {
                    item.durationHolder = s;
                    episodeHolder.episodeLength.setText(s);
                }
            }.execute(url);
        } else {
            episodeHolder.episodeLength.setText(item.durationHolder);
        }

        episodeHolder.linkToEpisode = item.link;

        return view;
    }

    private String getLocalDateTimeString(Date date) {
        LocalDateTime localDateTime =
                new LocalDateTime(date, DateTimeZone.forTimeZone(TimeZone.getDefault()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("M/dd/yy");

        return localDateTime.toString(dateTimeFormatter);
    }

    @WorkerThread
    private String getDuration(String urlString) {
        HttpURLConnection conn = null;
        double length = -1;

        //We connect to the file real quick to get the file size in bytes from the content-header
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            length = conn.getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        //Something icky happened, we don't know the duration!
        if(length == -1) {
            return "Unknown";
        }

        //We do (length / 1024) * 8 to get length in Kib and then divide by the bitrate (64)
        //So why not shorten it to (length / 8192)?
        double lengthInSeconds = length / 8192;

        //Round up and cast to int
        int roundedLengthInSeconds = (int) (lengthInSeconds + 0.5);

        Period period = new Period(Seconds.seconds(roundedLengthInSeconds));

        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();

        return periodFormatter.print(period.normalizedStandard());
    }

    public static class EpisodeViewHolder {
        @Bind(R.id.episode_title)
        public TextView episodeTitle;

        @Bind(R.id.episode_length)
        public TextView episodeLength;

        @Bind(R.id.episode_date)
        public TextView episodeDate;

        public String episodeMediaFileUrl;
        public String episodeMediaMime;

        public String fileName;

        public String linkToEpisode;

        private Context context;

        public EpisodeViewHolder(Context context, View view) {
            this.context = context;

            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.episode_card)
        public void showBottomSheet() {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.view_bottom_sheet, null);
            BottomSheetViewHolder bottomSheetViewHolder = new BottomSheetViewHolder(bottomSheetView);

            final Intent intent = new Intent();

            bottomSheetViewHolder.buttonStream.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(episodeMediaFileUrl), episodeMediaMime);
                    context.startActivity(intent);
                }
            });

            bottomSheetViewHolder.buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(episodeMediaFileUrl));

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    request.allowScanningByMediaScanner();

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                }
            });

            bottomSheetViewHolder.buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Actually cast to a device
                }
            });

            bottomSheetViewHolder.buttonBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(linkToEpisode));
                    context.startActivity(intent);
                }
            });

            bottomSheetDialog.contentView(bottomSheetView).show();
        }

        public static class BottomSheetViewHolder {
            @Bind(R.id.button_stream)
            BottomSheetButtonView buttonStream;

            @Bind(R.id.button_download)
            BottomSheetButtonView buttonDownload;

            @Bind(R.id.button_cast)
            BottomSheetButtonView buttonCast;

            @Bind(R.id.button_browser)
            BottomSheetButtonView buttonBrowser;

            BottomSheetViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}