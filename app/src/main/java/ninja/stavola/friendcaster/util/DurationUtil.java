package ninja.stavola.friendcaster.util;

import android.support.annotation.WorkerThread;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

//TODO: For whatever reason, if you scroll really fast you get ridiculous times (ex: 9+ hours)
public class DurationUtil {
    @WorkerThread
    public static String getDuration(String urlString) {
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
}
