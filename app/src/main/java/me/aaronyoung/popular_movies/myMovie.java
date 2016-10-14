package me.aaronyoung.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shangweiyoung on 10/4/16.
 */

public class myMovie implements Parcelable{
    private String posterURL;

    public myMovie(String url) {
        super();
        this.posterURL = url;
    }

    private myMovie(Parcel in) {
        posterURL = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterURL);
    }

    public void setPosterURL(String url) {
        this.posterURL = url;
    }

    public String getPosterURL() {
        return this.posterURL;
    }

    public String toString() {
        return posterURL;
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
