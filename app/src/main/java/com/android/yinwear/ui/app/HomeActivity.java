package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.android.yinwear.R;
import com.android.yinwear.YINApplication;
import com.android.yinwear.core.controller.CoreController;
import com.android.yinwear.core.db.entity.DeviceDetail;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private String mRestCallbackId;
    List<DeviceDetail> mDeviceList = new ArrayList<>();

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
        requestDevicesFromDb();
    }

    private void requestDevicesFromDb() {
        CoreController coreController = ((YINApplication) this.getApplication()).getCoreController();
        NetRequest deviceRequest = new NetRequest(Constants.REQUEST.DEVICE_LIST_FOR_PERSON_DB_REQUEST, -1,
                "", mPersonDetail.getPersonId());
        coreController.addRequest(Constants.GET_DATA_FROM_DB, deviceRequest, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }


    private void populateDeviceList() {
        if (mDeviceList == null || mDeviceList.isEmpty()) {
            Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            return;
        }
        WearableRecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DevicesAdapter(this, mDeviceList, new DevicesAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(Integer position) {
                DeviceDetail deviceDetail = mDeviceList.get(position);
                Toast.makeText(HomeActivity.this, "Service provider: " + deviceDetail.getService_provider(),
                        Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
            case Constants.REQUEST.DEVICE_LIST_FOR_PERSON_DB_REQUEST: {
                if (msg.obj != null) {
                    mDeviceList = (List<DeviceDetail>) msg.obj;
                    populateDeviceList();
                } else {
                    Toast.makeText(this, "No Device found", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
        return super.handleMessage1(msg);
    }
}
