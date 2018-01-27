package xyz.udacity.udacitypopularmoviesii.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import xyz.udacity.udacitypopularmoviesii.models.Results;


public class AppPreferences {


    private static String FAVORITE = "favorite";

    public static void setFavoriteList(Context context, ArrayList<Results> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(FAVORITE, json);
        editor.commit();
    }

    public static ArrayList<Results> getFavoriteList(Context context) {
        Gson gson = new Gson();
        ArrayList<Results> alarmlist = new ArrayList<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonPreferences = sharedPref.getString(FAVORITE, "");

        Type type = new TypeToken<List<Results>>() {
        }.getType();
        alarmlist = gson.fromJson(jsonPreferences, type);

        return alarmlist;
    }
}
