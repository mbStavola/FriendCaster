package ninja.stavola.friendcaster.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Feed {
    public boolean hasNext;
    public List<Episode> episodeList = new ArrayList<>();

    public static class Episode {
        public String episodeLink;
        public String title;
        public String date;
        public long duration;
        public String mp3Link;
        public String mimeType;
    }
}
