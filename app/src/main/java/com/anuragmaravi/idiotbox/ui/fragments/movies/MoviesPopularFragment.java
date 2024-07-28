package com.anuragmaravi.idiotbox.ui.fragments.movies;

import static com.anuragmaravi.idiotbox.common.Contract.MOVIE_POPULAR_REQUEST;

public class MoviesPopularFragment extends BaseMovieFragment {
    @Override
    protected String getApiUrl() {
        return MOVIE_POPULAR_REQUEST;
    }
}
