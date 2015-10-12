package ninja.stavola.friendcaster.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.rey.material.widget.ProgressView;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.event.FeedFinishEvent;
import ninja.stavola.friendcaster.model.Rss;
import ninja.stavola.friendcaster.util.DurationUtil;
import ninja.stavola.friendcaster.util.EndlessScrollListener;
import ninja.stavola.friendcaster.util.FeedAdapter;
import ninja.stavola.friendcaster.util.retrofit.FeedGetter;

public class FeedFragment extends BaseFragment {
    @Bind(R.id.view_feed_list)
    public ListView feedList;

    @Bind(R.id.progress_bar)
    public ProgressView progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedList.setAdapter(new FeedAdapter(getActivity(), 0));
        feedList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //TODO: Page 2 produces and NPE (because they have no pagination!)
                loadFeed(page);
            }
        });

        loadFeed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadFeed();
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed;
    }

    private void loadFeed() {
        loadFeed(0);
    }

    private void loadFeed(Integer page) {
        final FeedAdapter feedAdapter = (FeedAdapter) feedList.getAdapter();

        progressBar.setVisibility(View.VISIBLE);
        FeedGetter.getInstance().fetchEpisodes(feedAdapter, page);

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
        }.execute();
    }

    @Subscribe
    public void onFeedLoaded(FeedFinishEvent event) {
        progressBar.setVisibility(View.GONE);
    }
}