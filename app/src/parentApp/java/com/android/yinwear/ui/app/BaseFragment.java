package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;

public class BaseFragment extends Fragment implements Handler.Callback {

    protected Handler mHandler;
    protected CoreController mCoreController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        mCoreController = ((YINApplication) getContext().getApplicationContext()).getCoreController();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return handleMessage1(msg);
    }

    protected boolean handleMessage1(Message msg) {
        return false;
    }
}