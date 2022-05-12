package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Detail_penguin extends AppCompatActivity {

    FloatingActionButton floatingActionButton2;
    TextView detailInfoWindow;
    Bundle bundle;
    Intent aboutIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deteil_penguin);

        // Denna intent agerar mot AboutActivity
        aboutIntent = new Intent(Detail_penguin.this, AboutActivity.class);

        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        detailInfoWindow = findViewById(R.id.information_window);

        // öppnas aktiviteten AboutActivity
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(aboutIntent);
            }
        });

        // hämtar data via intent, för en specifik nyckel.
        bundle = getIntent().getExtras();

        if (bundle != null){
            String info = bundle.getString("detailInfo");
            detailInfoWindow.setText(info);
        }



    }
}