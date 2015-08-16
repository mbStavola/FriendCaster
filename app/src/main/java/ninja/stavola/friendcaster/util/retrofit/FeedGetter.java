package ninja.stavola.friendcaster.util.retrofit;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ninja.stavola.friendcaster.model.Item;
import ninja.stavola.friendcaster.model.Rss;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;

public class FeedGetter {
    private FeedGetter() {}

    public static FeedGetter INSTANCE;

    private final String SBFC_URL = "http://superbestfriendsplay.com/";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SBFC_URL)
            .converterFactory(SimpleXmlConverterFactory.create())
            .build();

    private final SBFCService service = retrofit.create(SBFCService.class);

    public interface SBFCService {
        @GET("/?feed=podcast")
        Call<Rss> getEpisodes();
    }

    public Item[] getEpisodes() {
        try {
            return new AsyncTask<Integer, Void, Item[]>() {
                @Override
                protected Item[] doInBackground(Integer... params) {
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
        return new Item[0];
    }

    public static FeedGetter getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FeedGetter();
        }
        return INSTANCE;
    }
}
