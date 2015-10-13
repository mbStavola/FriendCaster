package ninja.stavola.friendcaster.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.rey.material.widget.ProgressView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.dagger.DaggerFriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterModule;
import ninja.stavola.friendcaster.event.FeedFinishEvent;
import ninja.stavola.friendcaster.model.Rss;
import ninja.stavola.friendcaster.retrofit.PodcastAPI;
import ninja.stavola.friendcaster.util.DurationUtil;
import ninja.stavola.friendcaster.util.EndlessScrollListener;
import ninja.stavola.friendcaster.util.FeedAdapter;

public class MainActivity extends AppCompatActivity {
    private PodcastAPI podcastAPI;
    private Bus bus;

    @Bind(R.id.layout_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.view_feed_list)
    ListView feedList;

    @Bind(R.id.progress_bar)
    ProgressView progressBar;

    //TODO: Implement theme switching
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setColorTheme(R.color.dark_accent, R.color.dark_status_bar);

        ButterKnife.bind(this);

        FriendCasterComponent friendCasterComponent = DaggerFriendCasterComponent.builder()
                .friendCasterModule(new FriendCasterModule())
                .build();

        podcastAPI = friendCasterComponent.podcastAPI();

        bus = friendCasterComponent.provideBus();
        bus.register(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeed();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.dark_accent);

        feedList.setAdapter(new FeedAdapter(this, 0));
        feedList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //TODO: Page 2 produces an NPE (because they have no pagination!)
                loadFeed(page);
            }
        });

        loadFeed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
        bus.unregister(this);
    }

    public void setColorTheme(@ColorRes int actionBarColorId, @ColorRes int statusBarColorId) {
        final int actionBarColor = getResources().getColor(actionBarColorId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int statusBarColor =
                    getResources().getColor(statusBarColorId);

            getWindow().setStatusBarColor(statusBarColor);
        }
    }

    private void loadFeed() {
        loadFeed(0);
    }

    private void loadFeed(Integer page) {
        final FeedAdapter feedAdapter = (FeedAdapter) feedList.getAdapter();

        progressBar.setVisibility(View.VISIBLE);
        podcastAPI.fetchEpisodes(feedAdapter, page);

        //Since there is no duration returned by the RSS, we populate a holder in the model
        //(with our own calculated duration) so we don't have to ever recalculate!
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < feedAdapter.getCount(); i++) {
                    Rss.Item item = feedAdapter.getItem(i);
                    item.durationHolder = DurationUtil.getDuration(item.enclosure.url);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                feedAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Subscribe
    public void onFeedLoaded(FeedFinishEvent event) {
        progressBar.setVisibility(View.GONE);
    }
}