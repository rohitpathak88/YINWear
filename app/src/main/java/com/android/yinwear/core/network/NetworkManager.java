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
import com.android.yinwear.core.network.model.response.DeviceResp;
import com.android.yinwear.core.network.model.response.DeviceRespObj;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

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
    private static final boolean DUMMY_DEVICES_RESP = false;
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

    public void postRequest(NetRequest request) {
        Log.d(TAG, "postRequest");
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

    public void initNetworkManager(Context context, Handler callbackHandler) {
        mContext = context;
        mCallbackHandler = callbackHandler;
        mNetworkHandler = new Handler(this);
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.addRequestFinishedListener(this);
    }

    private void stringRequest(final NetRequest req) {
        Bundle requestParam = (Bundle) req.getRequestParam();
        final Map<String, String> params = getParams(requestParam);
        if (DUMMY_DEVICES_RESP && Constants.REQUEST.DEVICE_REQUEST == req.getRequestId()) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRequest(req);
            baseResponse.setResponseCode(200);

            switch (req.getRequestId()) {
                case Constants.REQUEST.LOGIN_REQUEST:
                    baseResponse.setResponse(loginResp);
                    break;
                case Constants.REQUEST.USER_REQUEST:
                    baseResponse.setResponse(userResp);
                    break;
                case Constants.REQUEST.DEVICE_REQUEST:
                    String dummyDeviceResponse = getDummyDeviceResponse();
                    baseResponse.setResponse(dummyDeviceResponse);
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
                        msg.arg1 = Constants.NETWORK_RESPONSE;
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

        };

        Log.d(TAG, "Request url: " + stringRequest.getUrl() + "Req Params: " + params);
        mRequestQueue.add(stringRequest);

    }

    private String getDummyDeviceResponse() {
        ArrayList<DeviceRespObj> deviceRespObjArrayList = new ArrayList<>();
        DeviceRespObj deviceRespObj1 = new DeviceRespObj();
        deviceRespObj1.setDeviceId("id_01");
        deviceRespObj1.setName("XBox");
        deviceRespObj1.setServiceProvider("Kidslox");
        deviceRespObj1.setUserIds(new ArrayList<String>() {
            {
                add("9D75B01F-97BF-4B8A-96B1-717EFCCB939F");
                add("0F5188F5-9B26-431E-BD15-E7ED98E898A7");
            }
        });

        DeviceRespObj deviceRespObj2 = new DeviceRespObj();
        deviceRespObj2.setDeviceId("id_02");
        deviceRespObj2.setName("XBox2");
        deviceRespObj2.setServiceProvider("Qtime");
        deviceRespObj2.setUserIds(new ArrayList<String>() {
            {
                add("9D75B01F-97BF-4B8A-96B1-717EFCCB939F");
                add("0F5188F5-9B26-431E-BD15-E7ED98E898A7");
                add("cfa40b29-f4b7-42ef-af2f-e333930d4b47");
                add("827BB2F6-716F-447C-9CD1-F8D018CBCDFA");
            }
        });
        deviceRespObjArrayList.add(deviceRespObj1);
        deviceRespObjArrayList.add(deviceRespObj2);
        DeviceResp deviceResp = new DeviceResp("", true, deviceRespObjArrayList);

        return Utility.getJsonString(deviceResp);
    }

    private static Map<String, String> getParams(Bundle params) {
        Map<String, String> param = new HashMap<>();
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
        Log.d(TAG, "onRequestFinished()");
        synchronized (mRequestList) {
            mRequestList.remove(0);
            if (mRequestList.size() > 0) {
                mNetworkHandler.sendMessage(mNetworkHandler.obtainMessage(
                        mRequestList.get(0).getRequestType(), mRequestList.get(0)));
            }
        }
    }

    private String loginResp = "{\"yin_account_id\": \"CFA15131-D6E5-4C6B-9F96-556D3BBAC3D3\", \"message\": \"\", \"success\": true, \"authentication_token\": \"8fe59aa55f9231b695e2c43416c990341788b49b8bea0e0df05\"}\n";
    private String userResp = "{\"users\": [{\"first_name\":\"Rohit\", \"last_name\":\"Pathak\", \"pin\":\"1234\"}, {\"first_name\":\"YIN\", \"last_name\":\"Yo\", \"pin\":\"\"}],\n" +
            "    \"message\": \"\",\n" +
            "    \"success\": true\n" +
            "}";
}
