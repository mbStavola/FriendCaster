package ninja.stavola.friendcaster.util.retrofit;


import android.os.AsyncTask;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ninja.stavola.friendcaster.model.Item;
import ninja.stavola.friendcaster.model.Meta;
import ninja.stavola.friendcaster.model.Rss;
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
        Meta getEpisodes();

    }

    public List<Item> getEpisodes() {
        try {
            return new AsyncTask<Integer, Void, List<Item>>() {
                @Override
                protected List<Item> doInBackground(Integer... params) {
                    Meta meta = service.getEpisodes();
                    return meta.rss.channel.items;
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
