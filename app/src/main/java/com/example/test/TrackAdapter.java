package com.example.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private final Context context;
    private final List<Track> trackList;
    private final OnTrackClickListener listener;

    public interface OnTrackClickListener {
        void onTrackClick(Track track);
    }

    public TrackAdapter(Context context, List<Track> trackList, OnTrackClickListener listener) {
        this.context = context;
        this.trackList = trackList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = trackList.get(position);

        holder.trackTitle.setText(track.getTitle());
        holder.trackDescription.setText(track.getDescription());
        //String artworkUrl = track.getArtworkUrl();

        Log.d("PicassoLoader", "Loading artwork from: " + track.getArtworkUrl());
        //Picasso.get().load(track.getArtworkUrl()).into(holder.trackImage);


        // Если artworkUrl равен null, устанавливаем изображение-заглушку
        if (track.getArtworkUrl() != null && !track.getArtworkUrl().isEmpty()) {
            Picasso.get().load(track.getArtworkUrl()).into(holder.trackImage);
        } else {
            holder.trackImage.setImageResource(R.drawable.placeholder_image);  // Заглушка
        }


        // Обработка клика по треку
        holder.itemView.setOnClickListener(v -> listener.onTrackClick(track));
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView trackImage;
        TextView trackTitle;
        TextView trackDescription;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);

            trackImage = itemView.findViewById(R.id.track_image);
            trackTitle = itemView.findViewById(R.id.track_title);
            trackDescription = itemView.findViewById(R.id.track_description);
        }
    }
}
