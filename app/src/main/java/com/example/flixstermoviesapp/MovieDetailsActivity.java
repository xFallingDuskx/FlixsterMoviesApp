package com.example.flixstermoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixstermoviesapp.databinding.ActivityMainBinding;
import com.example.flixstermoviesapp.databinding.ActivityMovieDetailsBinding;
import com.example.flixstermoviesapp.models.Movie;
import com.example.flixstermoviesapp.models.MovieTrailerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

import static com.example.flixstermoviesapp.R.layout.activity_movie_details;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;
    public static final String TAG = "MovieDetailsActivity";


    // the view objects
//    TextView tvTitle;
//    TextView tvOverview;
//    RatingBar rbVoteAverage;

    // extra objects
//    Context context; //todo
//    ImageView ivPoster;
    ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // resolve the view objects
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvOverview = (TextView) findViewById(R.id.tvOverview);
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
//        this.context = getApplicationContext(); //todo
//        ivPoster = (ImageView) findViewById(R.id.ivPoster);


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage / 2.0f);

        // Since we want to be able to alternate between images in portrait and landscape mode
        String imageURL;
        int placeholder;

        // If phone is in landscape mode
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
            placeholder = R.drawable.poster_placeholder;
        }
        // If phone is in portrait mode
        else {
            imageURL = movie.getPosterPath();
            placeholder = R.drawable.backdrop_placeholder;
        }

        // For ivPoster in top left corner
        Glide.with(this)
                .load(imageURL)
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop() // scale image to fill the entire ImageView
                .transform(new RoundedCornersTransformation(30, 0))
                .into(binding.ivPoster);

        // For ivVideo at the bottom
        Glide.with(this)
                .load(movie.getBackdropPath())
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop() // scale image to fill the entire ImageView
                .transform(new RoundedCornersTransformation(30, 0))
                .into(binding.ivVideo);

        // bind view to ImageView ivVideo in xml document activity_movie_details
        binding.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New client created for received movie data about YouTube videos
                AsyncHttpClient videoClient = new AsyncHttpClient();
                String YOUTUBE_TRAILER_URL = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
                Log.i(TAG, "Movie ID:" + movie.getId());

//          Second parameter in line below is a HANDLER, specified to handle the JSON data being received
                videoClient.get(YOUTUBE_TRAILER_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        Log.d(TAG, json.toString());

                        JSONObject jsonObject = json.jsonObject;

//                  Do a try-catch because there's a chance that 'results' may note exist
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            Log.i(TAG, "Results: " + results.toString());

                            String key = results.getJSONObject(0).getString("key");

                            // Intent needed to go to another activity -- to send one data to another
                            // 1st parameter is context (where we are at) and 1st is where we are going
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);

                            // .putExtra() used to send info from one activity to another
                            // 1st parameter is what we are calling the data, and the 2nd is what data is being sent
                            intent.putExtra("key", key);

                            // Starting intent
                            startActivity(intent);

                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");

                    }
                });
            }
        });


    }

   /* @Override
    protected void onResume() {
        super.onResume();
        binding.getRoot().invalidate();
    }*/
}