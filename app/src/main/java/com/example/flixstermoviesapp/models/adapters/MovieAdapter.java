package com.example.flixstermoviesapp.models.adapters;

import android.content.Context;
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
import com.example.flixstermoviesapp.R;
import com.example.flixstermoviesapp.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Want to reference each view within item_movie.xml to create each view holder
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // Since we want to be able to alternate between images in portrait and landscape mode
            String imageURL;
            int placeholder; //todo: is this correct?

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

            Glide.with(context)
                    .load(imageURL)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(ivPoster);
        }
    }
}
