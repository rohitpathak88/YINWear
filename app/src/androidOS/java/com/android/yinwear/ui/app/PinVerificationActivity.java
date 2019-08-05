package com.android.yinwear.ui.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.yinwear.R;

public class PinVerificationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PinVerificationActivity";
    private String mRestCallbackId;
    private EditText edtPin;
    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verification);
        edtPin = findViewById(R.id.edt_pin);
        btnVerify = findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(this);
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
        }
        return super.handleMessage1(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify:
                Editable text = edtPin.getText();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, "Enter Pin", Toast.LENGTH_LONG).show();
                } else {
                    if (mUserDetail.getPin().equals(text.toString())) {
                        Toast.makeText(this, "Pin Verified!", Toast.LENGTH_SHORT).show();
                        Intent intentToHome = new Intent(PinVerificationActivity.this, HomeActivity.class);
                        intentToHome.putExtra("user", mUserDetail);
                        startActivity(intentToHome);
                        finish();
                    } else {
                        Toast.makeText(this, "Pin Mismatch! Please retry!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
