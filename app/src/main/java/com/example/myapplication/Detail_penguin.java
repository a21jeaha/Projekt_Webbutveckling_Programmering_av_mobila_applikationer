package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class Detail_penguin extends AppCompatActivity {

    FloatingActionButton floatingActionButton2;
    TextView detailInfoWindow;
    ImageView penguinImageBig;
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
        penguinImageBig = findViewById(R.id.detail_image);

        detailInfoWindow.setMovementMethod(new ScrollingMovementMethod());

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
            String img = bundle.getString("penguin_image");
            detailInfoWindow.setText(info);
            Picasso.get().load(img).resize(300, 300).into(penguinImageBig);
        }



    }
}