package com.anuragmaravi.idiotbox.ui.adapters.casting;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.ui.activities.MovieDetailsActivity;
import com.anuragmaravi.idiotbox.model.Cast;

import java.util.List;

public class CastingMoviesAdapter extends RecyclerView.Adapter<CastingMoviesAdapter.MyViewHolder> {
    private Context mContext;
    private List<Cast> castMovieList;

    public CastingMoviesAdapter(Context mContext, List<Cast> castMovieList) {
        this.mContext = mContext;
        this.castMovieList = castMovieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_casting_movies, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Cast cast = castMovieList.get(position);
        Glide.with(mContext).load(cast.getCastMoviePosterPath()).into(holder.imageViewCasting);
        holder.textViewCastingAs.setText(cast.getCastMovieCharacter());
        holder.textViewMovieName.setText(cast.getCastMovieTitle());
        holder.textViewYear.setText(cast.getCastMovieYear());
        holder.imageViewCasting.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra("movie_id", cast.getCastMovieId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return castMovieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewCasting;
        public TextView textViewCastingAs, textViewMovieName, textViewYear;

        public MyViewHolder(View view) {
            super(view);
            imageViewCasting = view.findViewById(R.id.imageViewCasting);
            textViewCastingAs = view.findViewById(R.id.textViewCastingAs);
            textViewMovieName = view.findViewById(R.id.textViewMovieName);
            textViewYear = view.findViewById(R.id.textViewYear);
        }
    }
}

