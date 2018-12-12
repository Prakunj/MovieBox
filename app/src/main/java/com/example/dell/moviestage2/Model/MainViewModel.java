package com.example.dell.moviestage2.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.dell.moviestage2.Database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = MainViewModel.class.getName();


    private LiveData<List<Movie>> movies;
    private List<Movie> moviesById;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(getApplication());
        Log.e(LOG_TAG, "Retrieving the task from database");
        movies = database.movieDao().loadAllMovies();
    }


    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
