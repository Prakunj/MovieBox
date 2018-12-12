package com.example.dell.moviestage2.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dell.moviestage2.Database.AppDatabase;

import java.util.List;

public class AddTaskViewModel extends ViewModel {

    private LiveData<List<Movie>> movies;

    public AddTaskViewModel(AppDatabase database, int id){
        movies = database.movieDao().loadAllMoviesByTitle(id);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
