package com.example.dell.moviestage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.moviestage2.Adapter.MovieAdapter;
import com.example.dell.moviestage2.Model.Movie;
import com.example.dell.moviestage2.Utils.SectionLoader;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getName();


    public static final String URL_SCHEME = "https";

    public static final String BASE_URL = "api.tmdb.org";
    public static final String PATH = "/3/movie/";

    public static String api_key = "";


    private String sortBy;

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private Context context;
    private Toolbar toolbar;
    final String POSITION_STATE = "position-state";
    private TextView emptyViewState;
    public static final int SECTION_LOADER_ID = 1;
    View loadingIndicator;

    private GridLayoutManager layoutManager;
    private Parcelable superState;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            Log.e(LOG_TAG, "restored");
            superState = savedInstanceState.getParcelable(POSITION_STATE);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        superState = layoutManager.onSaveInstanceState();
        outState.putParcelable(POSITION_STATE, superState);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(superState != null ){
            Log.e(LOG_TAG, "postResume");
            layoutManager.onRestoreInstanceState(superState);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        recyclerView = findViewById(R.id.recyclerView);

        toolbar = findViewById(R.id.toolbarView);
        loadingIndicator = findViewById(R.id.loading_indicator);
        emptyViewState = findViewById(R.id.empty_view);

        setSupportActionBar(toolbar);

        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && superState == null) {

            LoaderManager loaderManager = LoaderManager.getInstance(this);
            Log.e(LOG_TAG, "loader1");
            loaderManager.initLoader(SECTION_LOADER_ID, null, this);
        }else{
            loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyViewState.setText(R.string.no_internet);

        }


    }

    private String setUpSharePreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        sortBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.highest_rated_value));

        return sortBy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {

        setUpSharePreference();

        String url = buildUrl();

        return new SectionLoader(context, url, SECTION_LOADER_ID);
    }

    private String buildUrl() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(URL_SCHEME).authority(BASE_URL).path(PATH);
        builder.appendPath(sortBy);
        builder.appendQueryParameter("api_key", api_key);

        return builder.toString();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> movieArrayList) {

        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        adapter = new MovieAdapter(context, movieArrayList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.favourites){
            Intent intent = new Intent(context, FavouritesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_order_by_key))) {
            setUpSharePreference();
            getSupportLoaderManager().restartLoader(SECTION_LOADER_ID, null, this);
            Log.e(LOG_TAG, "loader2");
        }
    }

}
