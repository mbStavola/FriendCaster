package ninja.stavola.friendcaster.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;

import it.gmariotti.cardslib.library.view.CardView;

public class FeedAdapter extends ArrayAdapter<SyndEntry>{
    //TODO: Wait, why does this need a layout? Ask Kevin again
    public FeedAdapter(Context context, @LayoutRes int layout) {
        super(context, layout);
    }

    //TODO: Actually set up the episode card
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new CardView(getContext());
        }

        final SyndEntry syndEntry = getItem(position);
        CardView convertViewAsCardView = ((CardView) convertView);

        String[] fullTitle = syndEntry.getTitle().split(": ");
        String episodeNumber = fullTitle[0].split(" ")[1];
        String episodeTitle = fullTitle[1];

        SyndEnclosure syndEnclosure = syndEntry.getEnclosures().get(0);
        long episodeLength = syndEnclosure.getLength();

        final String entryMediaFileUrl = syndEnclosure.getUrl();
        final String entryMediaMime = syndEnclosure.getType();

        //TODO: Implement
        convertViewAsCardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}