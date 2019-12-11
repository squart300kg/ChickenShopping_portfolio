package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ArrayConverter {

    public static void arrayToString(ArrayList<HashMap<String, CartData>> array){

    }

    public static void setStringArrayPref(Context context, String key, ArrayList<HashMap<String, CartData>> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }
    public static ArrayList<HashMap<String, CartData>> getStringArrayPref(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<HashMap<String, CartData>> urls = new ArrayList();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    try{
                        HashMap<String, CartData> url = (HashMap<String, CartData>) a.get(i);
                        urls.add(url);
                    }catch(Exception e){}

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}
