package com.android.yinwear.core.controller;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Used to create event loop to continue
 * LooperThread class extends java thread and prepare a looper in this
 * thread to process all the schedule events. That events mainly comes from
 * GUI thread, All events are process into linear order.
 */
public abstract class EventLooper {
    private static class LooperCallback implements Handler.Callback {
        EventLooper mLooper;
        LooperCallback(EventLooper eventLooper) {
            mLooper = eventLooper;
        }

        @Override
        public final boolean handleMessage(Message msg) {
            mLooper.eventHandler(msg.what, msg.arg1, msg.arg2, msg.obj);
            return true;
        }
    }

    private final Handler mHandler;

    protected EventLooper(String tag) {
        HandlerThread mHandlerThread = new HandlerThread(tag);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), new LooperCallback(this));
    }

    /**
     * Used to post request into event loop
     *
     * @param evType    Event to process
     * @param cbIndex   Callback Index
     * @param reqObject Request data object
     * @return Request process status
     */
    protected boolean postRequest(int evType, int cbIndex, Object reqObject) {
        return postRequest(evType, cbIndex, 0, reqObject, 0L);
    }

    /**
     * Used to post request into event loop
     *
     * @param evType    Event to process
     * @param cbIndex   Callback Index
     * @param data      extra data
     * @param reqObject Request data object
     * @return Request process status
     */
    protected boolean postRequest(int evType, int cbIndex, int data, Object reqObject) {
        return postRequest(evType, cbIndex, data, reqObject, 0L);
    }

    /**
     * Used to post request into event loop at front
     *
     * @param evType    Event to process
     * @param cbIndex   Callback Index
     * @param reqObject Request data object
     * @return Request process status
     */
    protected boolean postRequestAtFrontOfQueue(int evType, int cbIndex, Object reqObject) {
        /** Create new message object here */
        Message newMsg = mHandler.obtainMessage();
        newMsg.what = evType;
        newMsg.arg1 = cbIndex;
        newMsg.arg2 = 0;
        newMsg.obj = reqObject;

        /** Send message to handler here */
        return mHandler.sendMessageAtFrontOfQueue(newMsg);
    }

    /**
     * Used to post request into event loop with delay
     *
     * @param evType    Event to process
     * @param cbIndex   Callback Index
     * @param data      extra data
     * @param reqObject Request data object
     * @param delay     Purpose delay
     * @return Request process status
     */
    protected boolean postRequest(int evType, int cbIndex, int data, Object reqObject, long delay) {
        /** Create new message object here */
        Message newMsg = mHandler.obtainMessage();
        newMsg.what = evType;
        newMsg.arg1 = cbIndex;
        newMsg.arg2 = data;
        newMsg.obj = reqObject;

        /** Send message to handler here */
        return mHandler.sendMessageDelayed(newMsg, delay);
    }

    /**
     * Used to check is request all ready scheduled or not
     *
     * @param what Event Id
     * @return True if scheduled else false
     */
    protected boolean isRequestScheduled(int what) {
        return mHandler.hasMessages(what);
    }

    /**
     * Used to remove scheduled request
     *
     * @param what Event Id
     */
    protected void removeScheduledRequest(int what) {
        mHandler.removeMessages(what);
    }

    /**
     * Used to stop the event loop thread
     *
     * @return true, If stop the loop else false
     */
    public final boolean stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mHandler.getLooper().quitSafely();
        } else {
            mHandler.getLooper().quit();
        }
        return true;
    }

    /**
     * Used to handle all scheduled events into child class
     */
    protected abstract void eventHandler(int evType, int cbIndex, int data, Object reqObject);

}
