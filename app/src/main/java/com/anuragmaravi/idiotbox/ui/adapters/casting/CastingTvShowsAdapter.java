package com.anuragmaravi.idiotbox.ui.adapters.casting;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.anuragmaravi.idiotbox.R;
//import com.anuragmaravi.idiotbox.ui.details.TvShowDetailsActivity;
import com.anuragmaravi.idiotbox.model.Cast;

import java.util.List;

public class CastingTvShowsAdapter extends RecyclerView.Adapter<CastingTvShowsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Cast> castTvShowList;

    public CastingTvShowsAdapter(Context mContext, List<Cast> castTvShowList) {
        this.mContext = mContext;
        this.castTvShowList = castTvShowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_casting_tvshows, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Cast cast = castTvShowList.get(position);
        Glide.with(mContext).load(cast.getCastTvShowPosterPath()).into(holder.imageViewCasting);
        holder.textViewCastingAs.setText(cast.getCastTvShowCharacter());
//        holder.textViewTvShowName.setText(cast.getCastTvShowTitle());
//        holder.imageViewCasting.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, TvShowDetailsActivity.class);
//            intent.putExtra("tvshow_id", cast.getCastTvShowId());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return castTvShowList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewCasting;
        public TextView textViewCastingAs, textViewTvShowName;

        public MyViewHolder(View view) {
            super(view);
            imageViewCasting = view.findViewById(R.id.imageViewCasting);
            textViewCastingAs = view.findViewById(R.id.textViewCastingAs);
//            textViewTvShowName = view.findViewById(R.id.textViewTvShowName);
        }
    }
}

