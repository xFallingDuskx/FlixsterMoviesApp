package com.example.flixstermoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixstermoviesapp.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.flixstermoviesapp.R.layout.activity_movie_details;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;

    // extra objects
    Context context; //todo
    ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        this.context = getApplicationContext(); //todo
        ivPoster = (ImageView) findViewById(R.id.ivPoster);


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        // Since we want to be able to alternate between images in portrait and landscape mode
        String imageURL;
        int placeholder;

        // If phone is in landscape mode
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
            placeholder = R.drawable.poster_placeholder;
        }
        // If phone is in portrait mode
        else {
            imageURL = movie.getPosterPath();
            placeholder = R.drawable.backdrop_placeholder;
        }

        // todo: issue with lines 81 and 82
        Glide.with(context)
                .load(imageURL)
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop() // scale image to fill the entire ImageView
                .transform(new RoundedCornersTransformation(30, 0))
                .into(ivPoster);
    }
}