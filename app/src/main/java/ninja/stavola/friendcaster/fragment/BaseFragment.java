package ninja.stavola.friendcaster.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import ninja.stavola.friendcaster.activity.MainActivity;
import ninja.stavola.friendcaster.util.BusProvider;

public abstract class BaseFragment extends Fragment {
    protected Bus bus;

    public MainActivity getMainActivity() {
        try {
            return ((MainActivity) getActivity());
        } catch (ClassCastException e) {
            Log.e("FriendCaster", "One of Nyarlathotep's many maddening forms has appeared");
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        bus = BusProvider.getInstance().provideBus();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
        bus.unregister(this);
    }

    protected abstract @LayoutRes int getLayoutId();
}