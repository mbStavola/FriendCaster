package ninja.stavola.friendcaster.event;

import java.util.List;

import ninja.stavola.friendcaster.model.Feed.Episode;

public class FeedFinishEvent {
    public List<Episode> episodes;

    public FeedFinishEvent(List<Episode> episodes) {
        this.episodes = episodes;
    }
}