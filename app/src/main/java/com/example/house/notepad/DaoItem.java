package com.example.house.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by house on 2015/07/05.
 */
public class DaoItem {
    public static String TABLE_NAME = "notepad";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_CREATE_TIME = "create_time";
    public static String COLUMN_UPDATE_TIME = "update_time";
    public static String COLUMN_MARK = "mark";
    public static String COLUMN_CONTENT = "content";
    public static String create() {
        String sql =
                "create table " + TABLE_NAME + " (" +
                COLUMN_ID + " integer primary key autoincrement not null, " +
                COLUMN_CREATE_TIME + " text, " +
                COLUMN_UPDATE_TIME + " text, " +
                COLUMN_MARK + " int, " +
                COLUMN_CONTENT + " text" +
                ");";
        return sql;
    }

    public static SQLiteDatabase getReadableDB(Context context) {
        DatabaseOpenHelper helper = new DatabaseOpenHelper(context);
        return helper.getReadableDatabase();
    }

    public static SQLiteDatabase getWritableDB(Context context) {
        DatabaseOpenHelper helper = new DatabaseOpenHelper(context);
        return helper.getWritableDatabase();
    }

    public static DtoItem findById(Context context, long id) {
        DtoItem item = new DtoItem();
        SQLiteDatabase db = getReadableDB(context);
        String sql = "select * from " + TABLE_NAME + " where _id = ?";
        String[] sql_args = {String.valueOf(id)};
        Cursor cursor = db.rawQuery(sql, sql_args);
        if (cursor.moveToFirst()) {
            // DBのカラム順序に基づく
            item.id = cursor.getInt(0);
            item.create_time = Converter.convertStringToCalendar(cursor.getString(1));
            item.update_time = Converter.convertStringToCalendar(cursor.getString(2));
            item.mark = cursor.getInt(3);
            item.content = cursor.getString(4);
        }
        cursor.close();
        return item;
    }

    public static ArrayList<DtoItem> findAll(Context context) {
        SQLiteDatabase db = getReadableDB(context);
        ArrayList<DtoItem> item_list = new ArrayList<DtoItem>();
        String sql = "select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                DtoItem item = new DtoItem();
                item.id = cursor.getInt(0);
                item.create_time = Converter.convertStringToCalendar(cursor.getString(1));
                item.update_time = Converter.convertStringToCalendar(cursor.getString(2));
                item.mark = cursor.getInt(3);
                item.content = cursor.getString(4);
                item_list.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return item_list;
    }

    public static long insert(Context context, DtoItem item) {
        SQLiteDatabase db = getWritableDB(context);
        ContentValues values = new ContentValues();
        values.put(COLUMN_CREATE_TIME, Converter.convertCalendarToString(item.create_time));
        values.put(COLUMN_UPDATE_TIME, Converter.convertCalendarToString(item.update_time));
        values.put(COLUMN_MARK, String.valueOf(item.mark));
        values.put(COLUMN_CONTENT, item.content);
        return db.insert(TABLE_NAME, null, values);
    }

    public static long update(Context context, DtoItem item) {
        SQLiteDatabase db = getWritableDB(context);
        ContentValues values = new ContentValues();
        values.put(COLUMN_UPDATE_TIME, Converter.convertCalendarToString(item.update_time));
        values.put(COLUMN_MARK, String.valueOf(item.mark));
        values.put(COLUMN_CONTENT, item.content);
        String [] args = {String.valueOf(item.id)};
        long x = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", args);
        return x;

    }
}
