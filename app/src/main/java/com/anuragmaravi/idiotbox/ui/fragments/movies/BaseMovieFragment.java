package com.anuragmaravi.idiotbox.ui.fragments.movies;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.Movie;
import com.anuragmaravi.idiotbox.network.VolleySingleton;
import com.anuragmaravi.idiotbox.ui.adapters.movies.MoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.anuragmaravi.idiotbox.common.Contract.API_IMAGE_BASE_URL;
import static com.anuragmaravi.idiotbox.common.Contract.LANGUAGE;
import static com.anuragmaravi.idiotbox.common.Contract.REGION;

public abstract class BaseMovieFragment extends Fragment {
    private static final String TAG = "BaseMovieFragment";
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private View rootView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_popular, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

        movieList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recyclerViewMoviesPopular);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchMovies();

        return rootView;
    }

    private void fetchMovies() {
        // Get the region and language from the settings
        String locale = getResources().getConfiguration().locale.getCountry();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String region = prefs.getString("country", locale);
        String language = prefs.getString("language", "en");
        final String posterQuality = prefs.getString("poster_size", "w342/");

        // Build the API URL
        String url = getApiUrl() + LANGUAGE + language + REGION + region;
        Log.d(TAG, "Request URL: " + url);

        // Make the network request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.d(TAG, "Response: " + response);
                        JSONObject parentObject = new JSONObject(response);
                        JSONArray parentArray = parentObject.getJSONArray("results");
                        for (int i = 0; i < parentArray.length(); i++) {
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(finalObject.getString("id"));
                            movie.setPosterPath(API_IMAGE_BASE_URL + posterQuality + finalObject.getString("poster_path"));
                            movieList.add(movie);
                        }
                        adapter = new MoviesAdapter(getActivity(), movieList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: ", e);
                        Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: ", error);
                    Toast.makeText(getActivity(), "Network error occurred", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    protected abstract String getApiUrl();

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
