package com.android.yinwear.core.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.yinwear.core.network.NetworkManager;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.utils.Constants;

import java.util.HashMap;
import java.util.Map;

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
    private NetworkManager mNetworkManager;

    private int CALLBACK_INDEX = 100;
    private final Map<String, Handler> mCallbackHandlerList = new HashMap<>();

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

    public boolean init(Context context) {
        if (isInitialized) {
            return true;
        }
        mContext = context.getApplicationContext();

        mControllerHandler = new Handler(this);

        mNetworkManager = NetworkManager.getInstance();
        mNetworkManager.initNetworkManager(mContext, mControllerHandler);

        isInitialized = true;
        return true;
    }

    public String registerCallback(Handler cbHandler) {
        if (cbHandler != null) {
            synchronized (mCallbackHandlerList) {
                String regId = String.valueOf(++CALLBACK_INDEX);
                if (!mCallbackHandlerList.containsKey(regId)) {
                    mCallbackHandlerList.put(regId, cbHandler);
                }
                return regId;
            }
        }
        return null;
    }

    public void removeCallback(String cbRegisterId) {
        if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
            if (null != mCallbackHandlerList.get(cbRegisterId)) {
                synchronized (mCallbackHandlerList) {
                    mCallbackHandlerList.remove(cbRegisterId);
                }
            }
        }
    }

    private void notifyGUI(int evType, int cbIndex, int result, Object data) {
        if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
            synchronized (mCallbackHandlerList) {
                if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
                    for (String key : mCallbackHandlerList.keySet()) {
                        mCallbackHandlerList.get(key).obtainMessage(evType, cbIndex, result, data).sendToTarget();
                    }
                }
            }
        } else {
            Log.i(TAG, "No callback receiver.");
        }
    }

    private void notifyGUI(int evType,  Object data) {
        if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
            synchronized (mCallbackHandlerList) {
                if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
                    for (String key : mCallbackHandlerList.keySet()) {
                        mCallbackHandlerList.get(key).obtainMessage(evType, data).sendToTarget();
                    }
                }
            }
        } else {
            Log.i(TAG, "No callback receiver.");
        }
    }

    @Override
    protected void eventHandler(int evType, Object reqObject) {
        Log.d(TAG,"eventHandler");
        mNetworkManager.postRequest((NetRequest) reqObject);
    }

    public boolean addRequest(int what, Object reqObj) {
        Log.d(TAG,"addRequest");
        return postRequest(what, reqObj);
    }


    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case Constants.NETWORK_RESPONSE:
                processNetworkResponse(msg);
                break;
        }
        return false;
    }

    private void processNetworkResponse(Message msg) {
        BaseResponse baseResponse = (BaseResponse) msg.obj;
        NetRequest request = baseResponse.getRequest();
        if (request != null) {
            notifyGUI(request.getRequestId(), baseResponse);
        }
//        EngineEvents.CALLBACK_TYPE.REST_EVENTS
    }


}
