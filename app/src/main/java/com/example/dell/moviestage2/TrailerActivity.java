package com.example.dell.moviestage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;

import com.example.dell.moviestage2.Adapter.MovieAdapter;
import com.example.dell.moviestage2.Model.Movie;
import com.example.dell.moviestage2.Utils.SectionLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    int id;
    Context context;
    private static final int LOADER_ID = 2;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private static final String PATH = "videos";
    LinearLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        context = this;
        layoutManager = new LinearLayoutManager(context);

        recyclerView = findViewById(R.id.recyclerView);

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_ID, null, this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("result");

        id = movie.getId();

        String baseUrl = buildUrlWithId(id);

        return new SectionLoader(context, baseUrl, LOADER_ID);

    }

    private String buildUrlWithId(int id) {

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(MainActivity.URL_SCHEME).authority(MainActivity.BASE_URL).path(MainActivity.PATH);
        builder.appendPath(String.valueOf(id));
        builder.appendEncodedPath(PATH);
        builder.appendQueryParameter("api_key", MainActivity.api_key);

        return builder.toString();
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

}
