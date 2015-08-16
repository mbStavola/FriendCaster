package ninja.stavola.friendcaster.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ListView;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        final Context context = container.getContext();

        feedList.setAdapter(new FeedAdapter(context, 0));
        feedList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadFeed(page);
            }
        });

        loadFeed();

        return view;
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