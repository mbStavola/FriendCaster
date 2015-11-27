package ninja.stavola.friendcaster.retrofit;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ninja.stavola.friendcaster.event.FeedFinishEvent;
import ninja.stavola.friendcaster.model.Rss;
import ninja.stavola.friendcaster.model.Rss.Item;
import ninja.stavola.friendcaster.util.FeedAdapter;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;
import retrofit.http.GET;
import retrofit.http.Query;

@Singleton
public class PodcastAPI {
    private Bus bus;

    @Inject
    protected PodcastAPI(Bus bus, OkHttpClient okHttpClient) {
        this.bus = bus;
        bus.register(this);

        String SBFC_URL = "http://superbestfriendsplay.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SBFC_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(SBFCService.class);
    }

    private final SBFCService service;

    public interface SBFCService {
        @GET("/")
        Call<Rss> getEpisodes(@Query("feed") String feed, @Query("paged") Integer page);
    }

    public void fetchEpisodes(Integer page) {
        new AsyncTask<Integer, Void, List<Item>>() {
            @Override
            protected List<Item> doInBackground(Integer... params) {
                Call<Rss> call = service.getEpisodes("podcast", params[0]);
                Response<Rss>  response = null;

                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Rss rss = response.body();
                return rss.channel.item;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                bus.post(new FeedFinishEvent(items));
            }
        }.execute(page);
    }
}