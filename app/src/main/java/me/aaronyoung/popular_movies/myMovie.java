package me.aaronyoung.popular_movies;

/**
 * Created by shangweiyoung on 10/4/16.
 */

public class myMovie {
    private String posterURL;

    public myMovie(String url) {
        super();
        this.posterURL = url;
    }

    public void setPosterURL(String url) {
        this.posterURL = url;
    }

    public String getPosterURL() {
        return this.posterURL;
    }
}
