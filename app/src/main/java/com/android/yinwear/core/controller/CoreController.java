package com.android.yinwear.core.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.yinwear.core.network.NetworkManager;

/**
 * This class is responsible for all communication between UI components and
 * data providers i.e. Database, Network request/response etc.
 */
public class CoreController extends EventLooper implements Handler.Callback {

    private static final String TAG = "CoreController";
    private static CoreController INSTANCE;

    private boolean isInitialized;
    private Context mContext;
    private Handler mControllerHandler;
    private Handler mNetworkHandler;

    /**
     * Used to get current controller instance
     *
     * @return Current controller instance
     */
    public static CoreController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CoreController();
        }
        return INSTANCE;
    }

    protected CoreController() {
        super(TAG);
    }

    @Override
    protected void eventHandler(int evType, int cbIndex, int data, Object reqObject) {

    }

    public boolean init(Context context) {
        if (isInitialized) {
            return true;
        }
        mContext = context.getApplicationContext();

        mControllerHandler = new Handler(this);

        mNetworkHandler = NetworkManager.getInstance().initNetworkManager(mContext);

        isInitialized = true;
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
