package com.anuragmaravi.idiotbox.ui.adapters.casting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.Movie;

import java.util.List;

public class CastingImagesAdapter extends RecyclerView.Adapter<CastingImagesAdapter.MyViewHolder> {
    private Context context;
    private List<Movie> movieList;

    public CastingImagesAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_casting, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        Glide.with(context).load(movie.getCastingProfilePath()).into(holder.imageViewCasting);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewCasting;

        public MyViewHolder(View view) {
            super(view);
            imageViewCasting = view.findViewById(R.id.imageViewCasting);
        }
    }
}
