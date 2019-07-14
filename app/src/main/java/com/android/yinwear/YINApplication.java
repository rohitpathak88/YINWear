package com.android.yinwear;

import android.app.Application;
import android.util.Log;

import com.android.yinwear.core.controller.CoreController;

import static com.android.yinwear.BuildConfig.DEBUG;


public class YINApplication extends Application {

    private static final String TAG = "YINApplication";

    private CoreController mCoreController;

    @Override
    public void onCreate() {
        super.onCreate();
        initCoreController();
    }

    /**
     * To initialize application modules
     */
    private void initCoreController() {
        mCoreController = CoreController.getInstance();
        if (!mCoreController.init(getApplicationContext())) {
            if (DEBUG) Log.e(TAG, "Failed to initialize Core Controller");
        }
    }

    public CoreController getCoreController() {
        return mCoreController;
    }
}
