package com.example.flixstermoviesapp.models;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel // annotation indicates class is Parcelable
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Integer id;

    // no-arguement, empty constructor required for Parceler
    public Movie() {}

//  Rather than applying a try-catch for each of the method calls, just have the constructor throw an exception if there are any issues
    // Parsing the JSON data being received
    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }


//    To test if placeholders are working, 'screw up' the URL for the images below

    public String getPosterPath() {
//      the poster path URL is just the end bit so we must add the starter URL as well
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
//      when the phone is in landscape mode
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getId() {
        return Integer.toString(id);
    }
}
