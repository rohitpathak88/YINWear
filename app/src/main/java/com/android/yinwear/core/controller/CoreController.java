package com.android.yinwear.core.controller;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.yinwear.YINApplication;
import com.android.yinwear.core.db.AppDatabase;
import com.android.yinwear.core.db.entity.DeviceDetail;
import com.android.yinwear.core.db.entity.PersonDetail;
import com.android.yinwear.core.db.entity.Session;
import com.android.yinwear.core.network.NetworkManager;
import com.android.yinwear.core.network.model.request.NetRequest;
import com.android.yinwear.core.network.model.response.BaseResponse;
import com.android.yinwear.core.network.model.response.DeviceResp;
import com.android.yinwear.core.network.model.response.DeviceRespObj;
import com.android.yinwear.core.network.model.response.PersonsResp;
import com.android.yinwear.core.utils.Constants;
import com.android.yinwear.core.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for all communication between UI components and
 * data providers i.e. Database, Network request/response etc.
 */
public class CoreController extends EventLooper implements Handler.Callback {

    private static final String TAG = "CoreController";
    private static CoreController INSTANCE;

    private boolean isInitialized;
    private Context mContext;
    private Handler mControllerHandler;
    private BackgroundHandler mBackgroundHandler;
    private NetworkManager mNetworkManager;
    private AppDatabase mAppDatabase;

    private int CALLBACK_INDEX = 100;
    private final Map<String, Handler> mCallbackHandlerList = new HashMap<>();

    /**
     * Used to get current controller instance
     *
     * @return Current controller instance
     */
    public static CoreController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CoreController();
        }
        return INSTANCE;
    }

    protected CoreController() {
        super(TAG);
    }

    public boolean init(Context context) {
        if (isInitialized) {
            return true;
        }
        mContext = context.getApplicationContext();

        mControllerHandler = new Handler(this);

        HandlerThread mHandlerThread = new HandlerThread("BackgroundHandler",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());

        mNetworkManager = NetworkManager.getInstance();
        mNetworkManager.initNetworkManager(mContext, mControllerHandler);

        mAppDatabase = ((YINApplication) context).getAppDatabase();

        isInitialized = true;
        return true;
    }

    public String registerCallback(Handler cbHandler) {
        if (cbHandler != null) {
            synchronized (mCallbackHandlerList) {
                String regId = String.valueOf(++CALLBACK_INDEX);
                if (!mCallbackHandlerList.containsKey(regId)) {
                    mCallbackHandlerList.put(regId, cbHandler);
                }
                return regId;
            }
        }
        return null;
    }

    public void removeCallback(String cbRegisterId) {
        if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
            if (null != mCallbackHandlerList.get(cbRegisterId)) {
                synchronized (mCallbackHandlerList) {
                    mCallbackHandlerList.remove(cbRegisterId);
                }
            }
        }
    }

    private void notifyGUI(int evType, int cbIndex, int result, Object data) {
        if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
            synchronized (mCallbackHandlerList) {
                if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
                    for (String key : mCallbackHandlerList.keySet()) {
                        mCallbackHandlerList.get(key).obtainMessage(evType, cbIndex, result, data).sendToTarget();
                    }
                }
            }
        } else {
            Log.i(TAG, "No callback receiver.");
        }
    }

    private void notifyGUI(int evType, Object data) {
        if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
            synchronized (mCallbackHandlerList) {
                if (mCallbackHandlerList != null && !mCallbackHandlerList.isEmpty()) {
                    for (String key : mCallbackHandlerList.keySet()) {
                        mCallbackHandlerList.get(key).obtainMessage(evType, data).sendToTarget();
                    }
                }
            }
        } else {
            Log.i(TAG, "No callback receiver.");
        }
    }

    @Override
    protected void eventHandler(int evType, Object reqObj) {
        Log.d(TAG, "eventHandler");
        switch (evType) {
            case Constants.NETWORK_REQUEST:
                mNetworkManager.postRequest((NetRequest) reqObj);
                break;
            case Constants.GET_DATA_FROM_DB:
                mBackgroundHandler.sendMessage(Message.obtain(mBackgroundHandler, Constants.GET_DATA_FROM_DB, reqObj));
                break;
        }
    }

    public boolean addRequest(int what, NetRequest reqObj, boolean processFromCache) {
        if (what == Constants.NETWORK_REQUEST &&
                !Utility.isNetworkAvailable(mContext) && processFromCache) {
            mBackgroundHandler.sendMessage(Message.obtain(mBackgroundHandler, Constants.GET_RESPONSE_FROM_DB, reqObj));
        }
        return postRequest(what, reqObj);
    }

    private void getResponseFromDb(NetRequest reqObj) {
        BaseResponse baseResponse = new BaseResponse();
        switch (reqObj.getRequestId()) {
            case Constants.REQUEST.PERSON_REQUEST: {
                List<PersonDetail> personsList = mAppDatabase.personDao().getAll();
                Message msg = Message.obtain();
                baseResponse.setRequest(reqObj);
                baseResponse.setResponseCode(200);
                PersonsResp personsResp = new PersonsResp("", true,
                        new ArrayList<>(personsList));

                baseResponse.setResponse(Utility.getJsonString(personsResp));
                msg.what = reqObj.getRequestId();
                msg.obj = baseResponse;
                msg.arg1 = Constants.GET_RESPONSE_FROM_DB;
            }
            break;
            case Constants.REQUEST.DEVICE_REQUEST: {
                baseResponse.setResponseCode(200);
                Message msg = Message.obtain();
                msg.what = reqObj.getRequestId();
                msg.obj = baseResponse;
                break;
            }
        }
        notifyGUI(reqObj.getRequestId(), baseResponse);
    }

    private void getDataFromDb(NetRequest reqObj) {
        Object data = null;
        switch (reqObj.getRequestId()) {
            case Constants.REQUEST.DEVICE_LIST_FOR_PERSON_DB_REQUEST:
                List<String> deviceIds = mAppDatabase.sessionDao().loadDevicesByPersonIds(
                        new String[]{(String) reqObj.getRequestParam()});
                List<DeviceDetail> deviceList = new ArrayList<>();
                if (deviceIds == null || deviceIds.isEmpty()) break;
                for (int i = 0; i < deviceIds.size(); i++) {
                    List<DeviceDetail> deviceDetails = mAppDatabase.deviceDao().loadDeviceByIds(new String[]{deviceIds.get(i)});
                    if (deviceDetails != null && !deviceDetails.isEmpty()) {
                        DeviceDetail deviceDetail = deviceDetails.get(0);
                        deviceList.add(deviceDetail);
                    }
                }
                data = deviceList;
                break;
        }
        notifyGUI(reqObj.getRequestId(), data);
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case Constants.NETWORK_RESPONSE:
                processNetworkResponse(msg);
                break;
        }
        return false;
    }

    private void processNetworkResponse(Message msg) {
        BaseResponse baseResponse = (BaseResponse) msg.obj;
        NetRequest request = baseResponse.getRequest();
        if (request != null) {
            switch (request.getRequestId()) {
                case Constants.REQUEST.PERSON_REQUEST: {
                    String responseString = baseResponse.getResponse().toString();
                    if (baseResponse.getResponseCode() == 200) {
                        PersonsResp personResp = (PersonsResp) Utility.getDataObj(responseString, PersonsResp.class);
                        assert personResp != null;
                        ArrayList<PersonDetail> personList = personResp.getPersonList();
                        if (personList == null || personList.isEmpty())
                            return;
                        mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(Constants.SAVE_PERSON_RESPONSE_TO_DB,
                                personList.toArray(
                                        new PersonDetail[personList.size()])));
                    }
                }
                break;
                case Constants.REQUEST.DEVICE_REQUEST: {
                    String responseString = baseResponse.getResponse().toString();
                    if (baseResponse.getResponseCode() == 200) {
                        DeviceResp deviceResp = (DeviceResp) Utility.getDataObj(responseString, DeviceResp.class);
                        assert deviceResp != null;
                        ArrayList<DeviceRespObj> deviceList = deviceResp.getDeviceList();
                        if (deviceList == null || deviceList.isEmpty())
                            return;
                        mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(Constants.SAVE_DEVICE_RESPONSE_TO_DB,
                                deviceList));
                    }
                }
                break;
            }
            notifyGUI(request.getRequestId(), baseResponse);
        }
    }

    class BackgroundHandler extends Handler {
        BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.GET_RESPONSE_FROM_DB:
                    getResponseFromDb((NetRequest) msg.obj);
                    break;
                case Constants.GET_DATA_FROM_DB:
                    getDataFromDb((NetRequest) msg.obj);
                    break;
                case Constants.SAVE_PERSON_RESPONSE_TO_DB:
                    mAppDatabase.personDao().deleteAll();
                    mAppDatabase.personDao().insertAll((PersonDetail[]) msg.obj);
                    break;
                case Constants.SAVE_DEVICE_RESPONSE_TO_DB:
                    ArrayList<DeviceRespObj> deviceRespObjs = (ArrayList<DeviceRespObj>) msg.obj;
                    processDeviceResponseFromServer(deviceRespObjs);
                    break;
            }
        }
    }

    private void processDeviceResponseFromServer(ArrayList<DeviceRespObj> deviceRespObjs) {
        mAppDatabase.deviceDao().deleteAll();
        mAppDatabase.sessionDao().deleteAll();
        DeviceDetail[] deviceArray = new DeviceDetail[deviceRespObjs.size()];
        for (int i = 0; i < deviceRespObjs.size(); i++) {
            DeviceRespObj deviceRespObj = deviceRespObjs.get(i);
            deviceArray[i] = new DeviceDetail(deviceRespObj.getDeviceId(), deviceRespObj.getDeviceType(),
                    deviceRespObj.getName(), deviceRespObj.getServiceProvider());
            ArrayList<String> personIds = deviceRespObj.getPersonIds();
            if (personIds != null) {
                for (int j = 0; j < personIds.size(); j++) {
                    mAppDatabase.sessionDao().insert(new Session(deviceRespObj.getDeviceId(),
                            personIds.get(j)));
                }
            }
        }
        mAppDatabase.deviceDao().insertAll(deviceArray);
    }
}
