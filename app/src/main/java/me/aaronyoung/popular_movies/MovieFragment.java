package me.aaronyoung.popular_movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
    private ArrayAdapter<myMovie> movieAdapter;
    private ArrayList<myMovie> movieList = new ArrayList<myMovie>();

    private OnFragmentInteractionListener mListener;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movieList")) {
            // if no saved movie data, call updateMovies()
            updateMovies();
        } else {
            movieList = savedInstanceState.getParcelableArrayList("movieList");
        }
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieList", movieList);
        super.onSaveInstanceState(outState);
    }

    public void updateMovies() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        // TODO: Pass in popular or top_rated as user preference
        fetchMovieTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // create MovieAdapter
        movieAdapter = new MovieAdapter(
                getActivity(),
                R.layout.fragment_movie_grid_item,
                R.id.gridview_item_imageview,
                movieList);

        // get fragment_movie view
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        // get reference to gridview and attach MovieAdapter to it
        GridView gridView = (GridView) rootView.findViewById(R.id.movie_gridview);

        // attach MovieAdapter to gridview
        gridView.setAdapter(movieAdapter);

        // return the inflated rootView
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class FetchMovieTask extends AsyncTask<String, Void, ArrayList<myMovie>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected ArrayList<myMovie> doInBackground(String... params) {
            String sortOrder = "top_rated";

            if (params.length != 0) {
                sortOrder = params[0];
            }

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
                // If the code didn't successfully get the weather data, there's no point in attemping
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

                // Set myMovie data
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
            if (result != null) {
                movieList.clear(); // clear movie data
                for(myMovie movie : result) {
                    movieList.add(movie); // update with new data
                }
                // without notifying, adapter doesn't update after initial Fetch of data
                movieAdapter.notifyDataSetChanged();
            }
        }
    }
}
