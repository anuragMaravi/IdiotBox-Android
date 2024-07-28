package com.anuragmaravi.idiotbox.ui.adapters.movies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anuragmaravi.idiotbox.ui.activities.MovieDetailsActivity;
import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
//import com.anuragmaravi.idiotbox.ui.details.MovieDetailsActivity;
import com.anuragmaravi.idiotbox.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<Movie> movieList;

    public MoviesAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        Glide.with(mContext).load(movie.getPosterPath()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra("movie_id", movie.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
}
