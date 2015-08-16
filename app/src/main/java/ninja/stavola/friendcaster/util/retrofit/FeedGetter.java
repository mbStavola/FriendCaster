package ninja.stavola.friendcaster.util.retrofit;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import ninja.stavola.friendcaster.model.Rss;
import ninja.stavola.friendcaster.model.Rss.Item;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;

public class FeedGetter {
    private FeedGetter() {
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.setReadTimeout(10, TimeUnit.MINUTES);

        String SBFC_URL = "http://superbestfriendsplay.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SBFC_URL)
                .converterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(SBFCService.class);
    }

    public static FeedGetter INSTANCE;

    private final SBFCService service;

    public interface SBFCService {
        @GET("/?feed=podcast")
        Call<Rss> getEpisodes();
    }

    public List<Item> getEpisodes() {
        try {
            return new AsyncTask<Integer, Void, List<Item>>() {
                @Override
                protected List<Item> doInBackground(Integer... params) {
                    Call<Rss> call = service.getEpisodes();
                    Response<Rss>  response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Rss rss = response.body();
                    return rss.channel.item;
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static FeedGetter getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FeedGetter();
        }
        return INSTANCE;
    }
}
