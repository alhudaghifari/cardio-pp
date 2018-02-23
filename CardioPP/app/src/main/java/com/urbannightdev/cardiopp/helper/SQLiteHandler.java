package com.urbannightdev.cardiopp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.urbannightdev.cardiopp.Constant;

import java.util.HashMap;

/**
 * Created by Alhudaghifari on 11/8/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ngangkuy";

    // Login table name
    private static final String TABLE_USER = "userngangkuy";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + Constant.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Constant.KEY_USERNAME + " TEXT,"
                + Constant.KEY_NAMA + " TEXT,"
                + Constant.KEY_EMAIL + " TEXT,"
                + Constant.KEY_HANDPHONE + " TEXT,"
                + Constant.KEY_POINT + " TEXT,"
                + Constant.KEY_SALDO + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String username, String nama, String email, String handphone, String point, String saldo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.KEY_USERNAME, username);
        values.put(Constant.KEY_NAMA, nama); // Name
        values.put(Constant.KEY_EMAIL, email); // Email
        values.put(Constant.KEY_HANDPHONE, handphone); // handphone
        values.put(Constant.KEY_POINT, point);
        values.put(Constant.KEY_SALDO, saldo);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }



    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(Constant.KEY_ID, cursor.getString(0));
            user.put(Constant.KEY_USERNAME, cursor.getString(1));
            user.put(Constant.KEY_NAMA, cursor.getString(2));
            user.put(Constant.KEY_EMAIL, cursor.getString(3));
            user.put(Constant.KEY_HANDPHONE, cursor.getString(4));
            user.put(Constant.KEY_POINT, cursor.getString(5));
            user.put(Constant.KEY_SALDO, cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetailsByEmail(String EmailUser) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(Constant.KEY_ID, cursor.getString(0));
            user.put(Constant.KEY_USERNAME, cursor.getString(1));
            user.put(Constant.KEY_NAMA, cursor.getString(2));
            user.put(Constant.KEY_EMAIL, cursor.getString(3));
            user.put(Constant.KEY_HANDPHONE, cursor.getString(4));
            user.put(Constant.KEY_POINT, cursor.getString(5));
            user.put(Constant.KEY_SALDO, cursor.getString(6));
        }
        Log.d(TAG,"1. EmailUser : " +cursor.getString(3));
        while (!user.get(Constant.KEY_EMAIL).equals(EmailUser) && !cursor.isLast()) {
            cursor.moveToNext();
            if (cursor.getCount() > 0) {
                user.put(Constant.KEY_ID, cursor.getString(0));
                user.put(Constant.KEY_USERNAME, cursor.getString(1));
                user.put(Constant.KEY_NAMA, cursor.getString(2));
                user.put(Constant.KEY_EMAIL, cursor.getString(3));
                user.put(Constant.KEY_HANDPHONE, cursor.getString(4));
                user.put(Constant.KEY_POINT, cursor.getString(5));
                user.put(Constant.KEY_SALDO, cursor.getString(6));
            }
        }

        //jika data tidak ditemukan
        if (!user.get(Constant.KEY_EMAIL).equals(EmailUser)) {
            user.put(Constant.KEY_ID, null);
            user.put(Constant.KEY_USERNAME, null);
            user.put(Constant.KEY_NAMA, null);
            user.put(Constant.KEY_EMAIL, null);
            user.put(Constant.KEY_HANDPHONE, null);
            user.put(Constant.KEY_POINT, "0");
            user.put(Constant.KEY_SALDO, "0");
        }

        Log.d(TAG,"2. EmailUser : " +cursor.getString(3));

        cursor.close();

        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}

