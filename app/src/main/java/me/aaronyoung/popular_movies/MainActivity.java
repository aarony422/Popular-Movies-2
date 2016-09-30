package me.aaronyoung.popular_movies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add movie fragment to main activity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new MovieFragment())
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
