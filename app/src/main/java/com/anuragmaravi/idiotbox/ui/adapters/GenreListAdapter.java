package com.anuragmaravi.idiotbox.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.SearchResults;
import com.anuragmaravi.idiotbox.ui.activities.MovieDetailsActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchResults> movieList;

    public GenreListAdapter(Context mContext, List<SearchResults> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SearchResults movie = movieList.get(position);
        Glide.with(mContext).load(movie.getPosterPath()).into(holder.imageViewSearchPoster);
        holder.textViewName.setText(movie.getOriginalTitle());
        holder.textViewDate.setText(movie.getReleaseDate());
        holder.textViewVoteAverage.setText(movie.getVoteAverage());
        holder.itemView.setOnClickListener(v -> {
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
        public ImageView imageViewSearchPoster;
        public TextView textViewName, textViewDate, textViewVoteAverage;

        public MyViewHolder(View view) {
            super(view);
            imageViewSearchPoster = view.findViewById(R.id.imageViewSearchPoster);
            textViewName = view.findViewById(R.id.textViewName);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewVoteAverage = view.findViewById(R.id.textViewVoteAverage);
        }
    }
}
