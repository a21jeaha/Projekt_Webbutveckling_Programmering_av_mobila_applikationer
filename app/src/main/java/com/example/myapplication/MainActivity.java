package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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

    // används som konstanter för databashanteringen och preferences
    private final String over = "over";
    private final String under = "under";
    private final String _default = "default";
    private final String fetch = "fetch";

    // knappar
    private FloatingActionButton floatingActionButton1;
    private Button filterOver70;
    private Button filterUnder70;
    private Button filterDefault;

    // recycler view relaterat
    private Gson gson;
    private ArrayList<Penguin> penguins;
    private RecyclerView recyclerView;
    private PenguinRecyclerAdapter penguinRecyclerAdapter;


    //intents, databas och preference
    private Intent intentDetail;
    private Intent intentAbout;
    private SharedPreferences penguinListPreference;
    private SharedPreferences.Editor penguinListPreferenceEditor;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // databasen som kommer innehålla penguin informationen
        databaseHelper = new DatabaseHelper(this);

        // initieringen av prefereneces
        penguinListPreference = getPreferences(MODE_PRIVATE);
        penguinListPreferenceEditor = penguinListPreference.edit();

        penguins = new ArrayList<>();
        gson = new Gson();

        // intents mellan MainActivity, och de andra relaterade aktiviteterna
        intentDetail = new Intent(MainActivity.this, Detail_penguin.class );
        intentAbout = new Intent(MainActivity.this, AboutActivity.class);


        // knapp - widget association
        filterOver70 = findViewById(R.id.over_70);
        filterUnder70 = findViewById(R.id.under_70);
        filterDefault = findViewById(R.id.filter_default);
        floatingActionButton1 = findViewById(R.id.floatingActionButton);


        // lyssnare för sorterings knapparna, skickar med argument för filtrering av datanbas
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

        // öppnar ny aktivitet
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAbout);
            }
        });

        // recyclerView adapter, med clicklyssnare som reagerar på vart i listan man klickar
        // vid klick öppnas en ny aktivitet, data skickas med via intent.
        penguinRecyclerAdapter = new PenguinRecyclerAdapter(this, penguins, new PenguinRecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(Penguin penguin) {
                intentDetail.putExtra("detailInfo", penguin.getAuxdata().getInfo());
                intentDetail.putExtra("detail_name", penguin.getName());
                intentDetail.putExtra("penguin_image", penguin.getAuxdata().getImg());
                startActivity(intentDetail);
            }
        });

        // skickas till en metod som sätter upp recyclerView adaptern
        setAdapter();

        // Json strängen hämtas från URL:en i variabeln, fortsätter sedan arbetet i metoden `onPostExecute`
        new JsonTask(this).execute(JSON_URL);
    }

    // initierar penguinRecyclerAdaptern
    private void setAdapter(){
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(penguinRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    // Unmarshall Json strängen med hjälp av GSON
    @Override
    public void onPostExecute(String json) {

        // möjlig data försöks att hämtas från databasen, för att se ifall den är tom.
        try {
               fetchDB(fetch);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // om databasen var tom startas arbetet med att hämta JSON strängen med den data som behövs.
        if (penguins.isEmpty()) {

            //Unmarshall
            Type type = new TypeToken<ArrayList<Penguin>>() {}.getType();
            penguins = gson.fromJson(json, type);

            penguinRecyclerAdapter.setPenguins(penguins);
            penguinRecyclerAdapter.notifyDataSetChanged();

            // Den hämtade datan sätts in i databasen, via metoden `insertToDB()`
            insertToDB();

        }else   // Fanns det data i databasen kontrolles det ifall det finns ett sparat preference. (en variabel som används för att identifiera vilken databas sökning som ska utföras)
                {fetchDB(penguinListPreference.getString("chosen_filter", _default));}
    }

    // värdena i Penguin objekten som finns i ArrayListen fylls in i databasen.
    private void insertToDB(){

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

    //Utför sökningar i databasen.
    private void fetchDB (String filterOrGetDB){

        // tömmer innehållet i den nuvarande ArrayListen, detta så att det inte förekommer dubbla instanser i recycleViewn.
        penguins.clear();

        Cursor cursor = null;

        ArrayList<Penguin> tempPenguinList = new ArrayList<>();

        // hämtar ALL data i databasen
        if (filterOrGetDB.equals(fetch) || filterOrGetDB.equals(_default)) {

            cursor = databaseHelper.getReadableDatabase().rawQuery
                    (" SELECT * FROM " + DatabaseHelper.TABLE_PENGUIN + " INNER JOIN " + DatabaseHelper.TABLE_AUXDATA +
                                    " WHERE " + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.COLUMN_ID_2,
                            null, null);

        }

        // Sorterings sökningar--------------------------------

        // hämtar data där vikten är högre än 70kg
        else if (filterOrGetDB.equals(over)){

            cursor = databaseHelper.getReadableDatabase().rawQuery
                    (" SELECT * FROM " + DatabaseHelper.TABLE_PENGUIN + " INNER JOIN " + DatabaseHelper.TABLE_AUXDATA + " WHERE " + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.COLUMN_ID_2 +
                            " AND " + DatabaseHelper.COLUMN_SIZE + ">" + 70, null, null);

        }
        // hämtar data där vikten är under 70
        else if (filterOrGetDB.equals(under)){

            cursor = databaseHelper.getReadableDatabase().rawQuery
                    (" SELECT * FROM " + DatabaseHelper.TABLE_PENGUIN + " INNER JOIN " + DatabaseHelper.TABLE_AUXDATA + " WHERE " + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.COLUMN_ID_2 +
                            " AND " + DatabaseHelper.COLUMN_SIZE + "<" + 70, null, null);

        }
        //-----------------------------------------------------

        // innehållet i databasen skickas till objekt, dessa fyller sedan an ArrayList
        // då classen ´Penguin´ innehåller en annan klass som även den får data, måste den skapas också

        while (cursor.moveToNext()){

            // en instanse av auxdata skapas, med informationen från databasen
            Auxdata auxdata = new Auxdata(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID_2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INFO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMG))
            );

            // en instanse av Penguin skapas, med informationen från databasen och den nyligen skapade objektet auxdata
            Penguin penguin = new Penguin(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EATS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SIZE)),
                    auxdata

            );
            // objekten stoppas in i denna temporära lista
            tempPenguinList.add(penguin);

        }
        cursor.close();

        // hela innehållet skickas över till ArrayListen `penguins`
        penguins.addAll(tempPenguinList);
        penguinRecyclerAdapter.notifyDataSetChanged();

        // ett preference sparas OM sökningen var gjord för sortering
        if (filterOrGetDB != fetch) {
            penguinListPreferenceEditor.putString("chosen_filter", filterOrGetDB);
            penguinListPreferenceEditor.commit();
        }
    }
}