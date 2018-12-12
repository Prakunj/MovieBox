package com.example.dell.moviestage2;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.moviestage2.Adapter.MovieAdapter;
import com.example.dell.moviestage2.Database.AppDatabase;
import com.example.dell.moviestage2.Model.AddTaskViewModel;
import com.example.dell.moviestage2.Model.AddTaskViewModelFactory;
import com.example.dell.moviestage2.Model.Movie;
import com.example.dell.moviestage2.Utils.SectionLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    private static final String LOG_TAG = MovieDetailActivity.class.getName();

    public static final String URL_SCHEME = "https";
    public static final String BASE_URL = "www.youtube.com";
    public static final String PATH2 = "/watch";

    private AppDatabase database;

    private TextView vote_average_view, release_date_view, synopsis_view;
    private ImageView poster;
    private ImageButton favourite;
    private Context context;
    private String release_date, synopsis, movie_poster;
    private int id;
    private static final int LOADER_ID = 3;
    private static final String PATH = "reviews";
    private AddTaskViewModelFactory factory;
    LinearLayoutManager layoutManager;

    private RecyclerView recyclerView;
    private MovieAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        release_date_view = findViewById(R.id.detailRelease);
        vote_average_view = findViewById(R.id.detailAverage);
        synopsis_view = findViewById(R.id.detailOverview);
        poster = findViewById(R.id.detailImage);
        favourite = findViewById(R.id.imageButton);
        recyclerView = findViewById(R.id.recyclerView);

        context = this;
        layoutManager = new LinearLayoutManager(context);

            Intent intent = getIntent();
            Movie movie = intent.getParcelableExtra("result");

            release_date = movie.getRelease_date();
            release_date_view.setText(release_date);
            movie_poster = movie.getMovie_poster();

            String image_url = "http://image.tmdb.org/t/p/w185/";
            Picasso.with(context).load(image_url + movie.getMovie_poster()).into(poster);

            double vote_average = movie.getVote_average();
            vote_average_view.setText(String.valueOf(vote_average));
            synopsis = movie.getSynopsis();
            synopsis_view.setText(synopsis);

            getSupportActionBar().setTitle(movie.getTitle());

            database = AppDatabase.getsInstance(getApplicationContext());

            factory = new AddTaskViewModelFactory(database, movie.getId());
            AddTaskViewModel model = ViewModelProviders.of(this, factory).get(AddTaskViewModel.class);
            model.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    if (movies.isEmpty()) {
                        favourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {
                        favourite.setImageResource(R.drawable.ic_favorite_black_24dp);

                    }
                }
            });


        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_ID, null, this);
        }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void forTrailer(View view){
        Intent intent =  new Intent(this, TrailerActivity.class);
        Intent getIntent = getIntent();
        Movie movie = getIntent.getParcelableExtra("result");
        intent.putExtra("result", movie);
        startActivity(intent);

    }

    public void favouriteClicked(View view){

            Intent intent = getIntent();
            Movie movie = intent.getParcelableExtra("result");

            factory = new AddTaskViewModelFactory(database, movie.getId());
            final AddTaskViewModel model = ViewModelProviders.of(this, factory).get(AddTaskViewModel.class);

            model.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    if (movies.isEmpty()) {
                        favourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.movieDao().insertMovie(getInformation());
                            }
                        });
                        thread.start();
                        model.getMovies().removeObserver(this);
                    } else {
                        favourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.movieDao().deleteMovie(getInformation());
                            }
                        });
                        thread.start();
                        model.getMovies().removeObserver(this);
                    }
                }
            });
    }

    private String buildUrlWithId(int id) {

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(MainActivity.URL_SCHEME).authority(MainActivity.BASE_URL).path(MainActivity.PATH);
        builder.appendPath(String.valueOf(id));
        builder.appendEncodedPath(PATH);
        builder.appendQueryParameter("api_key", MainActivity.api_key);

        return builder.toString();
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("result");

        String url = "http://image.tmdb.org/t/p/w185/";
        id = movie.getId();

        String baseUrl = buildUrlWithId(id);

        return new SectionLoader(context, baseUrl, LOADER_ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {

        adapter = new MovieAdapter(context, movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {

    }

    private Movie getInformation() {
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("result");
        String title = movie.getTitle();
        release_date = movie.getRelease_date();
        movie_poster = movie.getMovie_poster();
        double vote_average = movie.getVote_average();
        synopsis = movie.getSynopsis();
        id = movie.getId();
        int viewType = Movie.Main_View_Type;

        Movie movies = new Movie(title, release_date, movie_poster, vote_average, synopsis, id, viewType);
        return movies;
    }
}
