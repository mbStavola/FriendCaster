package ninja.stavola.friendcaster.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ListView;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.util.FeedAdapter;

public class FeedFragment extends BaseFragment {
    private final String SBFC_URL = "http://superbestfriendsplay.com/?feed=podcast";

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

        final List<SyndEntry> entries = getEntries();

        for(SyndEntry entry : entries) {
            feedAdapter.add(entry);
        }

        feedAdapter.notifyDataSetChanged();
    }

    //TODO: This method is DISGUSTING! Refactor it!
    //TODO: I wonder if we can get entries by "page," would make load times much smaller
    private List<SyndEntry> getEntries() {
        final SyndFeedInput input = new SyndFeedInput();

        try {
            return new AsyncTask<Void, Void, List<SyndEntry>>() {
                @Override
                protected List<SyndEntry> doInBackground(Void... params) {
                    try {
                        return input.build(new XmlReader(new URL(SBFC_URL))).getEntries();
                    } catch (FeedException | IOException e) {
                        e.printStackTrace();
                    }
                    return Collections.emptyList();
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}