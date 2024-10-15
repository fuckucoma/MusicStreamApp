package com.example.test;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

    private ExoPlayer player;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private final List<Track> trackList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer_activity);

        // Инициализация кнопок
        Button playButton = findViewById(R.id.btn_play);
        Button stopButton = findViewById(R.id.btn_stop);
        Button pauseButton = findViewById(R.id.btn_pause);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация плеера
        player = new ExoPlayer.Builder(this).build();

        // Запуск потока для получения треков из Audius API
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            String url = "https://discoveryprovider.audius.co/v1/tracks/trending?limit=10";  // API Audius для популярных треков
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                Log.d("AudiusApiService", "Response Code: " + response.code());
                Log.d("AudiusApiService", "Response Body: " + responseBody);

                if (response.isSuccessful()) {
                    // Парсим JSON-ответ и обновляем RecyclerView
                    Gson gson = new Gson();
                    TrackResponse trackResponse = gson.fromJson(responseBody, TrackResponse.class);

                    // Заполняем список треков
                    runOnUiThread(() -> {
                        for (TrackResponse.Data data : trackResponse.getData()) {

                            String artworkUrl = null;
                            if (data.getArtwork() != null && data.getArtwork().get480x480() != null) {
                                artworkUrl = data.getArtwork().get480x480();  // Получаем URL, если доступен
                            }
                            // Логируем URL обложки
                            Log.d("TrackArtwork", "Artwork URL: " + artworkUrl);



                            trackList.add(new Track(data.getTitle(), data.getDescription(), data.getArtwork().get480x480(), data.getId()));
                        }
                        // Устанавливаем адаптер
                        trackAdapter = new TrackAdapter(AudioPlayerActivity.this, trackList, track -> {
                            // Обработка клика на элемент трека
                            String trackUrl = getTrackStreamUrl(track.getTrackId());
                            playTrack(trackUrl); // Проигрываем трек
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
    private void playTrack(String trackUrl) {
        player.setMediaItem(MediaItem.fromUri(Uri.parse(trackUrl)));
        player.prepare();
        player.play();
    }

    // Метод для получения стрим-URL трека
    private String getTrackStreamUrl(String trackId) {
        return "https://discoveryprovider.audius.co/v1/tracks/" + trackId + "/stream";
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }
}
