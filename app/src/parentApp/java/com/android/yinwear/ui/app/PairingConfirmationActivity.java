package com.android.yinwear.ui.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.yinwear.R;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.network.model.response.BasicResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

public class PairingConfirmationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PinVerificationActivity";
    private String mRestCallbackId;
    private EditText edtConfirmationPin;
    private TextView txtCode;
    private Button btnConfirmPairing;
    private String mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing_completion);
        edtConfirmationPin = findViewById(R.id.edt_confirmation_pin);
        txtCode = findViewById(R.id.txt_code);
        btnConfirmPairing = findViewById(R.id.btn_confirm_pairing);
        btnConfirmPairing.setOnClickListener(this);
        String pairingCode = getIntent().getStringExtra("pairing_code");
        mDeviceId = getIntent().getStringExtra("device_id");
        txtCode.setText(pairingCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRestCallbackId = mCoreController.registerCallback(mHandler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
            case Constants.REQUEST.PAIRING_COMPLETE_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                String responseString = baseResponse.getResponse().toString();
                if (baseResponse.getResponseCode() == 200) {
                    BasicResp basicResp = (BasicResp) Utility.getDataObj(responseString, BasicResp.class);

                    if (basicResp.isSuccess()) {
                        processPairingConfirmation();
                    } else {
                        Toast.makeText(this, "Unable to Pair", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, baseResponse.getResponse().toString(), Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
        return super.handleMessage1(msg);
    }

    private void processPairingConfirmation() {
        Toast.makeText(this, "Device paired successfully", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_pairing:
                Editable text = edtConfirmationPin.getText();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, "Enter Confirmation Code", Toast.LENGTH_LONG).show();
                } else {
                    verifiyPairingCode(String.valueOf(text));
                }
                break;
        }
    }

    private void verifiyPairingCode(String code) {
//        setProgressBarIndeterminateVisibility(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String authToken = sharedPref.getString(Constants.PREFERENCE.AUTH_TOKEN, "");
        Bundle reqParam = new Bundle();
        reqParam.putString("confirmation_code", code);
        reqParam.putString("device_id", mDeviceId);
        reqParam.putString("authentication_token", authToken);
        NetRequest request = new NetRequest(Constants.REQUEST.PAIRING_COMPLETE_REQUEST, Request.Method.POST,
                Constants.URL.PAIRING_COMPLETE, reqParam);
        mCoreController.addRequest(Constants.NETWORK_REQUEST, request, false);
    }
}
