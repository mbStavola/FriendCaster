package ninja.stavola.friendcaster.retrofit;

import javax.inject.Inject;
import javax.inject.Singleton;

import ninja.stavola.friendcaster.model.Feed;
import okhttp3.OkHttpClient;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

@Singleton
public class PodcastAPI {
    @Inject
    protected PodcastAPI(OkHttpClient okHttpClient) {
        String SBFC_URL = "https://friendcaster-server.herokuapp.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SBFC_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(SBFCService.class);
    }

    private final SBFCService service;

    private interface SBFCService {
        @GET("episodes")
        Observable<Feed> getEpisodes(@Query("page") Integer page);
    }

    public Observable<Feed> getEpisodes(Integer page) {
        return service.getEpisodes(page);
    }
}