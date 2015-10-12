package ninja.stavola.friendcaster.dagger;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import ninja.stavola.friendcaster.util.retrofit.PodcastAPI;

@Singleton
@Component(modules = { FriendCasterModule.class })
public interface FriendCasterComponent {
    Bus provideBus();
    OkHttpClient okHttpClient();
    PodcastAPI podcastAPI();
}
