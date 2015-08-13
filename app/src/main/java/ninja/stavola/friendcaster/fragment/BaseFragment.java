package ninja.stavola.friendcaster.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.activity.MainActivity;

public abstract class BaseFragment extends Fragment {
    public MainActivity getMainActivity() {
        try {
            return ((MainActivity) getActivity());
        } catch (ClassCastException e) {
            Log.e("FriendCaster", "One of Nyarlathotep's many maddening forms has appeared");
        }
        return null;
    }

    private void openFragment(BaseFragment fragment, String fragmentName) {
        getMainActivity().getFragmentManager().beginTransaction()
                .replace(R.id.root, fragment)
                .addToBackStack(fragmentName)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        ButterKnife.bind(this, view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
    }

    protected abstract @LayoutRes int getLayoutId();
}