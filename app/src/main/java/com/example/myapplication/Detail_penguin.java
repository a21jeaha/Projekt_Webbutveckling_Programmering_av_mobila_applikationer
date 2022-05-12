package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Detail_penguin extends AppCompatActivity {

    TextView detailInfoWindow;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deteil_penguin);

        detailInfoWindow = findViewById(R.id.information_window);

        bundle = getIntent().getExtras();
        if (bundle != null){
            String info = bundle.getString("detailInfo");
            detailInfoWindow.setText(info);
        }



    }
}