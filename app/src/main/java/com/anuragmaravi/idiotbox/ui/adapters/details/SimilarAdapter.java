package com.anuragmaravi.idiotbox.ui.adapters.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
//import com.anuragmaravi.idiotbox.ui.details.MovieDetailsActivity;
import com.anuragmaravi.idiotbox.model.Movie;

import java.util.List;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<Movie> movieList;

    public SimilarAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_similar, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        Glide.with(mContext).load(movie.getSimilarPosterPath()).into(holder.imageViewSimilar);
//        holder.imageViewSimilar.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
//            intent.putExtra("movie_id", movie.getSimilarId());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageViewSimilar;

        public MyViewHolder(View view) {
            super(view);
            imageViewSimilar = view.findViewById(R.id.imageViewSimilar);
        }
    }
}
