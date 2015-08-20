package ninja.stavola.friendcaster.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.fragment.FeedFragment;

public class MainActivity extends AppCompatActivity {
    //TODO: Implement theme switching
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setColorTheme(R.color.dark_accent, R.color.dark_status_bar);

        openFeedFragment();
    }

    private void openFeedFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.root, new FeedFragment())
                .addToBackStack("fragment_feed")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void setColorTheme(@ColorRes int actionBarColorId, @ColorRes int statusBarColorId) {
        final int actionBarColor = getResources().getColor(actionBarColorId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int statusBarColor =
                    getResources().getColor(statusBarColorId);

            getWindow().setStatusBarColor(statusBarColor);
        }
    }
}