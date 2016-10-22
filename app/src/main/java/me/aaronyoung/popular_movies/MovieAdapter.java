package me.aaronyoung.popular_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shangweiyoung on 10/4/16.
 */

public class MovieAdapter extends ArrayAdapter<myMovie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context context;
    private int layoutResourceId;
    private int imageViewId;
    private ArrayList<myMovie> movies = null;

    public MovieAdapter(Context context, int layoutResourceId, int imageViewId, ArrayList<myMovie> movies) {
        super(context, layoutResourceId, imageViewId, movies);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.imageViewId = imageViewId;
        this.movies = movies;
    }

    // Used website for reference: http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewEntry = convertView;
        MovieHolder holder = null;

        if (gridViewEntry == null) {
            // Inflate movie_grid_item
            gridViewEntry = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);

            // create holder for gridViewEntry
            holder = new MovieHolder();
            holder.posterImage = (ImageView) gridViewEntry.findViewById(imageViewId);
            gridViewEntry.setTag(holder);
        } else {
            holder = (MovieHolder) gridViewEntry.getTag();
        }

        // Get movie from movies array via position
        myMovie movie = movies.get(position);
        // Update ImageView with movie poster image using Picasso
        Picasso.with(context)
                .load(movie.getPoster_path())
                .placeholder(R.drawable.movie)
                .into(holder.posterImage);

        return gridViewEntry;
    }

    static class MovieHolder {
        ImageView posterImage;
    }

}
