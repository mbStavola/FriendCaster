package ninja.stavola.friendcaster.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.LayoutRes;
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

import java.util.Date;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.model.Feed.Wrapper.Episode.Item;
import ninja.stavola.friendcaster.model.Feed.Wrapper.Episode.Item.Content;
import ninja.stavola.friendcaster.view.BottomSheetButtonView;

public class FeedAdapter extends ArrayAdapter<Item> {
    private Context context;

    public FeedAdapter(Context context, @LayoutRes int layout) {
        super(context, layout);
        this.context = context;
    }

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

        final Content item = getItem(position).map;

        //Format for the entry title is "SBFC <Episode Number>: <Episode Title>"
        String title = item.title;
        episodeHolder.episodeTitle.setText(title.substring(5));
        episodeHolder.fileName = title.substring(0, title.indexOf(':')).replace(" ", "_") + ".mp3";

        episodeHolder.episodeDate.setText(getLocalDateTimeString(new Date(item.pubDate)));

        //Get the media file for the entry
        episodeHolder.episodeMediaFileUrl = item.enclosure.map.url;
        episodeHolder.episodeMediaMime = item.enclosure.map.type;

        episodeHolder.episodeLength.setText(getDurationFromSeconds(item.duration));

        episodeHolder.linkToEpisode = item.link;

        return view;
    }

    private String getLocalDateTimeString(Date date) {
        LocalDateTime localDateTime =
                new LocalDateTime(date, DateTimeZone.forTimeZone(TimeZone.getDefault()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("M/dd/yy");

        return localDateTime.toString(dateTimeFormatter);
    }

    private String getDurationFromSeconds(int seconds) {
        Period period = new Period(Seconds.seconds(seconds));

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