package com.android.yinwear.core.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Network request/response using Google Volley
 */
public class NetworkManager implements Handler.Callback {


    private Handler mNetworkHandler;
    private static NetworkManager INSTANCE;
    private Context mContext;
    private RequestQueue mRequestQueue;


    public static final int REST_JSON_POST = 1000;
    public static final int REST_JSON_GET = 1001;

    public static NetworkManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManager();
        }
        return INSTANCE;
    }

    public Handler initNetworkManager(Context context) {
        mContext = context;
        mNetworkHandler = new Handler(this);
        mRequestQueue = Volley.newRequestQueue(context);
        return mNetworkHandler;
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case REST_JSON_POST:
                break;
            case REST_JSON_GET:
                break;
        }
        return false;
    }
}
