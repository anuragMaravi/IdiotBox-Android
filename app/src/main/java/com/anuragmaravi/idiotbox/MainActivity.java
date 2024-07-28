package com.anuragmaravi.idiotbox;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anuragmaravi.idiotbox.ui.fragments.movies.MoviesFragment;
import com.anuragmaravi.idiotbox.ui.fragments.profile.ProfileFragment;
import com.anuragmaravi.idiotbox.ui.fragments.tvshows.TvShowFragment;
import com.anuragmaravi.idiotbox.ui.fragments.wishlist.WishlistFragment;
import com.anuragmaravi.idiotbox.ui.activities.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomNavigationView navigation;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the no title feature
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        toolbarTitle = findViewById(R.id.toolbar_title);

        navigation = findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager = getSupportFragmentManager();

        // Set default fragment
        fragment = new MoviesFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }

    private final BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_movies) {
                updateToolbar("MOVIES", R.color.colorAccent);
                fragment = new MoviesFragment();
                updateNavigationViewColors(R.id.navigation_movies, R.color.colorAccent);
            } else if (itemId == R.id.navigation_tv_shows) {
                updateToolbar("TV SHOWS", R.color.tv_show_accent);
                fragment = new TvShowFragment();
                updateNavigationViewColors(R.id.navigation_tv_shows, R.color.tv_show_accent);
            } else if (itemId == R.id.navigation_wishlist) {
                updateToolbar("WISHLIST", R.color.tmdbColor);
                fragment = new WishlistFragment();
                updateNavigationViewColors(R.id.navigation_wishlist, R.color.tmdbColor);
            } else if (itemId == R.id.navigation_profile) {
                updateToolbar("PROFILE", R.color.profileColor);
                fragment = new ProfileFragment();
                updateNavigationViewColors(R.id.navigation_profile, R.color.profileColor);
            } else {
                fragment = new MoviesFragment();
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, fragment).commit();
            Log.d(TAG, "Fragment replaced with: " + fragment.getClass().getSimpleName());
            return true;
        }
    };

    private void updateToolbar(String title, int colorResId) {
        toolbarTitle.setText(title);
        toolbarTitle.setTextColor(getResources().getColor(colorResId));
    }

    private void updateNavigationViewColors(int selectedItemId, int selectedItemColor) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        getResources().getColor(selectedItemColor),
                        Color.WHITE
                }
        );

        navigation.setItemIconTintList(colorStateList);
        navigation.setItemTextColor(colorStateList);

        // Update the icon color for the selected item
        navigation.getMenu().findItem(selectedItemId).getIcon().setColorFilter(getResources().getColor(selectedItemColor), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
