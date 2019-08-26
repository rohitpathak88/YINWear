package com.android.yinwear.ui.app;

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
import com.android.yinwear.core.network.model.response.PairingAcceptanceResp;
import com.android.yinwear.core.network.model.response.PairingStatusCheckResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

public class PairingAcceptanceActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PinVerificationActivity";
    private String mRestCallbackId;
    private EditText edtPin;
    private TextView txtConfirmationCode;
    private Button btnVerify;
    private Button btnCheckStatus;
    private String mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing_acceptance);
        edtPin = findViewById(R.id.edt_pin);
        txtConfirmationCode = findViewById(R.id.txt_confirmation_pin);
        btnVerify = findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(this);
        btnCheckStatus = findViewById(R.id.btn_check_status);
        btnCheckStatus.setOnClickListener(this);
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

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
            case Constants.REQUEST.PAIRING_ACCEPT_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                String responseString = baseResponse.getResponse().toString();
                if (baseResponse.getResponseCode() == 200) {
                    PairingAcceptanceResp pairingAcceptanceResponse = (PairingAcceptanceResp) Utility.getDataObj(responseString, PairingAcceptanceResp.class);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    mDeviceId = pairingAcceptanceResponse.getDeviceId();
                    editor.putString(Constants.PREFERENCE.PAIRED_DEVICE_ID, mDeviceId);
                    editor.putString(Constants.PREFERENCE.PAIRING_CONFIRMATION_CODE + mDeviceId,
                            pairingAcceptanceResponse.getConfirmationCode());
                    editor.apply();
                    processPairingConfirmation(pairingAcceptanceResponse);
                } else {
                    Toast.makeText(this, baseResponse.getResponse().toString(), Toast.LENGTH_LONG).show();
                }
            }
            break;
            case Constants.REQUEST.CHECK_PAIRING_STATUS:{
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                String responseString = baseResponse.getResponse().toString();
                if (baseResponse.getResponseCode() == 200) {
                    PairingStatusCheckResp basicResp = (PairingStatusCheckResp) Utility.getDataObj(responseString,
                                    PairingStatusCheckResp.class);
                    if (basicResp.isSuccess()) {
                        Toast.makeText(this, "Pairing status: " + basicResp.getPairingStatus(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Pairing status: " + basicResp.getPairingStatus(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, baseResponse.getResponse().toString(), Toast.LENGTH_LONG).show();
                }
            }
                break;
        }
        return super.handleMessage1(msg);
    }

    private void processPairingConfirmation(PairingAcceptanceResp pairingAcceptanceResponse) {
        findViewById(R.id.layout_pairing).setVisibility(View.GONE);
        findViewById(R.id.layout_confirmation).setVisibility(View.VISIBLE);
        txtConfirmationCode.setText(pairingAcceptanceResponse.getConfirmationCode());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify:
                Editable text = edtPin.getText();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, "Enter Pairing Code", Toast.LENGTH_LONG).show();
                } else {
                    verifiyPairingCode(String.valueOf(text));
                }
                break;
            case R.id.btn_check_status:
                verifiyPairingStatus();
                break;
        }
    }

    private void verifiyPairingStatus() {
//        setProgressBarIndeterminateVisibility(true);
        Bundle reqParam = new Bundle();
        reqParam.putString("device_id", mDeviceId);
        NetRequest request = new NetRequest(Constants.REQUEST.CHECK_PAIRING_STATUS, Request.Method.POST,
                Constants.URL.CHECK_PAIRING_STATUS, reqParam);
        mCoreController.addRequest(Constants.NETWORK_REQUEST, request, false);
    }

    private void verifiyPairingCode(String code) {
//        setProgressBarIndeterminateVisibility(true);
        Bundle reqParam = new Bundle();
        reqParam.putString("pairing_code", code);
        NetRequest request = new NetRequest(Constants.REQUEST.PAIRING_ACCEPT_REQUEST, Request.Method.POST,
                Constants.URL.PAIRING_ACCEPT, reqParam);
        mCoreController.addRequest(Constants.NETWORK_REQUEST, request, false);
    }
}
