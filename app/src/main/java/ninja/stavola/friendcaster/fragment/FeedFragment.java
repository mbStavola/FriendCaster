package ninja.stavola.friendcaster.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ListView;

import butterknife.Bind;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.model.Item;
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
        loadFeed();

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed;
    }

    private void loadFeed() {
        final FeedAdapter feedAdapter = (FeedAdapter) feedList.getAdapter();

        final Item[] entries = FeedGetter.getInstance().getEpisodes();

        for(Item entry : entries) {
            feedAdapter.add(entry);
        }

        feedAdapter.notifyDataSetChanged();
    }
}