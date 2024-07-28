package com.anuragmaravi.idiotbox.common;
import android.util.Log;

import com.anuragmaravi.idiotbox.BuildConfig;

public class Contract {
    private Contract() {
        // Prevent instantiation
    }

    public static final String API_KEY = BuildConfig.TMDB_API_KEY;
//    public static final String API_KEY = "0744794205a0d39eef72cad8722d4fba";

    // Base Url
    public static final String API_URL = "https://api.themoviedb.org/3/";

    // Query strings
    public static final String API_MOVIE = "movie/";
    public static final String API_TV = "tv/";
    public static final String API_CASTING = "person/";

    // Image Base Url
    public static final String API_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    // Poster Image Sizes
    public static final String API_IMAGE_SIZE_ORIGINAL = "original";
    public static final String API_IMAGE_SIZE_XXXL = "w780";
    public static final String API_IMAGE_SIZE_XXL = "w500";
    public static final String API_IMAGE_SIZE_XL = "w342";
    public static final String API_IMAGE_SIZE_L = "w185";
    public static final String API_IMAGE_SIZE_M = "w154";
    public static final String API_IMAGE_SIZE_S = "w92";

    // Image Url
    public static final String API_IMAGE_URL = API_IMAGE_BASE_URL + API_IMAGE_SIZE_L + "/";

    // Movies
    public static final String API_MOVIE_NOW_PLAYING = "now_playing";
    public static final String API_MOVIE_UPCOMING = "upcoming";
    public static final String API_MOVIE_POPULAR = "popular";
    public static final String API_MOVIE_TOP_RATED = "top_rated";

    // Append to response
    public static final String APPEND = "&append_to_response=";
    public static final String SEPARATOR = ",";

    // Keywords for append to response
    public static final String VIDEOS = "videos";
    public static final String SIMILAR = "similar";
    public static final String CREDITS = "credits";
    public static final String REVIEWS = "reviews";
    public static final String IMAGES = "images";
    public static final String KEYWORDS = "keywords";
    public static final String LISTS = "lists";
    public static final String RELEASE_DATES = "release_dates";
    public static final String TRANSLATIONS = "translations";
    public static final String ALTERNATIVE_TITLES = "alternative_titles";

    // YouTube Url
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    // YouTube Thumbnail
    public static final String YOUTUBE_BASE_THUMBNAIL = "https://img.youtube.com/vi/";

    // YouTube Thumbnail Qualities
    public static final String YOUTUBE_QUALITY_THUMBNAIL_DEFAULT = "/default.jpg";
    public static final String YOUTUBE_QUALITY_THUMBNAIL_MQ = "/mqdefault.jpg";
    public static final String YOUTUBE_QUALITY_THUMBNAIL_SD = "/sddefault.jpg";
    public static final String YOUTUBE_QUALITY_THUMBNAIL_HQ = "/hqdefault.jpg";
    public static final String YOUTUBE_QUALITY_THUMBNAIL_MAX = "/maxresdefault.jpg";

    // Region query
    public static final String REGION = "&region=";

    // Language Query
    public static final String LANGUAGE = "&language=";

    // Adult Content
    public static final String ADULT = "&include_adult=";

    // OMDB Base Url -- append IMDb id
    public static final String OMDB_BASE_URL = "http://www.omdbapi.com/?i=";

    // Final Built Urls for Movies
    public static final String MOVIE_NOW_PLAYING_REQUEST = API_URL + API_MOVIE + API_MOVIE_NOW_PLAYING + "?api_key=" + API_KEY;
    public static final String MOVIE_UPCOMING_REQUEST = API_URL + API_MOVIE + API_MOVIE_UPCOMING + "?api_key=" + API_KEY;
    public static final String MOVIE_POPULAR_REQUEST = API_URL + API_MOVIE + API_MOVIE_POPULAR + "?api_key=" + API_KEY;
    public static final String MOVIE_TOP_RATED_REQUEST = API_URL + API_MOVIE + API_MOVIE_TOP_RATED + "?api_key=" + API_KEY;

    // Final Built Urls for TvShows
    public static final String TV_POPULAR_REQUEST = API_URL + API_TV + API_MOVIE_POPULAR + "?api_key=" + API_KEY;
    public static final String TV_ON_THE_AIR_REQUEST = API_URL + API_TV + "on_the_air?api_key=" + API_KEY;
    public static final String TV_AIRING_TODAY_REQUEST = API_URL + API_TV + "airing_today?api_key=" + API_KEY;
    public static final String TV_TOP_RATED_REQUEST = API_URL + API_TV + API_MOVIE_TOP_RATED + "?api_key=" + API_KEY;

    // Request Token
    public static final String REQUEST_TOKEN = API_URL + "authentication/token/new?api_key=" + API_KEY;

    // Account
    public static final String ACCOUNT_DETAILS = API_URL + "account?api_key=" + API_KEY;
}
