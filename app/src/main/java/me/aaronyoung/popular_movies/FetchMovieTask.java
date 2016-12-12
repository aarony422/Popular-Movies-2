package me.aaronyoung.popular_movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shangweiyoung on 12/12/16.
 */

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<myMovie>> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private ArrayAdapter<myMovie> movieAdapter;
    private ArrayList<myMovie> movieList;
    public FetchMovieTask(ArrayAdapter<myMovie> movieAdapter, ArrayList<myMovie> movieList) {
        this.movieAdapter = movieAdapter;
        this.movieList = movieList;
    }

    @Override
    protected ArrayList<myMovie> doInBackground(String... params) {
        String sortOrder = params[0];

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the MovieDbApi query

            final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + sortOrder;
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather me.aaronyoung.popular_movies.data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // parse movieJsonStr

        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    private ArrayList<myMovie> getMovieDataFromJson (String movieJsonStr) throws JSONException {
        // Parse JSON string into JSON Object
        JSONObject movieJson = new JSONObject(movieJsonStr);
        // get results JSON Array
        JSONArray results = movieJson.getJSONArray("results");

        // Define JSON Object Key strings
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String TITLE = "original_title";
        final String VOTE_AVG = "vote_average";

        // Create ArrayList of myMovie to be returned
        ArrayList<myMovie> movies = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            // Get the JSONObject at that index in the results array
            JSONObject movieEntry = results.getJSONObject(i);

            // Create new myMovie Object
            myMovie movie = new myMovie();

            // Set myMovie me.aaronyoung.popular_movies.data
            movie.setPosterPath(movieEntry.getString(POSTER_PATH));
            movie.setOverview(movieEntry.getString(OVERVIEW));
            movie.setReleaseDate(movieEntry.getString(RELEASE_DATE));
            movie.setTitle(movieEntry.getString(TITLE));
            movie.setVoteAvg(movieEntry.getDouble(VOTE_AVG));

            // add movie to arraylist
            movies.add(movie);
        }

        // return list of myMovie objects
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<myMovie> result) {
        if (result != null && movieAdapter != null) {
            movieAdapter.clear(); // clear movie me.aaronyoung.popular_movies.data
            for(myMovie movie : result) {
                movieAdapter.add(movie); // update with new me.aaronyoung.popular_movies.data
            }
            movieAdapter.notifyDataSetChanged();
        }
    }
}
