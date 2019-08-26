package com.android.yinwear.ui.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.yinwear.R;
import com.android.yinwear.core.db.entity.DeviceDetail;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.network.model.response.BasicResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.android.yinwear.core.utils.Constants.APP_CONSTANTS.EVENT_PAIRING_CONFIRMATION;

/**
 * A placeholder fragment containing a simple view.
 */
public class DevicesFragment extends BaseFragment {

    private static final String TAG = "DevicesFragment";
    private String mRestCallbackId;
    List<DeviceDetail> mDeviceList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private String mPairingCode;
    private String mSelectedDeviceId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        mRecyclerView = root.findViewById(R.id.list);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mRestCallbackId = mCoreController.registerCallback(mHandler);
        requestDevicesFromDb();
    }

    private void requestDevicesFromDb() {
        NetRequest deviceRequest = new NetRequest(Constants.REQUEST.DEVICE_LIST_ALL_DB_REQUEST, -1,
                "", null);
        mCoreController.addRequest(Constants.GET_DATA_FROM_DB, deviceRequest, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }


    private void populateDeviceList() {
        if (mDeviceList == null || mDeviceList.isEmpty()) {
            Toast.makeText(getContext(), "Empty list", Toast.LENGTH_SHORT).show();
            return;
        }

        LinearLayoutManager mPagerLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mPagerLayoutManager);
        mRecyclerView.setAdapter(new DevicesAdapter(getContext(), mDeviceList, new DevicesAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(Integer position) {
                processItemClick(position);
            }
        }));
    }

    private void processItemClick(Integer position) {
        DeviceDetail deviceDetail = mDeviceList.get(position);
        mSelectedDeviceId = deviceDetail.getDeviceId();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPairingCode = sharedPref.getString(Constants.PREFERENCE.PAIRING_CODE + mSelectedDeviceId, "");
        if (!TextUtils.isEmpty(mPairingCode)) {
            displayPairingInitiateDialog();
            return;
        }
//        setProgressBarIndeterminateVisibility(true);
        Bundle reqParam = new Bundle();
        String authToken = sharedPref.getString(Constants.PREFERENCE.AUTH_TOKEN, "");
        reqParam.putString("authentication_token", authToken);
        reqParam.putString("device_id", mSelectedDeviceId);
        mPairingCode = Utility.generatePairingCode();
        reqParam.putString("pairing_code", mPairingCode);
        NetRequest request = new NetRequest(Constants.REQUEST.PAIRING_INITIATE_REQUEST, Request.Method.POST,
                Constants.URL.PAIRING_INITIATE, reqParam);
        mCoreController.addRequest(Constants.NETWORK_REQUEST, request, false);
    }

    private void displayPairingInitiateDialog() {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Pairing Initiated")
//                .setMessage("Enter " + mPairingCode + " pairing code on the device")
//                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        processDevicesRequest();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
        Intent intentToCompletion = new Intent(getActivity(), PairingConfirmationActivity.class);
        intentToCompletion.putExtra("pairing_code", mPairingCode);
        intentToCompletion.putExtra("device_id", mSelectedDeviceId);
        startActivityForResult(intentToCompletion, EVENT_PAIRING_CONFIRMATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_PAIRING_CONFIRMATION) {
            processDevicesRequest();
        }
    }

    private void processDevicesRequest() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String authToken = sharedPref.getString(Constants.PREFERENCE.AUTH_TOKEN, "");
        Bundle reqParam = new Bundle();
        reqParam.putString("authentication_token", authToken);

        NetRequest deviceRequest = new NetRequest(Constants.REQUEST.DEVICE_REQUEST, Request.Method.POST,
                Constants.URL.DEVICES, reqParam);
        mCoreController.addRequest(Constants.NETWORK_REQUEST, deviceRequest, true);
    }

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
            case Constants.REQUEST.DEVICE_LIST_ALL_DB_REQUEST: {
                if (msg.obj != null) {
                    mDeviceList = (List<DeviceDetail>) msg.obj;
                    populateDeviceList();
                } else {
                    Toast.makeText(getContext(), "No Device found", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case Constants.REQUEST.PAIRING_INITIATE_REQUEST: {
                BaseResponse baseResponse = (BaseResponse) msg.obj;
                if (baseResponse.getResponseCode() == 200) {
                    BasicResp resp = (BasicResp) Utility.getDataObj(baseResponse.getResponse().toString(), BasicResp.class);
                    Log.d(TAG, "Pairing Initiate Resonse: " + resp.isSuccess());
                    if (resp.isSuccess()) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(Constants.PREFERENCE.PAIRING_CODE + mSelectedDeviceId, mPairingCode);
                        editor.apply();
                        displayPairingInitiateDialog();
                    }
                } else {
                    Toast.makeText(getContext(), baseResponse.getResponse().toString(), Toast.LENGTH_LONG).show();
                }
            }
            break;
            case Constants.REQUEST.DEVICE_REQUEST: {
                requestDevicesFromDb();
            }
            break;
        }
        return super.handleMessage1(msg);
    }
}
