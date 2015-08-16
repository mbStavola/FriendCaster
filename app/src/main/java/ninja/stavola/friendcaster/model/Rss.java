package ninja.stavola.friendcaster.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@SuppressWarnings("unused")
@Root(name = "rss")
public class Rss {
    @Element(name="channel", required = false)
    public Channel channel;

    @Attribute(name="version", required = false)
    public String version;

    @Attribute(name="content", required = false)
    public String content;

    @Attribute(name="wfw", required = false)
    public String wfw;

    @Attribute(name="dc", required = false)
    public String dc;

    @Attribute(name="atom", required = false)
    public String atom;

    @Attribute(name="sy", required = false)
    public String sy;

    @Attribute(name="slash", required = false)
    public String slash;

    @Attribute(name="itunes", required = false)
    public String itunes;

    public static class Channel {
        @Element(name="title", required = false)
        public String title;

        @Element(name="link", required = false)
        public String link;

        @Element(name="description", required = false)
        public String description;

        @Element(name="lastBuildDate", required = false)
        public String lastBuildDate;

        @Element(name="language", required = false)
        public String language;

        @Element(name="copyright", required = false)
        public String copyright;

        @Element(name="subtitle", required = false)
        public String subtitle;

        @Element(name="author", required = false)
        public String author;

        @Element(name="summary", required = false)
        public String summary;

        @Element(name="owner", required = false)
        public Owner owner;

        @Element(name="explicit", required = false)
        public String explicit;

        @Element(name="image", required = false)
        public Image image;

        @Element(name="category", required = false)
        public Category category;

        @ElementList(name = "item", inline = true, required = false)
        public List<Item> item;
    }

    public static class Link {
        @Attribute(name="href", required = false)
        public String href;

        @Attribute(name="rel", required = false)
        public String rel;

        @Attribute(name="type", required = false)
        public String type;
    }

    public static class Owner {
        @Element(name="name", required = false)
        public String name;

        @Element(name="email", required = false)
        public String email;
    }

    public static class Image {
        @Attribute(name="href", required = false)
        public String href;
    }

    public static class Category {
        @Element(name="category", required = false)
        public Category category;

        @Attribute(name="text", required = false)
        public String text;
    }

    public static class Item {
        @Element(name="title", required = false)
        public String title;

        @Element(name="link", required = false)
        public String link;

        @Element(name="pubDate", required = false)
        public String pubDate;

        @Element(name="creator", required = false)
        public String creator;

        @Element(name="guid", required = false)
        public Guid guid;

        @Element(name="description", required = false)
        public String description;

        @Element(name="subtitle", required = false)
        public String subtitle;

        @Element(name="encoded", required = false)
        public String encoded;

        @Element(name="summary", required = false)
        public String summary;

        @Element(name="enclosure", required = false)
        public Enclosure enclosure;

        @Element(name="explicit", required = false)
        public String explicit;

        @Element(name="block", required = false)
        public String block;

        @Element(name="duration", required = false)
        public String duration;

        @Element(name="author", required = false)
        public String author;
    }

    public static class Guid {
        @Attribute(name="isPermaLink", required = false)
        public Boolean isPermaLink;
    }

    public static class Enclosure {
        @Attribute(name="url", required = false)
        public String url;

        @Attribute(name="length", required = false)
        public String length;

        @Attribute(name="type", required = false)
        public String type;
    }
}