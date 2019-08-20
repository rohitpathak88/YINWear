package com.android.yinwear.ui.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.yinwear.R;
import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.network.model.response.UsersResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private String mRestCallbackId;
    private boolean userDataAvailable;
    private boolean isLoggedIn;
    private UsersResp mUserResponse;
    private String mAuthToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        // Enables Always-on
//        setAmbientEnabled();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        isLoggedIn = sharedPref.getBoolean(Constants.PREFERENCE.IS_LOGGED_IN, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRestCallbackId = mCoreController.registerCallback(mHandler);
        mHandler.sendEmptyMessageDelayed(Constants.APP_CONSTANTS.EVENT_RETRY, 3 * 1000);
        if (isLoggedIn) {
            if (!userDataAvailable) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                mAuthToken = sharedPref.getString(Constants.PREFERENCE.AUTH_TOKEN, "");
                processDevicesRequest();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }

    private void launchUserActivity() {
        mHandler.removeMessages(Constants.APP_CONSTANTS.EVENT_RETRY);
        if (!isLoggedIn) {
            Intent intentToLogin = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intentToLogin);
            finish();
        } else {
            if (userDataAvailable) {
                Intent intentToUsers = new Intent(SplashActivity.this, UserActivity.class);
                intentToUsers.putExtra("user_resp", mUserResponse);
                startActivity(intentToUsers);
                finish();
            } else {
                mHandler.sendEmptyMessageDelayed(Constants.APP_CONSTANTS.EVENT_RETRY, 1 * 1000);
            }
        }
    }

    private void processLoginRequest() {
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();
        Bundle reqParam = new Bundle();
        reqParam.putString("username", "rohitpathak88@gmail.com");
        reqParam.putString("password", "MDgaw19");
        NetRequest loginRequest = new NetRequest(Constants.REQUEST.LOGIN_REQUEST, Request.Method.POST,
                Constants.URL.LOGIN, reqParam);
        coreController.addRequest(Constants.NETWORK_REQUEST, loginRequest, false);
    }

    private void processUsersRequest() {
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();

        Bundle reqParam = new Bundle();
        reqParam.putString("authentication_token", mAuthToken);

        NetRequest userRequest = new NetRequest(Constants.REQUEST.USER_REQUEST, Request.Method.POST,
                Constants.URL.USERS, reqParam);
        coreController.addRequest(Constants.NETWORK_REQUEST, userRequest, true);
    }

    private void processDevicesRequest() {
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();
        Bundle reqParam = new Bundle();
        reqParam.putString("authentication_token", mAuthToken);

        NetRequest deviceRequest = new NetRequest(Constants.REQUEST.DEVICE_REQUEST, Request.Method.POST,
                Constants.URL.DEVICES, reqParam);
        coreController.addRequest(Constants.NETWORK_REQUEST, deviceRequest, true);
    }

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
            case Constants.REQUEST.DEVICE_REQUEST: {
                processUsersRequest();
            }
            return true;
            case Constants.REQUEST.USER_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                String responseString = baseResponse.getResponse().toString();
                if (baseResponse.getResponseCode() == 200) {
                    mUserResponse = (UsersResp) Utility.getDataObj(responseString, UsersResp.class);
                    userDataAvailable = true;
                } else {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(Constants.PREFERENCE.AUTH_TOKEN);
                    editor.remove(Constants.PREFERENCE.IS_LOGGED_IN);
                    isLoggedIn = false;
                    editor.apply();
                    processLoginRequest();
                    Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
                }
            }
            return true;
            case Constants.APP_CONSTANTS.EVENT_RETRY:
                launchUserActivity();
                return true;
        }
        return super.handleMessage1(msg);
    }
}
