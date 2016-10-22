package me.aaronyoung.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shangweiyoung on 10/4/16.
 */

public class myMovie implements Parcelable{
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private String poster_path;
    private String overview;
    private String release_date;
    private String title;
    private double vote_average;

    public myMovie() {
        super();
        poster_path = "";
        overview = "";
        release_date = "";
        title = "";
        vote_average = 0.0;
    }

    // Constructor for testing purposes
    public myMovie(String poster_path) {
        this.poster_path = poster_path;
        overview = "";
        release_date = "";
        title = "";
        vote_average = 0.0;
    }

    private myMovie(Parcel in) {
        IMAGE_BASE_URL = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        title = in.readString();
        vote_average = in.readDouble();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(IMAGE_BASE_URL);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(title);
        parcel.writeDouble(vote_average);
    }

    public void setPosterPath(String url) {
        this.poster_path = url;
    }

    public String getPoster_path() {
        return IMAGE_BASE_URL + this.poster_path;
    }

    public void setOverview(String overview) { this.overview = overview; }

    public String getOverview() { return this.overview; }

    public void setReleaseDate(String date) { this.release_date = date; }

    public String getReleaseDate() { return this.release_date; }

    public String getReleaseYear() {
        return this.release_date.split("-")[0];
    }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return this.title; }

    public void setVoteAvg(double avg) { this.vote_average = avg; }

    public String getVoteAvg() {
        return Double.toString(this.vote_average) + "/10";
    }

    public String toString() {
        return poster_path;
    }

    static final Parcelable.Creator<myMovie> CREATOR = new Parcelable.Creator<myMovie>() {
        @Override
        public myMovie createFromParcel(Parcel parcel) {
            return new myMovie(parcel);
        }

        @Override
        public myMovie[] newArray(int size) {
            return new myMovie[size];
        }
    };
}
