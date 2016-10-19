package me.aaronyoung.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            movieData = intent.getParcelableExtra(MOVIE_DATA);
            ((TextView) rootView.findViewById(R.id.movie_detail_textview))
                    .setText(movieData.getTitle());
        }
        return rootView;
    }
}
