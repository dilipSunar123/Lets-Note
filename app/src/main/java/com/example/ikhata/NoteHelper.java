package com.example.ikhata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "khata.db";
    private static final int DATABASE_VERSION = 1;

    public NoteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " +
                DataCollectionClass.Collection.TABLE_NAME + "(" +
                DataCollectionClass.Collection._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataCollectionClass.Collection.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                DataCollectionClass.Collection.COLUMN_NAME_DESC + " TEXT NOT NULL, " +
                DataCollectionClass.Collection.TIME + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataCollectionClass.Collection.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
