package com.anuragmaravi.idiotbox.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.ui.adapters.casting.CastingImagesAdapter;
import com.anuragmaravi.idiotbox.ui.adapters.casting.CastingMoviesAdapter;
import com.anuragmaravi.idiotbox.ui.adapters.casting.CastingTvShowsAdapter;
import com.anuragmaravi.idiotbox.model.Cast;
import com.anuragmaravi.idiotbox.model.Movie;
import com.anuragmaravi.idiotbox.utils.CheckInternet;
import com.anuragmaravi.idiotbox.utils.DateFormatter;
import com.anuragmaravi.idiotbox.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.anuragmaravi.idiotbox.common.Contract.API_CASTING;
import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_BASE_URL;
import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_SIZE_XXL;
import static com.anuragmaravi.idiotbox.common.Contract.API_KEY;
import static com.anuragmaravi.idiotbox.common.Contract.API_URL;
import static com.anuragmaravi.idiotbox.common.Contract.APPEND;
import static com.anuragmaravi.idiotbox.common.Contract.IMAGES;
import static com.anuragmaravi.idiotbox.common.Contract.LANGUAGE;
import static com.anuragmaravi.idiotbox.common.Contract.REGION;
import static com.anuragmaravi.idiotbox.common.Contract.SEPARATOR;

public class CastDetailsActivity extends AppCompatActivity {
    private static String TAG = "CastDetailsActivity";
    private String castId, castDetailsRequest;
    private TextView textViewTitle, textViewOverview, textViewMovieOrTvShow, textViewDirector, textViewCountry;
    private ImageView imageViewFollowImdb, imageViewFollowTwitter, imageViewFollowFacebook, imageViewFollowInstagram, imageViewPoster;
    private LinearLayout linearLayoutTitle;
    private ScrollView container;
    private ProgressBar progressBar;

    // Cast Images
    private RecyclerView recyclerViewImages;
    private List<Movie> castingList = new ArrayList<>();
    private RecyclerView.Adapter adapterCasting;
    private RecyclerView.LayoutManager castingLayoutManager;

    // Cast Movies
    private RecyclerView recyclerViewCastingMovies;
    private List<Cast> castingListMovies = new ArrayList<>();
    private RecyclerView.Adapter adapterCastingMovies;
    private RecyclerView.LayoutManager layoutManagerCastingMovies;

    // Cast Tv Shows
    private RecyclerView recyclerViewCastingTvShows;
    private List<Cast> castingListTvShows = new ArrayList<>();
    private RecyclerView.Adapter adapterCastingTvShows;
    private RecyclerView.LayoutManager layoutManagerCastingTvShows;

    // To show or hide title box
    private boolean isShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String region = prefs.getString("country", "US"); // Default: US
        String language = prefs.getString("language", "en"); // Default: English
        final String posterQuality = prefs.getString("poster_size", "w342/"); // Default: Medium

        if (CheckInternet.getInstance(getApplicationContext()).isNetworkConnected()) {
            setContentView(R.layout.activity_cast_details);
            TAG = getClass().getSimpleName();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            castId = getIntent().getStringExtra("cast_id");

            initializeViews();

            castDetailsRequest = API_URL + API_CASTING + castId + "?api_key=" + API_KEY +
                    LANGUAGE + language +
                    REGION + region +
                    APPEND + IMAGES + SEPARATOR + "external_ids" + SEPARATOR + "movie_credits" + SEPARATOR + "tv_credits";
            Log.i(TAG, "CastURL: " + castDetailsRequest);

            fetchCastDetails(posterQuality);
        } else {
            setNoInternetView();
        }
    }

    private void initializeViews() {
        container = findViewById(R.id.container);
        textViewOverview = findViewById(R.id.textViewOverview);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewMovieOrTvShow = findViewById(R.id.textViewMovieOrTvShow);
        textViewDirector = findViewById(R.id.textViewDirector);
        textViewCountry = findViewById(R.id.textViewCountry);
        imageViewFollowFacebook = findViewById(R.id.imageViewFollowFacebook);
        imageViewFollowTwitter = findViewById(R.id.imageViewFollowTwitter);
        imageViewFollowImdb = findViewById(R.id.imageViewFollowImdb);
        imageViewFollowInstagram = findViewById(R.id.imageViewFollowInstagram);
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
        linearLayoutTitle = findViewById(R.id.linearLayoutTitle);
        imageViewPoster = findViewById(R.id.imageViewPoster);
        imageViewPoster.setOnClickListener(v -> {
            if (isShown) {
                linearLayoutTitle.setVisibility(View.INVISIBLE);
                isShown = false;
            } else {
                linearLayoutTitle.setVisibility(View.VISIBLE);
                isShown = true;
            }
        });
    }

    private void fetchCastDetails(String posterQuality) {
        StringRequest stringRequestCastDetails = new StringRequest(Request.Method.GET, castDetailsRequest,
                response -> {
                    try {
                        parseAndDisplayData(response, posterQuality);
                    } catch (JSONException | ParseException e) {
                        Log.e(TAG, "Error parsing response: ", e);
                        Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Log.e(TAG, "Volley error: ", error);
            Toast.makeText(getApplicationContext(), "Network error occurred", Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestCastDetails);
    }

    private void parseAndDisplayData(String response, String posterQuality) throws JSONException, ParseException {
        JSONObject parentObject = new JSONObject(response);
        Glide.with(getApplicationContext()).load(API_IMAGE_BASE_URL + API_IMAGE_SIZE_XXL + "/" + parentObject.getString("profile_path")).into(imageViewPoster);
        textViewOverview.setText(parentObject.getString("biography"));
        textViewTitle.setText(parentObject.getString("name"));
        textViewCountry.setText("Born: " + parentObject.optString("place_of_birth", "NA"));
        textViewDirector.setText("Birthday: " + DateFormatter.getInstance(getApplicationContext()).formatDate(parentObject.optString("birthday", "NA")));
        textViewMovieOrTvShow.setText(parentObject.getInt("gender") == 1 ? "Female" : "Male");

        handleExternalIds(parentObject.getJSONObject("external_ids"));

        displayImages(parentObject.getJSONObject("images"), posterQuality);
        displayMovies(parentObject.getJSONObject("movie_credits"), posterQuality);
        displayTvShows(parentObject.getJSONObject("tv_credits"), posterQuality);

        container.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void handleExternalIds(JSONObject externalObject) throws JSONException {
        handleExternalId(imageViewFollowInstagram, externalObject, "instagram_id", "http://instagram.com/_u/", "https://www.instagram.com/");
        handleExternalId(imageViewFollowTwitter, externalObject, "twitter_id", "twitter://user?user_id=", "https://twitter.com/");
        handleExternalId(imageViewFollowFacebook, externalObject, "facebook_id", "fb://page/", "https://www.facebook.com/");
        handleExternalId(imageViewFollowImdb, externalObject, "imdb_id", "imdb:///name/", "http://www.imdb.com/name/");
    }

    private void handleExternalId(ImageView imageView, JSONObject externalObject, String key, String appUrl, String webUrl) throws JSONException {
        String id = externalObject.optString(key);
        if (!id.equals("null")) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl + id));
                    intent.setPackage(getPackageForApp(key));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl + id)));
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private String getPackageForApp(String key) {
        switch (key) {
            case "instagram_id":
                return "com.instagram.android";
            case "twitter_id":
                return "com.twitter.android";
            case "facebook_id":
                return "com.facebook.katana";
            case "imdb_id":
                return "com.imdb.mobile";
            default:
                return "";
        }
    }

    private void displayImages(JSONObject imagesObject, String posterQuality) throws JSONException {
        JSONArray imagesArray = imagesObject.getJSONArray("profiles");
        for (int i = 0; i < imagesArray.length(); i++) {
            JSONObject finalObject = imagesArray.getJSONObject(i);
            Movie movieModel = new Movie();
            movieModel.setCastingProfilePath(API_IMAGE_BASE_URL + posterQuality + finalObject.getString("file_path"));
            castingList.add(movieModel);
        }
        castingLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        recyclerViewImages.setLayoutManager(castingLayoutManager);
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
        adapterCasting = new CastingImagesAdapter(getApplicationContext(), castingList);
        recyclerViewImages.setAdapter(adapterCasting);
    }

    private void displayMovies(JSONObject moviesObject, String posterQuality) throws JSONException {
        JSONArray moviesArray = moviesObject.getJSONArray("cast");
        for (int i = moviesArray.length() - 1; i >= 0; i--) {
            JSONObject finalObject = moviesArray.getJSONObject(i);
            Cast cast = new Cast();
            cast.setCastMovieCharacter(finalObject.optString("character", "NA"));
            cast.setCastMovieTitle(finalObject.getString("original_title"));
            cast.setCastMovieId(finalObject.getString("id"));
            cast.setCastMoviePosterPath(API_IMAGE_BASE_URL + posterQuality + finalObject.getString("poster_path"));
            castingListMovies.add(cast);
        }
        layoutManagerCastingMovies = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCastingMovies = findViewById(R.id.recyclerViewCastingMovies);
        recyclerViewCastingMovies.setLayoutManager(layoutManagerCastingMovies);
        recyclerViewCastingMovies.setItemAnimator(new DefaultItemAnimator());
        adapterCastingMovies = new CastingMoviesAdapter(getApplicationContext(), castingListMovies);
        recyclerViewCastingMovies.setAdapter(adapterCastingMovies);
    }

    private void displayTvShows(JSONObject tvShowsObject, String posterQuality) throws JSONException {
        JSONArray tvShowsArray = tvShowsObject.getJSONArray("cast");
        for (int i = tvShowsArray.length() - 1; i >= 0; i--) {
            JSONObject finalObject = tvShowsArray.getJSONObject(i);
            Cast cast = new Cast();
            cast.setCastTvShowCharacter(finalObject.getString("character"));
            cast.setCastTvShowTitle(finalObject.getString("original_name"));
            cast.setCastTvShowId(finalObject.getString("id"));
            cast.setCastTvShowPosterPath(API_IMAGE_BASE_URL + posterQuality + finalObject.getString("poster_path"));
            castingListTvShows.add(cast);
        }
        layoutManagerCastingTvShows = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCastingTvShows = findViewById(R.id.recyclerViewCastingTvShows);
        recyclerViewCastingTvShows.setLayoutManager(layoutManagerCastingTvShows);
        recyclerViewCastingTvShows.setItemAnimator(new DefaultItemAnimator());
        adapterCastingTvShows = new CastingTvShowsAdapter(getApplicationContext(), castingListTvShows);
        recyclerViewCastingTvShows.setAdapter(adapterCastingTvShows);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
