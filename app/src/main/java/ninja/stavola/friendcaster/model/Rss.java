package ninja.stavola.friendcaster.model;

import java.util.List;

public class Rss {
    public Channel channel;

    public static class Channel {
        public List<Item> items;
    }
}
