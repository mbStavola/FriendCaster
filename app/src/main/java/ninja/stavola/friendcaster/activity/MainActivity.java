package ninja.stavola.friendcaster.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
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
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.dagger.DaggerFriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterModule;
import ninja.stavola.friendcaster.event.FeedFinishEvent;
import ninja.stavola.friendcaster.model.Rss.Item;
import ninja.stavola.friendcaster.retrofit.PodcastAPI;
import ninja.stavola.friendcaster.util.DurationUtil;
import ninja.stavola.friendcaster.util.FeedAdapter;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.layout_swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.view_feed_list)
    protected ListView feedList;

    private PodcastAPI podcastAPI;
    private Bus bus;

    //TODO: Implement theme switching
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        FriendCasterComponent friendCasterComponent = DaggerFriendCasterComponent.builder()
                .friendCasterModule(new FriendCasterModule())
                .build();

        podcastAPI = friendCasterComponent.podcastAPI();

        bus = friendCasterComponent.provideBus();
        bus.register(this);

        toolbar.setTitle("FriendCaster");
        setSupportActionBar(toolbar);

        setColorTheme(R.color.dark_accent, R.color.dark_status_bar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeed();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.dark_accent);

        feedList.setAdapter(new FeedAdapter(this, 0));
        loadFeed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
        bus.unregister(this);
    }

    @OnClick(R.id.button_information)
    public void loadInformation() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/mbStavola/FriendCaster"));
        this.startActivity(intent);
    }

    private void setColorTheme(@ColorRes int actionBarColorId, @ColorRes int statusBarColorId) {
        final int toolbarColor = getResources().getColor(actionBarColorId);
        toolbar.setBackground(new ColorDrawable(toolbarColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int statusBarColor =
                    getResources().getColor(statusBarColorId);

            getWindow().setStatusBarColor(statusBarColor);
        }
    }

    private void loadFeed() {
        loadFeed(0);
    }

    //TODO: If/when they implement paging we'll actually use this method directly
    private void loadFeed(Integer page) {
        if(!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        podcastAPI.fetchEpisodes(page);
    }

    @Subscribe
    public void onFeedLoaded(FeedFinishEvent event) {
        final FeedAdapter feedAdapter = (FeedAdapter) feedList.getAdapter();
        feedAdapter.addAll(event.items);
        swipeRefreshLayout.setRefreshing(false);

        //Since there is no duration returned by the RSS, we populate a holder in the model
        //(with our own calculated duration) so we don't have to ever recalculate!
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < feedAdapter.getCount(); i++) {
                    Item item = feedAdapter.getItem(i);
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
}