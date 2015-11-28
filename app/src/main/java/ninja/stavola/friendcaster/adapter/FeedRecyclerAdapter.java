package ninja.stavola.friendcaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.model.Feed.Wrapper.Episode.Item;
import ninja.stavola.friendcaster.model.Feed.Wrapper.Episode.Item.Content;
import ninja.stavola.friendcaster.view.holder.BottomSheetViewHolder;
import ninja.stavola.friendcaster.view.holder.EpisodeViewHolder;

public class FeedRecyclerAdapter extends ArrayRecyclerAdapter<Item, EpisodeViewHolder> {
    private Context context;

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View v = LayoutInflater.from(context).inflate(R.layout.card_episode, parent, false);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.view_bottom_sheet, null);

        return new EpisodeViewHolder(v, bottomSheetView, bottomSheetDialog);
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {
        Content content = getElement(position).map;

        //Format for the entry title is "SBFC <Episode Number>: <Episode Title>"
        holder.episodeTitle.setText(content.title);

        //Convert time info to something easier to read
        holder.episodeLength.setText(getFormattedDuration(content.duration));
        holder.episodeDate.setText(getLocalDateTimeString(new Date(content.pubDate)));

        //Note: This stuff feels kinda dirty
        BottomSheetViewHolder bottomSheetViewHolder = new BottomSheetViewHolder(holder.dialogView, context);

        //Get the media file for the entry
        bottomSheetViewHolder.episodeMediaFileUrl = content.enclosure.map.url;
        bottomSheetViewHolder.episodeMediaMime = content.enclosure.map.type;

        bottomSheetViewHolder.fileName = content.title.substring(0, content.title.indexOf(':')).replace(" ", "_") + ".mp3";

        bottomSheetViewHolder.linkToEpisode = content.link;
    }

    private String getLocalDateTimeString(Date date) {
        LocalDateTime localDateTime =
                new LocalDateTime(date, DateTimeZone.forTimeZone(TimeZone.getDefault()));

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
