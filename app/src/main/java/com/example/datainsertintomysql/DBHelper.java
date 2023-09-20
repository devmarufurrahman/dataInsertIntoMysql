package com.example.datainsertintomysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "userInfo";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME +"( "+ ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+ NAME +" TEXT,"+ PHONE +"  TEXT,"+ EMAIL +" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Boolean insertData(String name, String phone, String email){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME,name);
        values.put(PHONE,phone);
        values.put(EMAIL,email);

        long result = DB.insert(TABLE_NAME,null,values);
        if (result == -1) return false;
        else return true;
    }

    // data fetch
    public Cursor readData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        return cursor;
    }


    // update data
    public void updateData(String name, String phone, String email, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME,name);
        values.put(PHONE,phone);
        values.put(EMAIL,email);

        db.update(TABLE_NAME,values,ID+" = "+ id,null);
    }
}
