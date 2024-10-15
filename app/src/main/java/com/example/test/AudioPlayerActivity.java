package com.example.test;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.extractor.mp4.Track;

import java.util.List;

import okhttp3.OkHttpClient;

public class AudioPlayerActivity extends AppCompatActivity {

    private ExoPlayer player;
    private TrackAdapter trackAdapter;
    private List<Track> trackList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer_activity);

        // Инициализация кнопок
        Button playButton = findViewById(R.id.btn_play);
        Button stopButton = findViewById(R.id.btn_stop);
        Button pauseButton = findViewById(R.id.btn_pause);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();


        });

        // Инициализация плеера
        player = new ExoPlayer.Builder(this).build();

        // Получаем URL стрима
        String trackId = "bVzYJNy";  // Замените на актуальный track_id
        String trackUrl = getTrackStreamUrl(trackId);

        // Создаем MediaItem и добавляем в плеер
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(trackUrl));
        player.setMediaItem(mediaItem);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.prepare(); // Готовим плеер
                player.play();    // Начинаем воспроизведение
            }
        });

        // Обработчик кнопки Stop
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.stop(); // Останавливаем воспроизведение
                }
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause(); // Останавливаем воспроизведение
                }
            }
        });


//        // Подготовка и старт воспроизведения
//        player.prepare();
//        player.play();


    }


    @Override
    protected void onStop() {
        super.onStop();
        // Освобождаем ресурсы плеера
        player.release();
    }

    // Метод для получения стрим-URL
    private String getTrackStreamUrl(String trackId) {
        // Пример URL для API Audius
        return "https://discoveryprovider.audius.co/v1/tracks/" + trackId + "/stream";
    }

}

