package com.android.yinwear;

import android.app.Application;
import android.util.Log;

import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.db.AppDatabase;
import com.android.yinwear.core.db.DbClient;

import static com.android.yinwear.BuildConfig.DEBUG;


public class YINApplication extends Application {

    private static final String TAG = "YINApplication";

    private CoreController mCoreController;
    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
        initCoreController();
    }

    /**
     * Initialize db
     */
    private void initDB() {
        mAppDatabase = DbClient.getInstance(getApplicationContext()).getAppDatabase();
    }

    /**
     * To initialize application modules
     */
    private void initCoreController() {
        mCoreController = CoreController.getInstance();
        if (!mCoreController.init(this)) {
            if (DEBUG) Log.e(TAG, "Failed to initialize Core Controller");
        }
    }

    public CoreController getCoreController() {
        return mCoreController;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }
}
