package com.android.yinwear.ui.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.yinwear.R;
import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.network.model.response.LoginResp;
import com.android.yinwear.core.network.model.response.UsersResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SplashActivity";
    private String mRestCallbackId;
    private UsersResp mUserResponse;
    private String mAuthToken;
    private String mYINAccountId;
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);
        edtUserName = findViewById(R.id.edt_account_id);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRestCallbackId = mCoreController.registerCallback(mHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }

    private void launchUserActivity() {
        setProgressBarIndeterminateVisibility(false);
        Intent intentToUsers = new Intent(LoginActivity.this, UserActivity.class);
        intentToUsers.putExtra("user_resp", mUserResponse);
        startActivity(intentToUsers);
        finish();
    }

    private void processLoginRequest() {
        setProgressBarIndeterminateVisibility(true);
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();
        Bundle reqParam = new Bundle();
        Editable userName = edtUserName.getText();
        Editable password = edtPassword.getText();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        reqParam.putString("username", userName.toString());
        reqParam.putString("password", password.toString());
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
            case Constants.REQUEST.LOGIN_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                if (baseResponse.getResponseCode() == 200) {
                    LoginResp loginResp = (LoginResp) Utility.getDataObj(baseResponse.getResponse().toString(), LoginResp.class);
                    Log.d(TAG, "LOGIN SUCCESSFUL");
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.PREFERENCE.AUTH_TOKEN, loginResp.getAuthToken());
                    editor.putBoolean(Constants.PREFERENCE.IS_LOGGED_IN, true);
                    mAuthToken = loginResp.getAuthToken();
                    editor.apply();
                    processDevicesRequest();
                } else {
                    Toast.makeText(this, baseResponse.getResponse().toString(), Toast.LENGTH_LONG).show();
                }
            }
            return true;
            case Constants.REQUEST.DEVICE_REQUEST: {
                processUsersRequest();
            }
            return true;
            case Constants.REQUEST.USER_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                String responseString = baseResponse.getResponse().toString();
                if (baseResponse.getResponseCode() == 200) {
                    mUserResponse = (UsersResp) Utility.getDataObj(responseString, UsersResp.class);
                    launchUserActivity();
                } else {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(Constants.PREFERENCE.AUTH_TOKEN);
                    editor.remove(Constants.PREFERENCE.IS_LOGGED_IN);
                    editor.apply();
                    processLoginRequest();
                    Toast.makeText(this, responseString, Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }
        return super.handleMessage1(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                processLoginRequest();
                break;
        }
    }
}
