package com.example.prashantpandey.prashant_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by chirag.kadam on 19-09-2014.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static DatabaseOpenHelper helper = null;

    private static final String Database_Name="Dbase";
    private static final String Table_Name="History";
    private static final int Version=2;

    //private static final String _ID = "id";
    public static final String TRACK_NAME = "track";
    public static final String DURATION = "duration";
    public static final String TRACK_URI = "uri";
    private Context mCxt;

    public static DatabaseOpenHelper getInstance(Context ctx) {

        if (helper == null) {
            helper = new DatabaseOpenHelper  (ctx.getApplicationContext());
        }
        return helper;
    }



    public DatabaseOpenHelper(Context context) {
        super(context, Database_Name, null, Version);
        this.mCxt = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Table_Name + "( _id INTEGER PRIMARY KEY AUTOINCREMENT," + TRACK_NAME + " VARCHAR2(100) NOT NULL UNIQUE, " + TRACK_URI + " VARCHAR2(300))";
        db.execSQL(CREATE_CONTACTS_TABLE);

        Log.d("check", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + Table_Name + " ADD " + DURATION + " varchar2(10)");
        Log.d("check", "onUpgrade");
        String[] rank = new String[]{ TRACK_URI};
        Cursor cursor=db.query(Table_Name,rank,null,null,null,null,null);
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Log.d("check", cursor.getString(0));
            String str=cursor.getString(0);
            str=str.replace("file://","");
            Cursor c = mCxt.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media.DURATION},
                    MediaStore.Audio.Media.DATA + "=?", new String[]{str},null);
            c.moveToFirst();

            ContentValues values = new ContentValues();
            values.put(DURATION, c.getString(0));
            db.update(Table_Name, values, TRACK_URI + " = ?",
                    new String[] { cursor.getString(0) });


        }
        // Create tables again
        //onCreate(db);


    }

    public void addHistory(String title, String uri, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Table_Name, "track =?", new String[] { title });

        ContentValues values = new ContentValues();
        values.put(TRACK_NAME, title);
        values.put(TRACK_URI, uri);
        values.put(DURATION,duration);

        // Inserting Row
        db.insert(Table_Name, null, values);

    }


    public Cursor getAllHistory() {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] rank = new String[]{ "_id", TRACK_NAME ,TRACK_URI,DURATION};
        Cursor cursor=db.query(Table_Name,rank,null,null,null,null,"_id DESC ");

        cursor.moveToFirst();

        return cursor;

    }

    public void deleteHistory() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_Name,null,null);
    }

}
