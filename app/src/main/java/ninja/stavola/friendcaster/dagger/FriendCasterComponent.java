package ninja.stavola.friendcaster.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ninja.stavola.friendcaster.retrofit.PodcastAPI;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = { FriendCasterModule.class })
public interface FriendCasterComponent {
    OkHttpClient okHttpClient();
    PodcastAPI podcastAPI();
}
