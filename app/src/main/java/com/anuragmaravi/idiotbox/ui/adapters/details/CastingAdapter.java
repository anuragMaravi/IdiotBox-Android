package com.anuragmaravi.idiotbox.ui.adapters.details;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.ui.activities.CastDetailsActivity;
import com.anuragmaravi.idiotbox.model.Movie;

import java.util.List;

public class CastingAdapter extends RecyclerView.Adapter<CastingAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<Movie> movieList;

    public CastingAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_casting, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        Glide.with(mContext).load(movie.getCastingProfilePath()).into(holder.imageViewCasting);
        holder.textViewCastingAs.setText(movie.getCastingCharacter());
        holder.textViewCastingName.setText(movie.getCastingName());
        holder.imageViewCasting.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CastDetailsActivity.class);
            intent.putExtra("cast_id", movie.getCastingId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageViewCasting;
        public final TextView textViewCastingName;
        public final TextView textViewCastingAs;

        public MyViewHolder(View view) {
            super(view);
            imageViewCasting = view.findViewById(R.id.imageViewCasting);
            textViewCastingAs = view.findViewById(R.id.textViewCastingAs);
            textViewCastingName = view.findViewById(R.id.textViewCastingName);
        }
    }
}
