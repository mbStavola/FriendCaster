package ninja.stavola.friendcaster.view.holder;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ninja.stavola.friendcaster.R;

@SuppressWarnings("unused")
public class EpisodeViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.episode_title)
    public TextView episodeTitle;

    @Bind(R.id.episode_length)
    public TextView episodeLength;

    @Bind(R.id.episode_date)
    public TextView episodeDate;

    private Dialog dialog;

    public EpisodeViewHolder(View view, BottomSheetDialog dialog) {
        super(view);

        ButterKnife.bind(this, view);
        this.dialog = dialog;
    }

    @OnClick(R.id.episode_card)
    public void showBottomSheet() {
        dialog.show();
    }
}
