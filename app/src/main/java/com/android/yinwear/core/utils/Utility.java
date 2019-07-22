package com.android.yinwear.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    private static final String TAG = "Utility";

    public JSONObject getJsonObject(Object objToConvert) {
        try {
            if (null != objToConvert) {
                Gson gson = new Gson();
                return new JSONObject(gson.toJson(objToConvert));
            }
        } catch (JSONException e) {
            Log.w(TAG, "getJsonObject: " + e.getMessage());
        }
        return new JSONObject();
    }

    public static String getJsonString(Object objToConvert) {
        if (null != objToConvert) {
            Gson gson = new Gson();
            return gson.toJson(objToConvert);
        } else return "";
    }

    public static Object getDataObj(String jsonString, Class responseClass) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return new GsonBuilder().create().fromJson(jsonString, responseClass);
            } catch (JsonSyntaxException je) {
                Log.e(TAG, "\nJSON SYNTAX EXCEPTION- " + je.getMessage());
            }
        } else {
            Log.e(TAG, "Null JSON string passed, return null object.");
        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        Log.d(TAG, "Utility: isNetworkAvailable()> " + isConnected);
        return isConnected;
    }
}
