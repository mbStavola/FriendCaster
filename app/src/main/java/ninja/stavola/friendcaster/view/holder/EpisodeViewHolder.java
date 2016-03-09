package ninja.stavola.friendcaster.view.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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

    public  View bottomSheetView;

    private Context context;

    public EpisodeViewHolder(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);

        this.context = context;
        this.bottomSheetView =
                LayoutInflater.from(context).inflate(R.layout.view_bottom_sheet, null);
    }

    @OnClick(R.id.episode_card)
    public void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);

        ViewParent parent = bottomSheetView.getParent();
        if(parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(bottomSheetView);
        }

        bottomSheetDialog.contentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
