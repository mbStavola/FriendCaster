package ninja.stavola.friendcaster.model;

import java.util.Date;

public class Item {
    public String title;
    public Date pubDate;

    public Encoded description;
    public Enclosure enclosure;

    public static class Encoded {
        public String __cdata;
    }

    public static class Enclosure {
        public String _url;
        public String _type;
    }
}
