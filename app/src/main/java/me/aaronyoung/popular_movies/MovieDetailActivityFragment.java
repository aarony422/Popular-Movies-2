package me.aaronyoung.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    private final String MOVIE_DATA = "movie_data";
    private myMovie movieData;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Retrieve MOVIE_DATA
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MOVIE_DATA)) {
            // Retrieve me.aaronyoung.popular_movies.data
            movieData = intent.getParcelableExtra(MOVIE_DATA);

            // Set Activity title
            ((MovieDetailActivity) getActivity()).setActionBarTitle(movieData.getTitle());

            // Set Movie Title
            ((TextView) rootView.findViewById(R.id.movie_title))
                    .setText(movieData.getTitle());

            // Set Movie poster Image
            Picasso.with(getContext())
                    .load(movieData.getPoster_path())
                    //.resize(6000, 2000)
                    //.centerInside()
                    .placeholder(R.drawable.movie)
                    .into((ImageView) rootView.findViewById(R.id.movie_poster));

            // Set Release Date
            ((TextView) rootView.findViewById(R.id.release_date))
                    .setText(movieData.getReleaseYear());

            // Set Vote Average
            ((TextView) rootView.findViewById(R.id.vote_avg))
                    .setText(movieData.getVoteAvg());

            // Set Movie Overview
            ((TextView) rootView.findViewById(R.id.overview))
                    .setText(movieData.getOverview());
        }
        return rootView;
    }
}
