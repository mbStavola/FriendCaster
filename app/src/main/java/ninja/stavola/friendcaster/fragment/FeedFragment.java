package ninja.stavola.friendcaster.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.model.Rss.Item;
import ninja.stavola.friendcaster.util.EndlessScrollListener;
import ninja.stavola.friendcaster.util.FeedAdapter;
import ninja.stavola.friendcaster.util.retrofit.FeedGetter;

public class FeedFragment extends BaseFragment {
    @Bind(R.id.view_feed_list)
    public ListView feedList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedList.setAdapter(new FeedAdapter(getActivity(), 0));
        feedList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //loadFeed(page);
            }
        });

        loadFeed();
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

        final List<Item> entries = FeedGetter.getInstance().getEpisodes(page);

        for(Item entry : entries) {
            feedAdapter.add(entry);
        }

        feedAdapter.notifyDataSetChanged();
    }
}