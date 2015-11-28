package ninja.stavola.friendcaster.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.adapter.FeedRecyclerAdapter;
import ninja.stavola.friendcaster.dagger.DaggerFriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterModule;
import ninja.stavola.friendcaster.event.FeedFinishEvent;
import ninja.stavola.friendcaster.retrofit.PodcastAPI;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.layout_swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.view_feed_list)
    protected RecyclerView feedList;

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

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        feedList.setLayoutManager(layoutManager);
        feedList.setAdapter(new FeedRecyclerAdapter());
        feedList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = 35;
                }
            }
        });

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
        final FeedRecyclerAdapter feedAdapter = (FeedRecyclerAdapter) feedList.getAdapter();
        feedAdapter.clear();
        loadFeed(0);
    }

    private void loadFeed(Integer page) {
        if(!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        podcastAPI.fetchEpisodes(page);
    }

    @Subscribe
    public void onFeedLoaded(FeedFinishEvent event) {
        final FeedRecyclerAdapter feedAdapter = (FeedRecyclerAdapter) feedList.getAdapter();
        feedAdapter.addAll(event.items);
        swipeRefreshLayout.setRefreshing(false);
    }
}