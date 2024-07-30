package com.anuragmaravi.idiotbox.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.SearchResults;
import com.anuragmaravi.idiotbox.utils.CheckInternet;
import com.anuragmaravi.idiotbox.network.VolleySingleton;
import com.anuragmaravi.idiotbox.ui.adapters.GenreListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_BASE_URL;
import static com.anuragmaravi.idiotbox.common.Contract.API_KEY;
import static com.anuragmaravi.idiotbox.common.Contract.API_URL;

public class GenreListActivity extends AppCompatActivity {
    private static final String TAG = GenreListActivity.class.getSimpleName();
    private String genreId;
    private SharedPreferences prefs;
    private String genreListRequest;

    private TextView textViewGenreTitle;
    private RecyclerView recyclerViewGenreList;
    private List<SearchResults> genreListMovieList = new ArrayList<>();
    private RecyclerView.Adapter genreListAdapter;
    private RecyclerView.LayoutManager genreListLayoutManager;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CheckInternet.getInstance(getApplicationContext()).isNetworkConnected()) {
            setContentView(R.layout.activity_genre_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            genreId = getIntent().getStringExtra("genre_id");
            textViewGenreTitle = findViewById(R.id.textViewGenreTitle);
            textViewGenreTitle.setText(getIntent().getStringExtra("genre_name"));

            progressBar = findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String region = prefs.getString("country", "US");
            String language = prefs.getString("language", "en");

            genreListRequest = API_URL + "genre/" + genreId + "/movies?api_key=" + API_KEY +
                    "&language=" + language + "&region=" + region;

            Log.i(TAG, "New Request: " + genreListRequest);
            StringRequest stringRequestTmdb = new StringRequest(Request.Method.GET, genreListRequest,
                    response -> {
                        Log.i(TAG, "onResponse(TMDb): " + response);
                        try {
                            parseAndDisplayData(response);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }, error -> Toast.makeText(getApplicationContext(), "Some Error Occurred.", Toast.LENGTH_SHORT).show());

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestTmdb);

        } else {
            setNoInternetView();
        }
    }

    private void parseAndDisplayData(String response) throws JSONException, ParseException {
        final String poster_quality = prefs.getString("poster_size", "w342/");
        JSONObject parentObject = new JSONObject(response);
        JSONArray parentArray = parentObject.getJSONArray("results");
        for (int i = 0; i < parentArray.length(); i++) {
            JSONObject finalObject = parentArray.getJSONObject(i);
            SearchResults searchResults = new SearchResults();
            searchResults.setOriginalTitle(finalObject.getString("original_title"));
            searchResults.setPosterPath(API_IMAGE_BASE_URL + poster_quality + finalObject.getString("poster_path"));
            searchResults.setVoteAverage(finalObject.getString("vote_average"));
            searchResults.setId(finalObject.getString("id"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(finalObject.getString("release_date"));
            String year = (String) DateFormat.format("yyyy", date);
            searchResults.setReleaseDate(year);
            genreListMovieList.add(searchResults);
        }

        genreListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewGenreList = findViewById(R.id.recyclerViewGenreList);
        recyclerViewGenreList.setLayoutManager(genreListLayoutManager);
        recyclerViewGenreList.setItemAnimator(new DefaultItemAnimator());

        genreListAdapter = new GenreListAdapter(getApplicationContext(), genreListMovieList);
        recyclerViewGenreList.setVisibility(View.VISIBLE);
        recyclerViewGenreList.setAdapter(genreListAdapter);
        progressBar.setVisibility(View.GONE);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
