package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, AudioPlayerActivity.class);
        startActivity(intent);
        finish();


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
