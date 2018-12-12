package com.example.dell.moviestage2.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.dell.moviestage2.Model.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movie ORDER BY Release_date")
   LiveData<List<Movie>> loadAllMovies();

    @Insert
    void insertMovie(Movie movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movieEntry);

    @Delete
    void deleteMovie(Movie movieEntry);

    @Query("SELECT * FROM Movie WHERE id = :id")
    LiveData<List<Movie>> loadAllMoviesByTitle(int id);
}
