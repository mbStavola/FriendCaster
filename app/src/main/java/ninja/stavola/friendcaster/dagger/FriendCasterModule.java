package ninja.stavola.friendcaster.dagger;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FriendCasterModule {
    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1, TimeUnit.MINUTES);

        return okHttpClient;
    }
}
