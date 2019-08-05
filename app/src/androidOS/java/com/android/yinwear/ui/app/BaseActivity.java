package com.android.yinwear.ui.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.db.entity.UserDetail;

public class BaseActivity extends Activity implements Handler.Callback {

    protected Handler mHandler;
    protected CoreController mCoreController;
    protected UserDetail mUserDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        mCoreController = ((YINApplication) this.getApplication()).getCoreController();
        mUserDetail = getIntent().getParcelableExtra("user");
    }

    @Override
    public boolean handleMessage(Message msg) {
        return handleMessage1(msg);
    }

    protected boolean handleMessage1(Message msg) {
        return false;
    }
}
