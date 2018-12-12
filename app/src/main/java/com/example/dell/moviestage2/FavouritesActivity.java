package com.example.dell.moviestage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dell.moviestage2.Adapter.MovieAdapter;
import com.example.dell.moviestage2.Database.AppDatabase;
import com.example.dell.moviestage2.Model.MainViewModel;
import com.example.dell.moviestage2.Model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    private static final String LOG_TAG = FavouritesActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> arrayList;
    private Context context;
    private AppDatabase database;
    private TextView emptyStateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        context = this;
        emptyStateView = findViewById(R.id.empty_view);

        database = AppDatabase.getsInstance(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        arrayList =  new ArrayList<>();
        adapter = new MovieAdapter(this, (ArrayList<Movie>) arrayList);
        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            setupViewModel();

        }else{

            emptyStateView.setText(R.string.no_internet);

        }


    }

    private void setupViewModel() {
        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
        model.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.e(LOG_TAG, "updating");
                adapter.setMovie(movies);
            }
        });
    }


}
