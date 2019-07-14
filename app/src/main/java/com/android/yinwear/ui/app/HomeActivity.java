package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.android.yinwear.R;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private String mRestCallbackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView txtWelcomeHome = findViewById(R.id.text_welcome_home);
        txtWelcomeHome.setText("Welcome Home " + mPersonDetail.getFirstName());
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
}
