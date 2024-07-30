package com.anuragmaravi.idiotbox.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.SearchResults;
import com.anuragmaravi.idiotbox.network.NetworkUtils;
import com.anuragmaravi.idiotbox.network.VolleySingleton;
import com.anuragmaravi.idiotbox.ui.adapters.search.SearchResultAdapter;
import com.anuragmaravi.idiotbox.model.SearchViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_BASE_URL;
import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_SIZE_M;
import static com.anuragmaravi.idiotbox.common.Contract.API_KEY;


public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private SearchView searchView;
    private SearchManager searchManager;
    private MenuItem menuItem;
    private static final String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView recyclerViewSearchResults;
    private SearchResultAdapter adapterSearchResults;
    private ProgressBar progressBar;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            setContentView(R.layout.activity_search);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

            setupRecyclerView();
            setupProgressBar();
        } else {
            setNoInternetView();
        }
    }

    private void setupRecyclerView() {
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSearchResults.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setNoInternetView() {
        setContentView(R.layout.fragment_no_internet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        findViewById(R.id.buttonTryAgain).setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        menuItem = menu.findItem(R.id.search);

        // Expand the search view and set listeners
        menuItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                onBackPressed();
                return false;
            }
        });

        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            searchView = (SearchView) menuItem.getActionView();
            if (searchView != null) {
                searchView.setQueryHint("Search movies and tv shows");
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(true);
                searchView.setOnQueryTextListener(this);
                searchView.setOnCloseListener(this);
                searchView.requestFocus();
            }
        }
        return true;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        sendRequest(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            if (newText.length() != 0) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerViewSearchResults.setVisibility(View.GONE);
                VolleySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
                sendRequest(newText);
            } else {
                progressBar.setVisibility(View.GONE);
                recyclerViewSearchResults.setVisibility(View.GONE);
            }
        }
        return false;
    }

    private void sendRequest(String query) {
        query = query.replace(" ", "+");
        final List<SearchResults> searchResultsList = new ArrayList<>();
        String urlSearch = "https://api.themoviedb.org/3/search/multi?api_key=" + API_KEY + "&query=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch,
                response -> {
                    Log.i(TAG, "onResponse(Search): " + response);
                    try {
                        JSONObject parentObject = new JSONObject(response);
                        JSONArray parentArray = parentObject.getJSONArray("results");
                        for (int i = 0; i < parentArray.length(); i++) {
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            SearchResults searchResults = new SearchResults();
                            if (finalObject.getString("media_type").equals("person")) {
                                continue;
                            }
                            searchResults.setPosterPath(API_IMAGE_BASE_URL + API_IMAGE_SIZE_M + "/" + finalObject.getString("poster_path"));
                            searchResults.setVoteAverage(finalObject.getString("vote_average"));
                            searchResults.setId(finalObject.getString("id"));
                            searchResults.setMediaType(finalObject.getString("media_type"));
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                            if (finalObject.getString("media_type").equals("movie")) {
                                searchResults.setOriginalTitle(finalObject.getString("original_title"));
                                Date date = format.parse(finalObject.getString("release_date"));
                                String year = (String) DateFormat.format("yyyy", date);
                                searchResults.setReleaseDate(year);
                            }
                            if (finalObject.getString("media_type").equals("tv")) {
                                searchResults.setOriginalTitle(finalObject.getString("original_name"));
                                Date date = format.parse(finalObject.getString("first_air_date"));
                                String year = (String) DateFormat.format("yyyy", date);
                                searchResults.setReleaseDate(year);
                            }
                            searchResultsList.add(searchResults);
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                    adapterSearchResults = new SearchResultAdapter(getApplicationContext(), searchResultsList);
                    recyclerViewSearchResults.setVisibility(View.VISIBLE);
                    recyclerViewSearchResults.setAdapter(adapterSearchResults);
                    progressBar.setVisibility(View.GONE);
                }, error -> Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show());

        stringRequest.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
