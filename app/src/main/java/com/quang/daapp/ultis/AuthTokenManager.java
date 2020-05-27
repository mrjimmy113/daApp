package com.quang.daapp.ultis;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AuthTokenManager {

    private static final String TOKEN = "TOKEN";
    private static final String FILENAME = "TOKENSTORAGE";

    public static void putToken(Context context, String authToken) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN, authToken);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILENAME, Context.MODE_PRIVATE);
        return sharedPref.getString(TOKEN,"");
    }

    public static void removeToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(TOKEN);
        editor.commit();
    }
}
