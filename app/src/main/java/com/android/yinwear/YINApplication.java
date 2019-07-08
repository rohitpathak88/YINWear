package com.android.yinwear;

import android.app.Application;
import android.util.Log;

import com.android.yinwear.core.controller.CoreController;

import static com.android.yinwear.BuildConfig.DEBUG;

public class YINApplication extends Application {

    private static final String TAG = "YINApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initCoreController();
    }

    /**
     * To initialize application modules
     */
    private void initCoreController() {
        if (!CoreController.getInstance().init(getApplicationContext())) {
            if (DEBUG) Log.e(TAG, "Failed to initialize Core Controller");
        }
    }
}
