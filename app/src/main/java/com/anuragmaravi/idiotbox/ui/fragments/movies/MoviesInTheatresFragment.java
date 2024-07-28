package com.anuragmaravi.idiotbox.ui.fragments.movies;

import static com.anuragmaravi.idiotbox.common.Contract.MOVIE_NOW_PLAYING_REQUEST;

public class MoviesInTheatresFragment extends BaseMovieFragment {
    @Override
    protected String getApiUrl() {
        return MOVIE_NOW_PLAYING_REQUEST;
    }
}
