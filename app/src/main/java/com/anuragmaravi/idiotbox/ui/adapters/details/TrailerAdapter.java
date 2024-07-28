package com.anuragmaravi.idiotbox.ui.adapters.details;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
import com.anuragmaravi.idiotbox.model.Movie;

import java.util.List;

import static com.anuragmaravi.idiotbox.common.Contract.YOUTUBE_BASE_THUMBNAIL;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private final Context context;
    private final List<Movie> trailersList;

    public TrailerAdapter(Context context, List<Movie> trailersList) {
        this.context = context;
        this.trailersList = trailersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_videos, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Movie movie = trailersList.get(position);
        String youtubeThumbnail = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("youtube_thumbnail", "/sddefault.jpg");
        String url = YOUTUBE_BASE_THUMBNAIL + movie.getVideoKey() + youtubeThumbnail;
        holder.textViewVideoType.setText(movie.getVideoType());
        holder.textViewTrailerName.setText(movie.getVideoName());
        Glide.with(context).load(url).into(holder.imageViewVideo);
        holder.imageViewVideo.setOnClickListener(v -> watchYoutubeVideo(movie.getVideoKey()));
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageViewVideo;
        public final TextView textViewTrailerName;
        public final TextView textViewVideoType;

        public MyViewHolder(View view) {
            super(view);
            imageViewVideo = view.findViewById(R.id.imageViewVideo);
            textViewTrailerName = view.findViewById(R.id.textViewTrailerName);
            textViewVideoType = view.findViewById(R.id.textViewVideoType);
        }
    }

    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(webIntent);
        }
    }
}

