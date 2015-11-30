package ninja.stavola.friendcaster.event;

import java.util.List;

import ninja.stavola.friendcaster.model.Feed;
import ninja.stavola.friendcaster.model.Feed.Episode;

public class FeedFinishEvent {
    public boolean hasNext;
    public List<Episode> episodes;

    public FeedFinishEvent(Feed feed) {
        this.hasNext = feed.hasNext;
        this.episodes = feed.episodeList;
    }
}