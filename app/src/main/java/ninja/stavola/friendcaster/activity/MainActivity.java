package ninja.stavola.friendcaster.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;

import ninja.stavola.friendcaster.R;
import ninja.stavola.friendcaster.fragment.FeedFragment;

public class MainActivity extends AppCompatActivity {

    //TODO: Implement theme switching
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setColorTheme(R.color.dark_action_bar, R.color.dark_status_navigation_bar);

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
        String currentFragment = getFragmentManager()
                .getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();

        if (!currentFragment.equals("fragment_feed")) {
            getFragmentManager().popBackStack();
        }
    }

    public void setColorTheme(@ColorRes int actionBarColorId, @ColorRes int statusAndNavigationBarColorId) {
        final int actionBarColor = getResources().getColor(actionBarColorId);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int statusAndNavigationBarColor =
                    getResources().getColor(statusAndNavigationBarColorId);

            getWindow().setStatusBarColor(statusAndNavigationBarColor);
            getWindow().setNavigationBarColor(statusAndNavigationBarColor);
        }
    }
}