package ninja.stavola.friendcaster.util.retrofit;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ninja.stavola.friendcaster.model.Rss;
import ninja.stavola.friendcaster.model.Rss.Item;
import ninja.stavola.friendcaster.util.FeedAdapter;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;
import retrofit.http.GET;
import retrofit.http.Query;

public class FeedGetter {
//    @Bind(R.id.progress_bar)
//    public ProgressBar progressView;

    private FeedGetter() {
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.setReadTimeout(2, TimeUnit.MINUTES);

        String SBFC_URL = "http://superbestfriendsplay.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SBFC_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(SBFCService.class);
    }

    public static FeedGetter INSTANCE;

    private final SBFCService service;

    public interface SBFCService {
        @GET("/")
        Call<Rss> getEpisodes(@Query("feed") String feed, @Query("paged") Integer page);
    }

    public void fetchEpisodes(final FeedAdapter intoAdapter, Integer page) {
        //progressView.setVisibility(View.VISIBLE);

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
                intoAdapter.addAll(items);
                intoAdapter.notifyDataSetChanged();

                //progressView.setVisibility(View.GONE);
            }
        }.execute(page);
    }

    public static FeedGetter getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FeedGetter();
        }
        return INSTANCE;
    }
}