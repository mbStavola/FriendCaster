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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.adapter.FeedRecyclerAdapter;
import ninja.stavola.friendcaster.dagger.DaggerFriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterComponent;
import ninja.stavola.friendcaster.dagger.FriendCasterModule;
import ninja.stavola.friendcaster.model.Feed;
import ninja.stavola.friendcaster.retrofit.PodcastAPI;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.layout_swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.view_feed_list)
    protected RecyclerView feedList;

    private PodcastAPI podcastAPI;

    private boolean hasNext;
    private Integer currentPage = 0;

    //TODO: Implement theme switching
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        final FriendCasterComponent friendCasterComponent = DaggerFriendCasterComponent.builder()
                .friendCasterModule(new FriendCasterModule())
                .build();

        podcastAPI = friendCasterComponent.podcastAPI();

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

        decorateFeedList();

        loadFeed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
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
        currentPage = 0;
        loadFeed(0);
    }

    private void loadFeed(final Integer page) {
        if(!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        Observable<Feed> feedObservable = podcastAPI.getEpisodes(page);

        feedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Feed>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Feed feed) {
                        final FeedRecyclerAdapter feedAdapter = (FeedRecyclerAdapter) feedList.getAdapter();
                        feedAdapter.addAll(feed.episodeList);

                        hasNext = feed.hasNext;
                    }
                });
    }

    private void decorateFeedList() {
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

        feedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    int totalItemCount = layoutManager.getItemCount();
                    int visibleItemCount = layoutManager.getChildCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    boolean isAtBottom = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount;

                    if(hasNext && isAtBottom) {
                        hasNext = false;
                        currentPage++;
                        loadFeed(currentPage);
                    }
                }
            }
        });
    }
}