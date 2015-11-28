package ninja.stavola.friendcaster.model;

import java.util.List;

@SuppressWarnings("unused")
public class Feed {
    public Wrapper map;

    public static class Wrapper {
        public boolean hasNext;

        public Episode episodes;

        public static class Episode {
            public List<Item> myArrayList;

            public static class Item {
                public Content map;

                public static class Content {
                    public String link;

                    public String title;

                    public String pubDate;

                    public int duration;

                    public Enclosure enclosure;

                    public static class Enclosure {
                        public File map;

                        public static class File {
                            public String url;

                            public String type;
                        }
                    }
                }
            }
        }
    }
}
