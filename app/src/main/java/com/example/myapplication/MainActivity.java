package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements JsonTask.JsonTaskListener{

    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=a21jeaha";

    FloatingActionButton floatingActionButton1;


    private RecyclerView recyclerView;
    private PenguinRecyclerAdapter penguinRecyclerAdapter;

    private ArrayList<Penguin> penguins;

    private Gson gson;
    private Intent intentDetail;
    private Intent intentAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        intentDetail = new Intent(MainActivity.this, Detail_penguin.class );
        intentAbout = new Intent(MainActivity.this, AboutActivity.class);

        penguins = new ArrayList<>();

        floatingActionButton1 = findViewById(R.id.floatingActionButton);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAbout);
            }
        });


        penguinRecyclerAdapter = new PenguinRecyclerAdapter(this, penguins, new PenguinRecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(Penguin penguin) {
                intentDetail.putExtra("detailInfo", penguin.getAuxdata().getInfo());
                intentDetail.putExtra("penguin_image", penguin.getAuxdata().getImg());
                startActivity(intentDetail);
            }
        });

       setAdapter();

        new JsonTask(this).execute(JSON_URL);
    }

    private void setAdapter(){                                                    // initierar penguinRecyclerAdaptern
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(penguinRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onPostExecute(String json) {                            // Unmarshall Json strängen med hjälp av GSON

        Type type = new TypeToken<ArrayList<Penguin>>() {}.getType();
        penguins = gson.fromJson( json, type);

        penguinRecyclerAdapter.setPenguins(penguins);
        penguinRecyclerAdapter.notifyDataSetChanged();

    }
}