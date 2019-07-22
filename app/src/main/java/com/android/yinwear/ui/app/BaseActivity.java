package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.activity.WearableActivity;

import androidx.annotation.Nullable;

import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.db.entity.PersonDetail;

public class BaseActivity extends WearableActivity implements Handler.Callback  {

    protected Handler mHandler;
    protected CoreController mCoreController;
    protected PersonDetail mPersonDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        mCoreController = ((YINApplication) this.getApplication()).getCoreController();
        mPersonDetail = getIntent().getParcelableExtra("person");
    }

    @Override
    public boolean handleMessage(Message msg) {
        return handleMessage1(msg);
    }

    protected boolean handleMessage1(Message msg) {
        return false;
    }
}
