package com.example.flixstermoviesapp.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixstermoviesapp.MovieDetailsActivity;
import com.example.flixstermoviesapp.R;
import com.example.flixstermoviesapp.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

//  Then you can extend the adapter second, parameterizing it with the appropriate adapter
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @NotNull
    @Override
    // Usually involves inflating a layout from xml and returning the holder
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    // Involves populating data into the item through holder
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindVieHolder" + position);
        // Get the movie at the position
        Movie movie = movies.get(position);
        // Bind the movie data in the holder
        holder.bind(movie);
    }

    @Override
    // Return the total count of items in the list
    public int getItemCount() {
        return movies.size();
    }

    //  Make sure to extend view holder first
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Want to reference each view within item_movie.xml to create each view holder
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // Since we want to be able to alternate between images in portrait and landscape mode
            String imageURL;
            int placeholder;
            int radius;
            int margin;

            // If phone is in landscape mode
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                placeholder = R.drawable.poster_placeholder;
                radius = 140;
                margin = 40;
            }
            // If phone is in portrait mode
            else {
                imageURL = movie.getPosterPath();
                placeholder = R.drawable.backdrop_placeholder;
                radius = 70;
                margin = 15;
            }

            Glide.with(context)
                    .load(imageURL)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .centerCrop() // scale image to fill the entire ImageView
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
        }

        @Override
        // Method from implementing View.OnClickListener to the ViewHolder class
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
