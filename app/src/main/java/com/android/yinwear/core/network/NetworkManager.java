package com.android.yinwear.core.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.android.volley.Request;


/**
 * Network request/response using Google Volley
 */
public class NetworkManager implements Handler.Callback, RequestQueue.RequestFinishedListener<Object> {


    private static final String TAG = "NetworkManager";
    private static final boolean DUMMY_RESP = true;
    private Handler mNetworkHandler;
    private static NetworkManager INSTANCE;
    private Context mContext;
    private List<NetRequest> mRequestList = new ArrayList<>();
    private Handler mCallbackHandler;
    private RequestQueue mRequestQueue;

    public static NetworkManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManager();
        }
        return INSTANCE;
    }

    public void initNetworkManager(Context context, Handler callbackHandler) {
        mContext = context;
        mCallbackHandler = callbackHandler;
        mNetworkHandler = new Handler(this);
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void postRequest(NetRequest request) {
        Log.d(TAG,"postRequest");
        if (mRequestList.size() == 0) {
            mNetworkHandler.sendMessage(mNetworkHandler.obtainMessage(
                    request.getRequestType(), request));
        }
        mRequestList.add(request);
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case Request.Method.POST:
                stringRequest((NetRequest) msg.obj);
                break;
            case Request.Method.GET:
                break;
        }
        return false;
    }

    private void stringRequest(final NetRequest req) {
        Bundle requestParam = (Bundle) req.getRequestParam();
        final Map<String, String> params = getParams(requestParam);
        if (DUMMY_RESP) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRequest(req);
            baseResponse.setResponseCode(200);

            switch (req.getRequestId()) {
                case Constants.REQUEST.LOGIN_REQUEST:
                    baseResponse.setResponse(loginResp);
                    break;
                case Constants.REQUEST.PERSON_REQUEST:
                    baseResponse.setResponse(personResp);
                    break;
            }
            Message msg = Message.obtain();
            msg.what = Constants.NETWORK_RESPONSE;
            msg.obj = baseResponse;
            mCallbackHandler.sendMessage(msg);
            synchronized (mRequestList) {
                mRequestList.remove(req);
                if (mRequestList.size() > 0) {
                    mNetworkHandler.sendMessage(mNetworkHandler.obtainMessage(
                            mRequestList.get(0).getRequestType(), mRequestList.get(0)));
                }
            }
            return;
        }
        StringRequest stringRequest = new StringRequest(req.getRequestType(), req.getRequestUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response from server: " + response);
                        BaseResponse baseResponse = new BaseResponse();
                        baseResponse.setRequest(req);
                        String message = "Something went wrong, Please try again";
                        boolean success = false;
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                success = obj.optBoolean("success");
                                message = obj.optString("message");
                                baseResponse.setResponse(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!success) {
                            baseResponse.setResponseCode(-200);
                            baseResponse.setResponse(message);
                        } else {
                            baseResponse.setResponseCode(200);
                            baseResponse.setResponse(response);
                        }
                        Message msg = Message.obtain();
                        msg.what = Constants.NETWORK_RESPONSE;
                        msg.obj = baseResponse;
                        mCallbackHandler.sendMessage(msg);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errMsg = error.getMessage();
                        Log.d(TAG, "Error: " + error.getMessage());
                        if (TextUtils.isEmpty(errMsg)) {
                            errMsg = "Request timeout";
                        }
                        BaseResponse baseResponse = new BaseResponse(-200, req, errMsg);
                        Message msg = Message.obtain();
                        msg.what = Constants.NETWORK_RESPONSE;
                        msg.obj = baseResponse;
                        mCallbackHandler.sendMessage(msg);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }

        };

        Log.d(TAG,"Request url: " + stringRequest.getUrl() + "Req Params: " + params);
        mRequestQueue.add(stringRequest);

    }

    private static Map<String,String> getParams(Bundle params) {
        Map<String,String> param = new HashMap<>();
        if (null != params) {
            for (String requestParam : params.keySet()) {
                param.put(requestParam,
                        params.getString(requestParam));
            }
        }
        return param;
    }

    @Override
    public void onRequestFinished(Request<Object> request) {
        synchronized (mRequestList) {
            mRequestList.remove(request);
            if (mRequestList.size() > 0) {
                mNetworkHandler.sendMessage(mNetworkHandler.obtainMessage(
                        mRequestList.get(0).getRequestType(), mRequestList.get(0)));
            }
        }
    }

    private String loginResp = "{\"yin_account_id\": \"CFA15131-D6E5-4C6B-9F96-556D3BBAC3D3\", \"message\": \"\", \"success\": true, \"authentication_token\": \"8fe59aa55f9231b695e2c43416c990341788b49b8bea0e0df05\"}\n";
    private String personResp = "{\"persons\": [{\"first_name\":\"Rohit\", \"last_name\":\"Pathak\", \"pin\":\"1234\"}, {\"first_name\":\"YIN\", \"last_name\":\"Yo\", \"pin\":\"\"}],\n" +
            "    \"message\": \"\",\n" +
            "    \"success\": true\n" +
            "}";
}
