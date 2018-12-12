package com.example.dell.moviestage2.Utils;

import android.text.TextUtils;

import com.example.dell.moviestage2.MainActivity;
import com.example.dell.moviestage2.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int DETAIL_ID = 2;
    private static final int REVIEW_ID = 3;

    private QueryUtils() {
    }

    public static ArrayList<Movie> fetchData(String requestUrl, int id) {
        ArrayList<Movie> movies = null;
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
        }
        switch (id){
            case MainActivity.SECTION_LOADER_ID:
                movies = extractDataFromStreamOne(jsonResponse);
                break;
            case DETAIL_ID:
                movies = extractDataFromStreamTwo(jsonResponse);
                break;
            case REVIEW_ID:
                movies = extractDaataFromStreamThree(jsonResponse);

        }
        return movies;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException m) {
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url == null) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Movie> extractDataFromStreamOne(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        try {
            JSONObject baseObject = new JSONObject(jsonResponse);
            JSONArray baseArray = baseObject.getJSONArray("results");

            for (int i = 0; i < baseArray.length(); i++) {
                JSONObject object = baseArray.getJSONObject(i);
                String title = object.getString("title");
                String poster = object.getString("poster_path");
                double vote_average = object.getDouble("vote_average");
                int id = object.getInt("id");

                String synopsis = object.getString("overview");
                String release_date = object.getString("release_date");

                Movie movie = new Movie(title, release_date, poster, vote_average, synopsis, id, Movie.Main_View_Type);
                movieArrayList.add(movie);
            }
        } catch (JSONException e) {

        }
        return movieArrayList;
    }
    private static ArrayList<Movie> extractDataFromStreamTwo(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        try {
            JSONObject baseObject = new JSONObject(jsonResponse);
            JSONArray baseArray = baseObject.getJSONArray("results");

            for (int i = 0; i < baseArray.length(); i++){
                JSONObject object = baseArray.getJSONObject(i);
                String name = object.getString("name");
                String key = object.getString("key");

                Movie movie = new Movie(name, key, Movie.Trailer_View_Type);
                movieArrayList.add(movie);

            }

        } catch (JSONException e){

        }return movieArrayList;

    }
    private static ArrayList<Movie> extractDaataFromStreamThree(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        try {
            JSONObject baseObject = new JSONObject(jsonResponse);
            JSONArray baseArray = baseObject.getJSONArray("results");

            for (int i = 0; i < baseArray.length(); i++){
                JSONObject object = baseArray.getJSONObject(i);

                String author = object.getString("author");
                String content = object.getString("content");

                Movie movie = new Movie(author, content, Movie.Review_View_Type);
                movieArrayList.add(movie);

            }

        } catch (JSONException e){

        }return movieArrayList;

    }
}
