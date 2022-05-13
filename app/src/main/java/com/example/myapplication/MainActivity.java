package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements JsonTask.JsonTaskListener{

    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=a21jeaha";

    private FloatingActionButton floatingActionButton1;
    private Button sortByName;
    private Button sortByHeigt;
    private Button sortByDefault;


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

        sortByName = findViewById(R.id.by_name);
        sortByHeigt = findViewById(R.id.by_height);
        sortByDefault = findViewById(R.id.by_default);
        floatingActionButton1 = findViewById(R.id.floatingActionButton);



        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy(sortByName);
            }
        });
        sortByHeigt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy(sortByHeigt);
            }
        });
        sortByDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy(sortByDefault);
            }
        });
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
                intentDetail.putExtra("detail_name", penguin.getName());
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
    private void sortBy(Button pressedButton){         /// fill out with funktionality

    }
}