package com.example.mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gadgeontest.db";
    private static final int DATABASE_VERSION = 4;
    static final String TABLE_NAME = "ecg";
    private static final String TAG = DBHelper.class.getSimpleName();
    public static DBHelper helper = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static synchronized DBHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DBHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table DATA(channel0 text,channel1 text, ch0count integer,ch1count integer ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists DATA");
    }

    public boolean insertRecord(String s1, String s2, int s3, int s4) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

            contentValues.put("channel0", s1);
            contentValues.put("channel1", s2);
            contentValues.put("ch0count", s3);
            contentValues.put("ch1count", s4);

        Log.d(TAG,"swa"+contentValues);
        long result=db.insert("DATA", null,
                contentValues);
        if(result==-1){
            return false;
        }
        else {
            return  true;
        }
    }


    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from DATA " ,null);
        return res;
    }

}