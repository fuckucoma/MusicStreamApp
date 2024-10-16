package com.example.test;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AudioPlayerActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private final List<Track> trackList = new ArrayList<>();
    private int playingPosition = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer_activity);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();

        // Получаем треки из API
        fetchTracks();

        // Добавляем обработку скроллинга для остановки проигрывания треков, выходящих из видимости
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                // Проверяем, если трек больше не виден, останавливаем его проигрывание
                if (playingPosition != -1 && (playingPosition < firstVisibleItemPosition || playingPosition > lastVisibleItemPosition)) {
                    exoPlayer.stop();
                    playingPosition = -1;
                    trackAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Получение треков из Audius API
    private void fetchTracks() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "https://discoveryprovider.audius.co/v1/tracks/trending?limit=10";  // API Audius
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                Log.d("AudiusApiService", "Response Code: " + response.code());
                Log.d("AudiusApiService", "Response Body: " + responseBody);

                if (response.isSuccessful()) {
                    // Парсим JSON-ответ и обновляем RecyclerView
                    Gson gson = new Gson();
                    TrackResponse trackResponse = gson.fromJson(responseBody, TrackResponse.class);

                    runOnUiThread(() -> {
                        for (TrackResponse.Data data : trackResponse.getData()) {
                            // Проверяем наличие artwork и его URL
                            String artworkUrl = null;
                            if (data.getArtwork() != null) {
                                artworkUrl = data.getArtwork().get480x480();  // Получаем URL, если он доступен
                            }

                            // Добавляем трек в список с проверенным значением artworkUrl
                            trackList.add(new Track(data.getTitle(), data.getDescription(), artworkUrl, data.getId()));
                        }

                        // Инициализация адаптера
                        trackAdapter = new TrackAdapter(AudioPlayerActivity.this, trackList, track -> {
                            // Обработка клика по треку
                            String trackUrl = getTrackStreamUrl(track.getTrackId());
                            playTrack(trackUrl, trackList.indexOf(track));  // Проигрываем трек
                        });
                        recyclerView.setAdapter(trackAdapter);
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
    private void playTrack(String trackUrl, int position) {
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(trackUrl)));
        exoPlayer.prepare();
        exoPlayer.play();
        playingPosition = position;
        trackAdapter.notifyDataSetChanged();
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