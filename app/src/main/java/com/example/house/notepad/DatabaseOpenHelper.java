package com.example.house.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by house on 2015/07/04.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notepad.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_SQL =
            "create table notepad (" +
            "_id integer primary key autoincrement not null, " +
            "create_time text, " +
            "update_time text, " +
            "mark int, " +
            "content text" +
            ");";
    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
