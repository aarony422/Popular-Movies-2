package me.aaronyoung.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private ArrayAdapter<myMovie> movieAdapter;
    private ArrayList<myMovie> movieList = new ArrayList<myMovie>();
    private final String MOVIE_DATA = "movie_data";
    private String LOG_TAG = MovieFragment.class.getSimpleName();

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
            // if no saved movie me.aaronyoung.popular_movies.data, call updateMovies()
            //updateMovies();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // if sort order preference is changed
        if (key.equals(getString(R.string.pref_sortOrder))) {
            updateMovies(); // update Movie
        }
    }

    public void updateMovies() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask(movieAdapter, movieList);

        // Pass in popular or top_rated as user preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // get sortOrder, using toprated as default
        String sortOrder = prefs.getString(getString(R.string.pref_sortOrder),
                getString(R.string.pref_sortOrder_toprated));
        // Call fetchMovieTask with sortOrder as a parameter
        fetchMovieTask.execute(sortOrder);
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

        /*
         * My solution for ensuring that the adapter is initialized before any data is being fetched
         * This will have to change when using content provider
         */

        if (movieList.size() == 0) {
            updateMovies();
        }

        // get fragment_movie view
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        // get reference to gridview and attach MovieAdapter to it
        GridView gridView = (GridView) rootView.findViewById(R.id.movie_gridview);

        // attach MovieAdapter to gridview
        gridView.setAdapter(movieAdapter);

        // Set item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get myMovie object corresponding to clicked position
                myMovie movie = movieAdapter.getItem(position);

                // Create intent to start movie Detail Activity
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra(MOVIE_DATA, movie);

                // Start the Activity with intent
                startActivity(intent);
            }
        });

        // return the inflated rootView
        return rootView;
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

}
