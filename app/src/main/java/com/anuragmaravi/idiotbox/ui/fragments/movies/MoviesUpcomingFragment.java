package com.anuragmaravi.idiotbox.ui.fragments.movies;

import static com.anuragmaravi.idiotbox.common.Contract.MOVIE_UPCOMING_REQUEST;

public class MoviesUpcomingFragment extends BaseMovieFragment {
    @Override
    protected String getApiUrl() {
        return MOVIE_UPCOMING_REQUEST;
    }
}
