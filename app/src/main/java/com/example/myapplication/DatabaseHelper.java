package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "penguin_database";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EATS = "eats";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_INFO = "info";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL("CREATE TABLE penguin (" + COLUMN_ID + " TEXT PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_EATS + " TEXT, " + COLUMN_SIZE + " INTEGER, " + COLUMN_IMG + " TEXT, " + COLUMN_INFO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
