package com.urbannightdev.cardiopp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.urbannightdev.cardiopp.Constant;

/**
 * Created by ghifar on 23/02/18.
 */

public class UserLoggedManager {

    // LogCat tag
    private static String TAG = UserLoggedManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = Constant.this_app;
    private static final String KEY_EMAIL = "emailuserkuy";
    private static final String KEY_UID = "uiduserkuy";
    private static final String KEY_AUTH = "authkuy";
    private static final String KEY_NOHP = "nohpkuy";
    private static final String KEY_NOHP_PENGGUNA = "nohpPENGGUNA";

    private String emailUser;
    private String UidUser;
    private String authKey;
    private String nohp;
    private String nohppengguna;

    public UserLoggedManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setEmailUserLogged(String emailUserlho) {
        editor = pref.edit();
        editor.putString(KEY_EMAIL, emailUserlho);

        // commit changes
        editor.commit();

        Log.d(TAG, "Email login session modified!");
    }

    public String getEmailUserLogged() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getUidUser() {
        return pref.getString(KEY_UID, null);
    }

    public void setUidUser(String uidUser) {
        editor = pref.edit();
        editor.putString(KEY_UID, uidUser);

        // commit changes
        editor.commit();

        Log.d(TAG, "UID USER session modified!");
    }

    public String getAuthKey() {
        return pref.getString(KEY_AUTH, null);
    }

    public void setAuthKey(String authKey) {
        editor = pref.edit();
        editor.putString(KEY_AUTH, authKey);

        // commit changes
        editor.commit();

        Log.d(TAG, "authkey USER session modified!");
    }

    public String getNohp() {
        return pref.getString(KEY_NOHP, null);
    }

    public void setNohp(String nohp) {
        editor = pref.edit();
        editor.putString(KEY_NOHP, nohp);

        // commit changes
        editor.commit();

        Log.d(TAG, "nohp USER session modified!");
    }

    public String getNohppengguna() {
        return pref.getString(KEY_NOHP_PENGGUNA, null);
    }

    public void setNohppengguna(String nohppengguna) {
        editor = pref.edit();
        editor.putString(KEY_NOHP_PENGGUNA, nohppengguna);

        // commit changes
        editor.commit();

        Log.d(TAG, "nohp PENGGUNA session modified!");
    }
}
