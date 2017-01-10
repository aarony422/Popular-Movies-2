package me.aaronyoung.popular_movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shangweiyoung on 12/1/16.
 */

public class MovieContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MovieContract() {}

    // Content Authority for the content provider
    // The package name for the app is guaranteed to be unique on the device
    public static final String CONTENT_AUTHORITY = "me.aaronyoung.popular_movies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI)
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_FAVORITES = "favorites";

    /* inner class that defines the table contents of movie table */
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Table name
        public static final String TABLE_NAME = "movies";

        // movie_id
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // movie title
        public static final String COLUMN_TITLE = "title";

        // release_date
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // vote_avg
        public static final String COLUMN_VOTE_AVG = "vote_avg";

        // overview
        public static final String COLUMN_OVERVIEW = "overview";

        // poster_path
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // popular
        public static final String COLUMN_POPULAR = "popular";

        // top rated
        public static final String COLUMN_TOP_RATED = "top_rated";

        // Uri for movies/[popular | top_rated | favorite ]
        public static Uri buildMovieWithPreferenceUri(String preference) {
            return CONTENT_URI.buildUpon().appendPath(preference).build();
        }

        // Uri for movies/[movie_id]
        public static Uri buildMovieWithId(int movie_id) {
            return ContentUris.withAppendedId(CONTENT_URI, movie_id);
        }

    }

    /* inner class that defines the table contents of Trailer table */
    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        // Table name
        public static final String TABLE_NAME = "trailers";

        // movie_id
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // key
        public static final String COLUMN_KEY = "key";

        // name
        public static final String COLUMN_NAME = "name";

        // site
        public static final String COLUMN_SITE = "site";

        // Uri for trailer/[movie_id]
        public static Uri buildTrailerWithId(int movie_id) {
            return ContentUris.withAppendedId(CONTENT_URI, movie_id);
        }
    }

    /* inner class that defines the table contents of Review table */
    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        // Table name
        public static final String TABLE_NAME = "reviews";

        // movie_id
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // author
        public static final String COLUMN_AUTHOR = "author";

        // content
        public static final String COLUMN_CONTENT = "content";

        // Uri for review/[movie_id]
        public static Uri buildReviewWithId(int movie_id) {
            return ContentUris.withAppendedId(CONTENT_URI, movie_id);
        }
    }

    /* inner class that defines the table contents of Favorites table */
    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        // Table Name
        public static final String TABLE_NAME = "favorites";

        // movie_id
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Uri for favorite/[movie_id]
        public static Uri buildFavoriteWithId(int movie_id) {
            return ContentUris.withAppendedId(CONTENT_URI, movie_id);
        }
    }
}
