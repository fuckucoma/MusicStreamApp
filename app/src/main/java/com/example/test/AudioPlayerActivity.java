package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AudioPlayerActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private ViewPager2 viewPager;
    private TrackAdapter trackAdapter;
    private final List<Track> trackList = new ArrayList<>();
    private int playingPosition = -1;

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer_activity);

        viewPager = findViewById(R.id.view_pager);

        // Инициализация ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()== R.id.navigation_home) {
                    startActivity(new Intent(AudioPlayerActivity.this, AudioPlayerActivity.class));
            }
            else if (item.getItemId()== R.id.navigation_search){
                    startActivity(new Intent(AudioPlayerActivity.this, SearchActivity.class));
            }
            else if (item.getItemId()== R.id.navigation_library){
                    startActivity(new Intent(AudioPlayerActivity.this, LibraryActivity.class));
            }
            return true;
        });


        fetchTracks();

        // Добавляем слушатель для смены страницы и воспроизведения трека
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Track selectedTrack = trackList.get(position);
                playTrack(selectedTrack.getTrackId());
            }
        });
    }

    // Получение треков из Audius API
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

                    runOnUiThread(() -> {
                        for (TrackResponse.Data data : trackResponse.getData()) {
                            String artworkUrl = null;
                            if (data.getArtwork() != null) {
                                artworkUrl = data.getArtwork().get480x480();
                            }
                            trackList.add(new Track(data.getTitle(), data.getDescription(), artworkUrl, data.getId()));
                        }

                        trackAdapter = new TrackAdapter(AudioPlayerActivity.this, trackList, track -> {
                            String trackUrl = getTrackStreamUrl(track.getTrackId());
                            playTrack(trackUrl);  // Проигрываем трек
                        }, exoPlayer);
                        viewPager.setAdapter(trackAdapter);
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(AudioPlayerActivity.this, "Ошибка получения треков", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(AudioPlayerActivity.this, "Ошибка сети: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
    protected void onStop() {
        super.onStop();
        exoPlayer.release();
    }

    public int getPlayingPosition() {
        return playingPosition;
    }
}