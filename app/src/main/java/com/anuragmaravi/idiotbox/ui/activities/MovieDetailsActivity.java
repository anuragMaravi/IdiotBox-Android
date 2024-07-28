package com.anuragmaravi.idiotbox.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anuragmaravi.idiotbox.utils.DateFormatter;
import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.ui.adapters.details.CastingAdapter;
import com.anuragmaravi.idiotbox.ui.adapters.details.GenreAdapter;
import com.anuragmaravi.idiotbox.ui.adapters.details.SimilarAdapter;
import com.anuragmaravi.idiotbox.ui.adapters.details.TrailerAdapter;
import com.anuragmaravi.idiotbox.model.Movie;
import com.anuragmaravi.idiotbox.utils.CheckInternet;
import com.anuragmaravi.idiotbox.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_BASE_URL;
import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_SIZE_XXL;
import static com.anuragmaravi.idiotbox.common.Contract.API_KEY;
import static com.anuragmaravi.idiotbox.common.Contract.API_MOVIE;
import static com.anuragmaravi.idiotbox.common.Contract.API_URL;
import static com.anuragmaravi.idiotbox.common.Contract.APPEND;
import static com.anuragmaravi.idiotbox.common.Contract.CREDITS;
import static com.anuragmaravi.idiotbox.common.Contract.LANGUAGE;
import static com.anuragmaravi.idiotbox.common.Contract.REGION;
import static com.anuragmaravi.idiotbox.common.Contract.SEPARATOR;
import static com.anuragmaravi.idiotbox.common.Contract.SIMILAR;
import static com.anuragmaravi.idiotbox.common.Contract.VIDEOS;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";
    private static final String PREFS_NAME = "LOGIN";

    private String movieId;
    private String movieDetailsRequest;
    private TextView textViewDirector, textViewTitle, textViewVoteAverage, textViewReleaseDateRuntime, textViewOverview, textViewMovieOrTvShow, textViewYear, textViewMovieTagline, textViewCountry;
    private ImageView imageViewPoster;
    private LinearLayout linearLayoutTitle;
    private ScrollView container;
    private ProgressBar progressBar;

    // Similar Movies
    private RecyclerView recyclerViewSimilar;
    private List<Movie> similarMovieList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager similarLayoutManager;

    // Trailers
    private List<Movie> trailersList = new ArrayList<>();
    private RecyclerView recyclerViewTrailers;
    private RecyclerView.Adapter adapterTrailers;
    private RecyclerView.LayoutManager layoutManagerTrailers;

    // Casting
    private List<Movie> castingList = new ArrayList<>();
    private RecyclerView recyclerViewCasting;
    private RecyclerView.Adapter adapterCasting;
    private RecyclerView.LayoutManager layoutManagerCasting;

    // Genres
    private List<Movie> genreList = new ArrayList<>();
    private RecyclerView recyclerViewGenre;
    private RecyclerView.Adapter adapterGenre;
    private RecyclerView.LayoutManager layoutManagerGenre;

    // To show or hide title box
    private boolean isShown = true;

    private SharedPreferences prefs;
    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String region = prefs.getString("country", "US"); // Default: US
        String language = prefs.getString("language", "en"); // Default: English

        if (CheckInternet.getInstance(getApplicationContext()).isNetworkConnected()) {
            setContentView(R.layout.activity_movie_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            movieId = getIntent().getStringExtra("movie_id");

            // Views Initialisation
            container = findViewById(R.id.container);
            textViewOverview = findViewById(R.id.textViewOverview);
            textViewTitle = findViewById(R.id.textViewTitle);
            textViewMovieOrTvShow = findViewById(R.id.textViewMovieOrTvShow);
            textViewYear = findViewById(R.id.textViewYear);
            textViewReleaseDateRuntime = findViewById(R.id.textViewReleaseDateRuntime);
            textViewDirector = findViewById(R.id.textViewDirector);
            textViewCountry = findViewById(R.id.textViewCountry);
            textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
            textViewMovieTagline = findViewById(R.id.textViewMovieTagline);
            linearLayoutTitle = findViewById(R.id.linearLayoutTitle);
            imageViewPoster = findViewById(R.id.imageViewPoster);

            progressBar = findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

            // Toggles movie poster completely visible and half hidden
            imageViewPoster.setOnClickListener(v -> {
                if (isShown) {
                    linearLayoutTitle.setVisibility(View.INVISIBLE);
                    isShown = false;
                } else {
                    linearLayoutTitle.setVisibility(View.VISIBLE);
                    isShown = true;
                }
            });

            // Movie Details with videos, images, credits, similar
            movieDetailsRequest = API_URL + API_MOVIE + movieId + "?api_key=" + API_KEY +
                    LANGUAGE + language +
                    REGION + region +
                    APPEND + VIDEOS + SEPARATOR + CREDITS + SEPARATOR + SIMILAR;
            Log.i(TAG, "New Request: " + movieDetailsRequest);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, movieDetailsRequest, null,
                    this::parseAndDisplayData, error -> {
                Log.e(TAG, "Volley error: ", error);
                Toast.makeText(getApplicationContext(), "Some Error Occurred.", Toast.LENGTH_SHORT).show();
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        } else {
            setNoInternetView();
        }
    }

    private void parseAndDisplayData(JSONObject parentObject) {
        final String posterQuality = prefs.getString("poster_size", "w342/"); // Default: Medium

        try {
            Glide.with(getApplicationContext()).load(API_IMAGE_BASE_URL + API_IMAGE_SIZE_XXL + "/" + parentObject.getString("poster_path")).into(imageViewPoster);
            textViewOverview.setText(parentObject.getString("overview"));
            textViewTitle.setText(parentObject.getString("original_title"));
            textViewMovieTagline.setText(parentObject.getString("tagline"));
            textViewMovieOrTvShow.setText("Movie");
            textViewReleaseDateRuntime.setText("• " + parentObject.getString("runtime") + " min");
            textViewCountry.setText("• Status: " + parentObject.getString("status"));
            textViewVoteAverage.setText(parentObject.getString("vote_average"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(parentObject.getString("release_date"));
            String year = (String) DateFormat.format("yyyy", date);
            textViewYear.setText(year);
            textViewDirector.setText("• " + DateFormatter.getInstance(getApplicationContext()).formatDate(parentObject.getString("release_date")));
        } catch (JSONException | ParseException e) {
            Log.e(TAG, "Parsing error: ", e);
        }

        // Genres
        try {
            JSONArray genreArray = parentObject.getJSONArray("genres");
            for (int i = 0; i < genreArray.length(); i++) {
                JSONObject finalObject = genreArray.getJSONObject(i);
                Movie movieModel = new Movie();
                movieModel.setGenreId(finalObject.getString("id"));
                movieModel.setGenreName(finalObject.getString("name"));
                genreList.add(movieModel);
            }
            layoutManagerGenre = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewGenre = findViewById(R.id.recyclerViewGenre);
            recyclerViewGenre.setLayoutManager(layoutManagerGenre);
            recyclerViewGenre.setItemAnimator(new DefaultItemAnimator());
            adapterGenre = new GenreAdapter(getApplicationContext(), genreList);
            recyclerViewGenre.setAdapter(adapterGenre);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing genres error: ", e);
        }

        // Trailers
        try {
            JSONObject videoObject = parentObject.getJSONObject("videos");
            JSONArray videoArray = videoObject.getJSONArray("results");
            for (int i = 0; i < videoArray.length(); i++) {
                JSONObject finalObject = videoArray.getJSONObject(i);
                Movie movieModel = new Movie();
                movieModel.setVideoKey(finalObject.getString("key"));
                movieModel.setVideoName(finalObject.getString("name"));
                movieModel.setVideoType(finalObject.getString("type"));
                trailersList.add(movieModel);
            }
            layoutManagerTrailers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
            recyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
            recyclerViewTrailers.setItemAnimator(new DefaultItemAnimator());
            adapterTrailers = new TrailerAdapter(getApplicationContext(), trailersList);
            recyclerViewTrailers.setAdapter(adapterTrailers);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing trailers error: ", e);
        }

        // Similar Movies
        try {
            JSONObject similarObject = parentObject.getJSONObject("similar");
            JSONArray similarArray = similarObject.getJSONArray("results");
            for (int i = 0; i < similarArray.length(); i++) {
                JSONObject finalObject = similarArray.getJSONObject(i);
                Movie movieModel = new Movie();
                movieModel.setSimilarId(finalObject.getString("id"));
                movieModel.setSimilarPosterPath(API_IMAGE_BASE_URL + posterQuality + finalObject.getString("poster_path"));
                similarMovieList.add(movieModel);
            }
            similarLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilar = findViewById(R.id.recyclerViewSimilar);
            recyclerViewSimilar.setLayoutManager(similarLayoutManager);
            recyclerViewSimilar.setItemAnimator(new DefaultItemAnimator());
            adapter = new SimilarAdapter(getApplicationContext(), similarMovieList);
            recyclerViewSimilar.setAdapter(adapter);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing similar movies error: ", e);
        }

        // Credits or casting
        try {
            JSONObject castObject = parentObject.getJSONObject("credits");
            JSONArray castArray = castObject.getJSONArray("cast");
            for (int i = 0; i < castArray.length(); i++) {
                JSONObject finalObject = castArray.getJSONObject(i);
                Movie movieModel = new Movie();
                movieModel.setCastingId(finalObject.getString("id"));
                movieModel.setCastingCharacter(finalObject.getString("character"));
                movieModel.setCastingName(finalObject.getString("name"));
                movieModel.setCastingProfilePath(API_IMAGE_BASE_URL + posterQuality + finalObject.getString("profile_path"));
                castingList.add(movieModel);
            }
            layoutManagerCasting = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewCasting = findViewById(R.id.recyclerViewCasting);
            recyclerViewCasting.setLayoutManager(layoutManagerCasting);
            recyclerViewCasting.setItemAnimator(new DefaultItemAnimator());
            adapterCasting = new CastingAdapter(getApplicationContext(), castingList);
            recyclerViewCasting.setAdapter(adapterCasting);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing casting error: ", e);
        }

        // Make the container visible and hide the progressbar
        container.setVisibility(View.VISIBLE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_favorite, menu);
        return true;
    }

    private boolean movieSelected = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_favorite) {
            handleFavoriteAction(item);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void handleFavoriteAction(MenuItem item) {
        if (!movieSelected) {
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
            item.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            item.setTitle("Unfavorite");
            Toast.makeText(this, "marked", Toast.LENGTH_SHORT).show();
            movieSelected = true;
            updateFavoriteStatus(false);
        } else {
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
            item.setTitle("Favorite");
            Toast.makeText(this, "unmarked", Toast.LENGTH_SHORT).show();
            movieSelected = false;
            updateFavoriteStatus(true);
        }
    }

    private void updateFavoriteStatus(boolean isFavorite) {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String accountId = sharedPreferences.getString("ACCOUNT_ID", null);
        String sessionId = sharedPreferences.getString("SESSION_ID", null);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("media_type", "movie");
            jsonBody.put("media_id", movieId);
            jsonBody.put("favorite", isFavorite);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "https://api.themoviedb.org/3/account/" + accountId + "/favorite?api_key=" + API_KEY + "&session_id=" + sessionId,
                    jsonBody,
                    response -> Log.i("Volley", "onResponse(Mark Favorite): " + response),
                    error -> Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show());
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error: ", e);
        }
    }
}
