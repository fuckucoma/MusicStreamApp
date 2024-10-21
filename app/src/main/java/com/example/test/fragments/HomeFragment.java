package com.example.test.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.test.AudioPlayerActivity;
import com.example.test.R;
import com.example.test.Track;
import com.example.test.TrackAdapter;
import com.example.test.TrackResponse;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private ExoPlayer exoPlayer;
    private ViewPager2 viewPager;
    private TrackAdapter trackAdapter;
    private final List<Track> trackList = new ArrayList<>();
    private int playingPosition = -1;

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Инициализируем ExoPlayer
        exoPlayer = new ExoPlayer.Builder(getContext()).build();

        // Инициализируем ViewPager
        viewPager = view.findViewById(R.id.view_pager);

        // Добавляем слушатель для смены страницы и проигрывания трека
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Track selectedTrack = trackList.get(position);
                playTrack(selectedTrack.getTrackId());
            }
        });

        // Получаем треки для отображения в ViewPager
        fetchTracks();

        return view;
    }

    private void fetchTracks() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "https://discoveryprovider.audius.co/v1/tracks/trending?limit=10";
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                Log.d("AudiusApiService", "Response Code: " + response.code());
                Log.d("AudiusApiService", "Response Body: " + responseBody);

                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    TrackResponse trackResponse = gson.fromJson(responseBody, TrackResponse.class);

                    this.getActivity().runOnUiThread(() -> {
                        for (TrackResponse.Data data : trackResponse.getData()) {
                            String artworkUrl = null;
                            if (data.getArtwork() != null) {
                                artworkUrl = data.getArtwork().get480x480();
                            }
                            trackList.add(new Track(data.getTitle(), data.getDescription(), artworkUrl, data.getId()));
                        }

                        trackAdapter = new TrackAdapter(getContext(), trackList, track -> {
                            String trackUrl = getTrackStreamUrl(track.getTrackId());
                            playTrack(trackUrl);  // Проигрываем трек
                        }, exoPlayer);
                        viewPager.setAdapter(trackAdapter);
                    });
                } else {
                    this.getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Ошибка получения треков", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                this.getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Ошибка сети: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    // Проигрывание трека
    private void playTrack(String trackId) {
        String trackUrl = getTrackStreamUrl(trackId);
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(trackUrl)));
        exoPlayer.prepare();
        exoPlayer.play();
    }

    // Получение URL для стриминга трека
    private String getTrackStreamUrl(String trackId) {
        return "https://discoveryprovider.audius.co/v1/tracks/" + trackId + "/stream";
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayer.release();
    }

    public int getPlayingPosition() {
        return playingPosition;
    }


}