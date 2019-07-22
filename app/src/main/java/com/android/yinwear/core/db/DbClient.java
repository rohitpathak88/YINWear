package com.android.yinwear.core.db;

import android.content.Context;

import androidx.room.Room;

public class DbClient {

    private static DbClient mInstance;

    private AppDatabase appDatabase;

    private DbClient(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "MyYINDb").build();
    }

    public static synchronized DbClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}