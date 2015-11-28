package ninja.stavola.friendcaster.event;

import java.util.List;

import ninja.stavola.friendcaster.model.Feed.Wrapper.Episode.Item;

public class FeedFinishEvent {
    public List<Item> items;

    public FeedFinishEvent(List<Item> items) {
        this.items = items;
    }
}