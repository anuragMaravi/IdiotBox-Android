package com.anuragmaravi.idiotbox.ui.fragments.movies;

import static com.anuragmaravi.idiotbox.common.Contract.MOVIE_TOP_RATED_REQUEST;

public class MoviesTopRatedFragment extends BaseMovieFragment {
    @Override
    protected String getApiUrl() {
        return MOVIE_TOP_RATED_REQUEST;
    }
}
