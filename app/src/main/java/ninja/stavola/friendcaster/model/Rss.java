package ninja.stavola.friendcaster.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict = false)
public class Rss {
    @Element(name="channel", required = true)
    public Channel channel;

    @Root(name = "channel", strict = false)
    public static class Channel {
        @ElementList(name = "item", inline = true, required = true)
        public List<Item> item;
    }

    @Root(name = "item", strict = false)
    public static class Item {
        @Element(name="link", required = false)
        public String link;

        @Element(name="title", required = true)
        public String title;

        @Element(name="pubDate", required = false)
        public String pubDate;

        @Element(name="enclosure", required = true)
        public Enclosure enclosure;

        //NOT part of the RSS! We need this to make sure we don't recalculate duration!
        public String durationHolder;
    }

    @Root(name = "enclosure", strict = false)
    public static class Enclosure {
        @Attribute(name="url", required = true)
        public String url;

        @Attribute(name="type", required = true)
        public String type;
    }
}