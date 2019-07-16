package com.android.yinwear.ui.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.yinwear.R;
import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.network.model.response.LoginResp;
import com.android.yinwear.core.network.model.response.PersonsResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private String mRestCallbackId;
    private boolean userDataAvailable;
    private boolean isLoggedIn;

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
        if (!isLoggedIn) {
            processLoginRequest();
        } else {
            if (!userDataAvailable) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                String authToken = sharedPref.getString(Constants.PREFERENCE.AUTH_TOKEN, "");
                String accountId = sharedPref.getString(Constants.PREFERENCE.ACCOUNT_ID, "");
                processPersonsRequest(authToken, accountId);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }

    private void launchPersonsActivity() {
        mHandler.removeMessages(Constants.APP_CONSTANTS.EVENT_RETRY);
        if (userDataAvailable) {
            Intent intentToPersons = new Intent(SplashActivity.this, PersonsActivity.class);
            startActivity(intentToPersons);
            finish();
        } else {
            mHandler.sendEmptyMessageDelayed(Constants.APP_CONSTANTS.EVENT_RETRY, 1 * 1000);
        }
    }

    private void processLoginRequest() {
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();
        Bundle reqParam = new Bundle();
        reqParam.putString("username", "rohitpathak88@gmail.com");
        reqParam.putString("password", "MDgaw19");
        NetRequest loginRequest = new NetRequest(Constants.REQUEST.LOGIN_REQUEST, Request.Method.POST,
                Constants.URL.LOGIN, reqParam);
        coreController.addRequest(Constants.NETWORK_REQUEST, loginRequest);
    }

    private void processPersonsRequest(String authToken, String accountId) {
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();

        Bundle reqParam = new Bundle();
        reqParam.putString("authentication_token", authToken);
        reqParam.putString("yin_account_id", accountId);

        NetRequest personRequest = new NetRequest(Constants.REQUEST.PERSON_REQUEST, Request.Method.POST,
                Constants.URL.PERSONS, reqParam);
        coreController.addRequest(Constants.NETWORK_REQUEST, personRequest);
    }

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
            case Constants.REQUEST.LOGIN_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                if (baseResponse.getResponseCode() == 200) {
                    LoginResp loginResp = (LoginResp) Utility.getDataObj(baseResponse.getResponse().toString(), LoginResp.class);
                    Log.d(TAG, "LOGIN SUCCESSFUL");
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.PREFERENCE.AUTH_TOKEN, loginResp.getAuthToken());
                    editor.putString(Constants.PREFERENCE.ACCOUNT_ID, loginResp.getYin_account_id());
                    editor.putBoolean(Constants.PREFERENCE.IS_LOGGED_IN, true);
                    isLoggedIn = true;
                    editor.apply();
                    processPersonsRequest(loginResp.getAuthToken(), loginResp.getYin_account_id());
                } else {
                    Toast.makeText(this, baseResponse.getResponse().toString(), Toast.LENGTH_LONG).show();
                }
            }
            return true;
            case Constants.REQUEST.PERSON_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                String responseString = baseResponse.getResponse().toString();
                if (baseResponse.getResponseCode() == 200) {
                    PersonsResp personResp = (PersonsResp) Utility.getDataObj(responseString, PersonsResp.class);
                    Log.d(TAG, "PERSON_REQUEST SUCCESSFUL");
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.PREFERENCE.PERSON_RESPONSE, responseString);
                    editor.apply();
                    userDataAvailable = true;
                } else {
                    Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
                }
            }
            return true;
            case Constants.APP_CONSTANTS.EVENT_RETRY:
                launchPersonsActivity();
                return true;
        }
        return super.handleMessage1(msg);
    }
}
