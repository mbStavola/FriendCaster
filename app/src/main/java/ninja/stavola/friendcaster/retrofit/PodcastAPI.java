package ninja.stavola.friendcaster.retrofit;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ninja.stavola.friendcaster.event.FeedFinishEvent;
import ninja.stavola.friendcaster.model.Feed;
import ninja.stavola.friendcaster.model.Feed.Episode;
import retrofit.Call;
import retrofit.MoshiConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

@Singleton
public class PodcastAPI {
    private Bus bus;

    @Inject
    protected PodcastAPI(Bus bus, OkHttpClient okHttpClient) {
        this.bus = bus;
        bus.register(this);

        String SBFC_URL = "https://friendcaster-server.herokuapp.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SBFC_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(SBFCService.class);
    }

    private final SBFCService service;

    public interface SBFCService {
        @GET("/episodes")
        Call<Feed> getEpisodes(@Query("page") Integer page);
    }

    public void fetchEpisodes(Integer page) {
        new AsyncTask<Integer, Void, Feed>() {
            @Override
            protected Feed doInBackground(Integer... params) {
                Call<Feed> call = service.getEpisodes(params[0]);
                Response<Feed> response = null;

                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Feed feed = response.body();
                return feed;
            }

            @Override
            protected void onPostExecute(Feed feed) {
                bus.post(new FeedFinishEvent(feed));
            }
        }.execute(page);
    }
}