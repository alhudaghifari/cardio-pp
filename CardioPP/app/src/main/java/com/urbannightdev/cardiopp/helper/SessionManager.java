package com.urbannightdev.cardiopp.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.urbannightdev.cardiopp.Constant;

/**
 * Created by ghifar on 16/02/18.
 */

public class SessionManager extends Application {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = Constant.this_app;

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    /**
     * constructor session manager wajib mengirim context aktivitas
     * @param context context activity
     */
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * procedure untuk melakukan set apakah sudah login atau belum.
     * isLoggedIn bernilai true ketika berhasil melakukan login.
     * isLoggedIn bernilai false ketika dalam kondisi logout.
     * @param isLoggedIn boolean
     */
    public void setLogin(boolean isLoggedIn) {
        editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    /**
     * function untuk melihat apakah sudah login atau belum
     * true : sudah
     * false : tidak login
     * @return boolean login
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
