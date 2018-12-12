package com.example.dell.moviestage2.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "Movie")
public class Movie implements Parcelable  {


    private String Title;


    private String Release_date;
    private String Movie_poster;
    private double Vote_average;
    private String synopsis;
    @PrimaryKey

    private int id;
    private int viewType;

    @Ignore
    public static final int Main_View_Type = 0;
    @Ignore
    public static final int Trailer_View_Type = 1;
    @Ignore
    public static final int Review_View_Type = 2;



    @Ignore
    private String name;
    @Ignore
    private String key;

    public void setMovie_poster(String movie_poster) {
        Movie_poster = movie_poster;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setRelease_date(String release_date) {
        Release_date = release_date;
    }

    public void setVote_average(double vote_average) {
        Vote_average = vote_average;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Ignore
    public void setName(String name) {
        this.name = name;
    }
    @Ignore
    public void setKey(String key) {
        this.key = key;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
    @Ignore
    public String getName() {

        return name;
    }


    public int getViewType() {
        return viewType;
    }

    public int getId() {
        return id;
    }

    public String getTrailerName() {
        return name;
    }
    @Ignore
    public String getKey() {
        return key;
    }

    public String getTitle() {
        return Title;
    }

    public  String  getMovie_poster() {
        return Movie_poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    @Ignore
    public Movie(String trailerName, String key, int viewType){
        this.name = trailerName;
        this.key = key;
        this.viewType = viewType;

    }

    public Movie(String Title, String Release_date, String Movie_poster, double Vote_average, String synopsis, int id, int viewType) {
        this.Title = Title;
        this.Release_date = Release_date;
        this.Movie_poster = Movie_poster;
        this.Vote_average = Vote_average;
        this.synopsis = synopsis;
        this.id = id;
        this.viewType = viewType;
    }

    public double getVote_average() {
        return Vote_average;
    }

    public String  getRelease_date() {
        return Release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Movie_poster);
        dest.writeString(this.Title);
        dest.writeString(this.Release_date);
        dest.writeDouble(this.Vote_average);
        dest.writeString(this.synopsis);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeInt(this.viewType);
    }

    protected Movie(Parcel in) {
        this.Movie_poster = in.readString();
        this.Title = in.readString();
        this.Release_date = in.readString();
        this.Vote_average = in.readDouble();
        this.synopsis = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.key = in.readString();
        this.viewType = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
