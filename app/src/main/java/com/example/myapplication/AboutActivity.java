package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutActivity extends AppCompatActivity {     // hela aktiviteten är en Webview som öppnar den intärna sidan "penguin_about.html"

    WebView penguinAboutWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        penguinAboutWebview = findViewById(R.id.penguin_about_www);
        penguinAboutWebview.setWebViewClient(new WebViewClient());
        penguinAboutWebview.loadUrl("file:///android_asset/penguin_about.html");

    }
}