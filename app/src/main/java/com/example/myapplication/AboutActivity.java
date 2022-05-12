package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity {

    WebView penguinAboutWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        penguinAboutWebview = findViewById(R.id.penguin_about_www);
        penguinAboutWebview.loadUrl("file:///asset/about.html");

    }
}