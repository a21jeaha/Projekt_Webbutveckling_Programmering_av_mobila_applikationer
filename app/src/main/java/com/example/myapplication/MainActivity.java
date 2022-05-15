package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private final String over = "over";
    private final String under = "under";
    private final String _default = "default";
    private final String fetch = "fetch";

    private FloatingActionButton floatingActionButton1;
    private Button filterOver70;
    private Button filterUnder70;
    private Button filterDefault;


    private RecyclerView recyclerView;
    private PenguinRecyclerAdapter penguinRecyclerAdapter;

    private ArrayList<Penguin> penguins;

    private Gson gson;
    private Intent intentDetail;
    private Intent intentAbout;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();


        gson = new Gson();
        intentDetail = new Intent(MainActivity.this, Detail_penguin.class );
        intentAbout = new Intent(MainActivity.this, AboutActivity.class);

        penguins = new ArrayList<>();

        filterOver70 = findViewById(R.id.over_70);
        filterUnder70 = findViewById(R.id.under_70);
        filterDefault = findViewById(R.id.filter_default);
        floatingActionButton1 = findViewById(R.id.floatingActionButton);



        filterOver70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDB(over);
            }
        });
        filterUnder70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDB(under);
            }
        });
        filterDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDB(_default);
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


        //        databaseHelper.getWritableDatabase().execSQL(" DELETE FROM " + DatabaseHelper.TABLE_PENGUIN);        // TA BORT NÄR DU ÄR KLAR MED DEM.
        //        databaseHelper.getWritableDatabase().execSQL(" DELETE FROM " + DatabaseHelper.TABLE_AUXDATA);


        try {
               fetchDB(fetch);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        if (penguins.isEmpty()) {       /// FINISH THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!
        Type type = new TypeToken<ArrayList<Penguin>>() {
        }.getType();
        penguins = gson.fromJson(json, type);

        penguinRecyclerAdapter.setPenguins(penguins);
        penguinRecyclerAdapter.notifyDataSetChanged();

        for (int i = 0; i < penguins.size(); i++) {

            Penguin penguin = penguins.get(i);

            ContentValues values = new ContentValues();
            ContentValues values2 = new ContentValues();

            values.put(DatabaseHelper.COLUMN_ID, penguin.get_id());
            values.put(DatabaseHelper.COLUMN_NAME, penguin.getName());
            values.put(DatabaseHelper.COLUMN_EATS, penguin.getEats());
            values.put(DatabaseHelper.COLUMN_SIZE, penguin.getSize());

            values2.put(DatabaseHelper.COLUMN_ID_2, penguin.get_id());
            values2.put(DatabaseHelper.COLUMN_IMG, penguin.getAuxdata().getImg());
            values2.put(DatabaseHelper.COLUMN_INFO, penguin.getAuxdata().getInfo());

            databaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_PENGUIN, null, values);
            databaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_AUXDATA, null, values2);
        }
    }
    else { Log.d (":D", ":D"); }


    }


    private void fetchDB (String sortOrGetDB){

        Cursor cursor = null;
        ArrayList<Penguin> tempPenguinList = new ArrayList<>();

        if (sortOrGetDB.equals(fetch) || sortOrGetDB.equals(_default)) {

            cursor = databaseHelper.getReadableDatabase().rawQuery
                    (" SELECT * FROM " + DatabaseHelper.TABLE_PENGUIN + " INNER JOIN " + DatabaseHelper.TABLE_AUXDATA +
                                    " WHERE " + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.COLUMN_ID_2,
                            null, null);

        }
        else if (sortOrGetDB.equals(over)){

            cursor = databaseHelper.getReadableDatabase().rawQuery
                    (" SELECT * FROM " + DatabaseHelper.TABLE_PENGUIN + " INNER JOIN " + DatabaseHelper.TABLE_AUXDATA + " WHERE " + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.COLUMN_ID_2 +
                            " AND " + DatabaseHelper.COLUMN_SIZE + ">" + 70, null, null);

        }
        else if (sortOrGetDB.equals(under)){

            cursor = databaseHelper.getReadableDatabase().rawQuery
                    (" SELECT * FROM " + DatabaseHelper.TABLE_PENGUIN + " INNER JOIN " + DatabaseHelper.TABLE_AUXDATA + " WHERE " + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.COLUMN_ID_2 +
                            " AND " + DatabaseHelper.COLUMN_SIZE + "<" + 70, null, null);

        }

        while (cursor.moveToNext()){

            Auxdata auxdata = new Auxdata(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID_2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INFO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMG))
            );

            Penguin penguin = new Penguin(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EATS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SIZE)),
                    auxdata

            );
            tempPenguinList.add(penguin);

        }
        cursor.close();

        penguins.addAll(tempPenguinList);
        penguinRecyclerAdapter.notifyDataSetChanged();
    }
}