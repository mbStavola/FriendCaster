package ninja.stavola.friendcaster.util;

import android.media.MediaPlayer;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class DurationUtil {
    @WorkerThread
    public static String getDuration(String urlString) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(urlString);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.
        float lengthInSeconds = mediaPlayer.getDuration() / 1000;
        mediaPlayer.release();

        //Round up and cast to int
        int roundedLengthInSeconds = Math.round(lengthInSeconds);

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
}
