package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database";
    private static final int DATABASE_VERSION = 1;

    // Namn på Tabellerna
    public static final String TABLE_PENGUIN = "penguin";
    public static final String TABLE_AUXDATA = "auxdata";


    // Tabellen penguin
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EATS = "eats";
    public static final String COLUMN_SIZE = "size";


    //tabellen auxdata
    public static final String COLUMN_ID_2 = "_id2";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_INFO = "info";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PENGUIN + " " + "(" +
                                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                                    COLUMN_NAME + " TEXT, " +
                                    COLUMN_EATS + " TEXT, " +
                                    COLUMN_SIZE + " INTEGER )"
            );

            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_AUXDATA + " (" +
                                    COLUMN_ID_2 + " TEXT PRIMARY KEY, " +
                                    COLUMN_INFO + " TEXT, " +
                                    COLUMN_IMG + " TEXT)"
            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
