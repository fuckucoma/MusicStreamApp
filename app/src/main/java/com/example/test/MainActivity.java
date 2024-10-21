package com.example.test;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.test.fragments.HomeFragment;
import com.example.test.fragments.LibraryFragment;
import com.example.test.fragments.SearchFragment;
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

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    Fragment homeFragment = null;
    Fragment searchFragment = null;
    Fragment libraryFragment = null;

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(MainActivity.this, AudioPlayerActivity.class);
        //startActivity(intent);
        //finish();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

// Переменные для хранения созданных фрагментов

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Используем сохранённые фрагменты для предотвращения их пересоздания
                    if (item.getItemId() == R.id.navigation_home) {

                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                        }
                        selectedFragment = homeFragment;
                    }
                    else if (item.getItemId() == R.id.navigation_search) {
                    if (searchFragment == null) {
                        searchFragment = new SearchFragment();
                    }
                    selectedFragment = searchFragment;
                    }

                    else if (item.getItemId() == R.id.navigation_library) {
                    if (libraryFragment == null) {
                        libraryFragment = new LibraryFragment();
                    }
                    selectedFragment = libraryFragment;
            }

            // Меняем фрагмент, только если он выбран
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });





//        webView = findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true); // Включаем JavaScript
//        webSettings.setDomStorageEnabled(true); // Включаем хранилище DOM
//
//        // Проверка на наличие активных cookies (индикатор того, что пользователь может быть залогинен)
//        CookieManager cookieManager = CookieManager.getInstance();
//        String cookies = cookieManager.getCookie("https://audius.co/signin");
//
//        // Загружаем страницу регистрации Audius
//        webView.loadUrl("https://audius.co/signup");
//
//        // Добавляем JavaScript интерфейс для взаимодействия с WebView
//        webView.addJavascriptInterface(new WebAppInterface(), "Android");
//
//        if (cookies != null && cookies.contains("sdgsdfgsdfg")) {
//            // Пользователь залогинен, переходим к другой активности
//            Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            // Пользователь не залогинен, загружаем страницу входа/регистрации
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//
//                    // Проверка на успешную регистрацию или вход
////                    if (url.contains("success")) {
////                        Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
////                        startActivity(intent);
////                        finish();
////                    }
//                }
//            });
//            webView.loadUrl("https://audius.co/signup");
//        }
//    }
//
//    // Класс интерфейса для взаимодействия с WebView через JavaScript
//    public static class WebAppInterface {
//        @JavascriptInterface
//        public void showToast(String message) {
//            // Можно вызвать из JavaScript
//        }
    }

}
