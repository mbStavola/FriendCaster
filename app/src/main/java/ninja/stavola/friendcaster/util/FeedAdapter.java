package ninja.stavola.friendcaster.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rey.material.widget.TextView;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
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

public class FeedAdapter extends ArrayAdapter<SyndEntry>{
    public FeedAdapter(Context context, @LayoutRes int layout) {
        super(context, layout);
    }

    //TODO: Actually set up the episode card
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EpisodeViewHolder episodeHolder;

        if (view != null) {
            episodeHolder = (EpisodeViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.card_episode, parent, false);
            episodeHolder = new EpisodeViewHolder(view);
            view.setTag(episodeHolder);
        }

        final SyndEntry syndEntry = getItem(position);

        //Format for the entry title is "SBFC <Episode Number>: <Episode Title>"
        final String[] fullTitle = syndEntry.getTitle().split(": ");
        episodeHolder.episodeNumber.setText(fullTitle[0].split(" ")[1]);
        episodeHolder.episodeTitle.setText(fullTitle[1]);

        episodeHolder.episodeDate.setText(getLocalDateTimeString(syndEntry.getPublishedDate()));

        //Get the media file for the entry
        final SyndEnclosure syndEnclosure = syndEntry.getEnclosures().get(0);
        episodeHolder.episodeMediaFileUrl = syndEnclosure.getUrl();
        episodeHolder.episodeMediaMime = syndEnclosure.getType();

        episodeHolder.episodeLength.setText(getDuration(syndEnclosure.getUrl()));

        episodeHolder.episodeSummaryHtml = syndEntry.getContents().get(0).toString();

        return view;
    }

    private String getLocalDateTimeString(Date date) {
        LocalDateTime localDateTime =
                new LocalDateTime(date, DateTimeZone.forTimeZone(TimeZone.getDefault()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("m/dd/yy");

        return localDateTime.toString(dateTimeFormatter);
    }

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

        //We do (length / 1024) * 8 to get length in Kib and then divide by the bitrate (128)
        //So why not shorten it to (length / 16384)?
        double lengthInSeconds = length / 16384;

        //Round up and cast to int
        int roundedLengthInSeconds = (int) (lengthInSeconds + 0.5);

        Period period = new Period(Seconds.seconds(roundedLengthInSeconds));

        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();

        return periodFormatter.print(period.normalizedStandard());
    }

    private static class EpisodeViewHolder {
        @Bind(R.id.episode_number)
        public TextView episodeNumber;

        @Bind(R.id.episode_title)
        public TextView episodeTitle;

        @Bind(R.id.episode_length)
        public TextView episodeLength;

        @Bind(R.id.episode_date)
        public TextView episodeDate;

        public String episodeMediaFileUrl;
        public String episodeMediaMime;
        public String episodeSummaryHtml;

        public EpisodeViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        //TODO: Bundle important info and open EpisodeDetailFragment
        @OnClick
        public void openEpisodeDetailFragment() {
        }
    }
}