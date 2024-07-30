package com.anuragmaravi.idiotbox.ui.adapters.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anuragmaravi.idiotbox.ui.activities.MovieDetailsActivity;
import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.SearchResults;
//import com.anuragmaravi.idiotbox.ui.moviedetails.MovieDetailsActivity;
//import com.anuragmaravi.idiotbox.ui.tvshowdetails.TvShowDetailsActivity;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<SearchResults> searchResultsList;

    public SearchResultAdapter(Context mContext, List<SearchResults> searchResultsList) {
        this.mContext = mContext;
        this.searchResultsList = searchResultsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_results, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SearchResults result = searchResultsList.get(position);
        Glide.with(mContext).load(result.getPosterPath()).into(holder.imageViewSearchPoster);
        holder.textViewName.setText(result.getOriginalTitle());
        holder.textViewDate.setText(result.getReleaseDate());
        holder.textViewType.setText(result.getMediaType());
        holder.textViewVoteAverage.setText(result.getVoteAverage());

        if (result.getMediaType().equals("movie")) {
            holder.textViewType.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else if (result.getMediaType().equals("tv")) {
            holder.textViewType.setTextColor(mContext.getResources().getColor(R.color.tv_show_accent));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = null;
            if (result.getMediaType().equals("movie")) {
                intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra("movie_id", result.getId());
            } 
//            else {
//                intent = new Intent(mContext, TvShowDetailsActivity.class);
//                intent.putExtra("tvshow_id", result.getId());
//            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return searchResultsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewSearchPoster;
        TextView textViewName, textViewDate, textViewType, textViewVoteAverage;

        MyViewHolder(View view) {
            super(view);
            imageViewSearchPoster = view.findViewById(R.id.imageViewSearchPoster);
            textViewName = view.findViewById(R.id.textViewName);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewType = view.findViewById(R.id.textViewType);
            textViewVoteAverage = view.findViewById(R.id.textViewVoteAverage);
        }
    }
}
