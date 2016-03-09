package ninja.stavola.friendcaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.model.Feed.Episode;
import ninja.stavola.friendcaster.view.holder.BottomSheetViewHolder;
import ninja.stavola.friendcaster.view.holder.EpisodeViewHolder;

public class FeedRecyclerAdapter extends ArrayRecyclerAdapter<Episode, EpisodeViewHolder> {
    private Context context;

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View v = LayoutInflater.from(context).inflate(R.layout.card_episode, parent, false);

        return new EpisodeViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {
        Episode episode = getElement(position);

        //Format for the entry title is "SBFC <Episode Number>: <Episode Title>"
        holder.episodeTitle.setText(episode.title);

        //Convert time info to something easier to read
        long duration = episode.duration;

        String durationString;
        if(duration > 0) {
            durationString = context.getString(R.string.label_duration, getFormattedDuration((int) duration));
        } else {
            durationString = context.getString(R.string.label_duration_unknown);
        }

        holder.episodeLength.setText(durationString);

        String dateString = context.getString(R.string.label_date, getLocalDateTimeString(episode.date));
        holder.episodeDate.setText(dateString);

        //Note: This stuff feels kinda dirty
        BottomSheetViewHolder bottomSheetViewHolder = new BottomSheetViewHolder(holder.bottomSheetView, context);

        //Get the media file for the entry
        bottomSheetViewHolder.episodeMediaFileUrl = episode.mp3Link;
        bottomSheetViewHolder.episodeMediaMime = episode.mimeType;

        bottomSheetViewHolder.fileName = episode.title.substring(0, episode.title.indexOf(':')).replace(" ", "_") + ".mp3";

        bottomSheetViewHolder.linkToEpisode = episode.episodeLink;
    }

    private String getLocalDateTimeString(String date) {
        LocalDateTime localDateTime =
                new LocalDateTime(new Date(date), DateTimeZone.forTimeZone(TimeZone.getDefault()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("M/dd/yy");

        return localDateTime.toString(dateTimeFormatter);
    }

    private String getFormattedDuration(int seconds) {
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
}
