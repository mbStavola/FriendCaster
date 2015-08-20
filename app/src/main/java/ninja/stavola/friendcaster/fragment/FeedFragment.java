package ninja.stavola.friendcaster.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import butterknife.Bind;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.util.EndlessScrollListener;
import ninja.stavola.friendcaster.util.FeedAdapter;
import ninja.stavola.friendcaster.util.retrofit.FeedGetter;

public class FeedFragment extends BaseFragment {
    @Bind(R.id.view_feed_list)
    public ListView feedList;

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

        FeedGetter.getInstance().fetchEpisodes(feedAdapter, page);
    }
}