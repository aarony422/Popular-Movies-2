package me.aaronyoung.popular_movies.data;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Movie;
import android.net.Uri;

import static android.Manifest.permission_group.LOCATION;

/**
 * Created by shangweiyoung on 1/10/17.
 */

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;
    //private static final SQLiteQueryBuilder sMovieByPreferenceQueryBuilder;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int MOVIE_WITH_PREFERENCE_POPULAR = 102;
    static final int MOVIE_WITH_PREFERENCE_TOP_RATED = 103;
    static final int MOVIE_FAVORITE = 104;
    static final int TRAILERS = 200;
    static final int TRAILERS_WITH_ID = 201;
    static final int REVIEWS = 300;
    static final int REVIEWS_WITH_ID = 301;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    /*
    static{
        sMovieByPreferenceQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sMovieByPreferenceQueryBuilder.setTables(
                WeatherContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
                        WeatherContract.LocationEntry.TABLE_NAME +
                        " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
                        "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + WeatherContract.LocationEntry.TABLE_NAME +
                        "." + WeatherContract.LocationEntry._ID);
    }
    */

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/popular", MOVIE_WITH_PREFERENCE_POPULAR);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/top_rated", MOVIE_WITH_PREFERENCE_TOP_RATED);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/favorite", MOVIE_FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, MovieContract.PATH_TRAILERS + "/#", TRAILERS_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS_WITH_ID);

        return matcher;
    }

    //movie.preference = ?
    private static final String sPreferenceSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_PREFERENCE + " = ? ";

    //movie.favorite = ?
    private static final String sFavoriteSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_FAVORITE + " = ? ";

    //movie.movie_id = ?
    private static final String sMovieIDSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    private Cursor getMovieByPreference(
            Uri uri, String[] projection, String sortOrder, int preference) {

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                sPreferenceSelection,
                new String[]{MovieContract.MovieEntry.COLUMN_PREFERENCE, String.valueOf(preference)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieFavorite(
            Uri uri, String[] projection, String sortOrder) {

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                sFavoriteSelection,
                new String[]{MovieContract.MovieEntry.COLUMN_FAVORITE, String.valueOf(MovieContract.MovieEntry.FAVORITE_FAVORED)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movie/popular
            case MOVIE_WITH_PREFERENCE_POPULAR: {
                retCursor = getMovieByPreference(uri, projection, sortOrder, MovieContract.MovieEntry.PREFERENCE_POPULAR);
                break;
            }
            // "movie/top_rated
            case MOVIE_WITH_PREFERENCE_TOP_RATED: {
                retCursor = getMovieByPreference(uri, projection, sortOrder, MovieContract.MovieEntry.PREFERENCE_TOP_RATED);
                break;
            }
            // "movie/favorite"
            case MOVIE_FAVORITE: {
                retCursor = getMovieFavorite(uri, projection, sortOrder);
                break;
            }
            // "movie/[id]
            case MOVIE_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        sMovieIDSelection,
                        new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID, MovieContract.getMovieIDFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "movie"
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "trailers"
            case TRAILERS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "reviews"
            case REVIEWS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "trailers"
            /*
            case FAVORITES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            */

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
}
