package com.example.dell.moviestage2.Utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.dell.moviestage2.MainActivity;
import com.example.dell.moviestage2.Model.Movie;

import java.util.ArrayList;

public class SectionLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private String mUrl;
    private int id;

    public SectionLoader(Context context, String url, int id){
        super(context);
        this.mUrl = url;
        this.id = id;
    }

    protected void onStartLoading(){
        forceLoad();
    }
    @Nullable
    @Override

    public ArrayList<Movie> loadInBackground() {

        try{
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mUrl == null){
            return null;
        }
        ArrayList<Movie> movies = QueryUtils.fetchData(mUrl, id);
        return movies;
    }
}
