package com.anuragmaravi.idiotbox.ui.adapters.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.anuragmaravi.idiotbox.R;
//import com.anuragmaravi.idiotbox.ui.details.GenreListActivity;
import com.anuragmaravi.idiotbox.model.Movie;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<Movie> movieList;

    public GenreAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        holder.buttonGenre.setText(movie.getGenreName());
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, GenreListActivity.class);
//            intent.putExtra("genre_id", movie.getGenreId());
//            intent.putExtra("genre_name", movie.getGenreName());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final Button buttonGenre;

        public MyViewHolder(View view) {
            super(view);
            buttonGenre = view.findViewById(R.id.buttonGenre);
        }
    }
}
