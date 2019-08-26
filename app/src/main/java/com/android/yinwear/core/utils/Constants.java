package com.android.yinwear.core.utils;

public interface Constants {
    int NETWORK_REQUEST = 11;
    int NETWORK_RESPONSE = 12;
    int SAVE_USER_RESPONSE_TO_DB = 13;
    int SAVE_DEVICE_RESPONSE_TO_DB = 14;
    int GET_RESPONSE_FROM_DB = 15;
    int GET_DATA_FROM_DB = 16;

    String BASE_URL = "http://yin2.schwarzsoftware.com.au:6543/api/v1/";

    interface REQUEST {
        int LOGIN_REQUEST = 101;
        int USER_REQUEST = 102;
        int DEVICE_REQUEST = 103;
        int DEVICE_LIST_FOR_USER_DB_REQUEST = 104;
        int DEVICE_LIST_ALL_DB_REQUEST = 105;
        int PAIRING_INITIATE_REQUEST = 106;
        int PAIRING_ACCEPT_REQUEST = 107;
        int PAIRING_COMPLETE_REQUEST = 108;
        int CHECK_PAIRING_STATUS = 109;
    }

    interface APP_CONSTANTS {
        int EVENT_RETRY = 1001;
        int EVENT_PAIRING_CONFIRMATION = 1002;
    }

    interface URL {
        String LOGIN = BASE_URL + "login";
        String USERS = BASE_URL + "users";
        String DEVICES = BASE_URL + "devices";
        String PAIRING_INITIATE = BASE_URL + "pairing/initiate";
        String PAIRING_ACCEPT = BASE_URL + "pairing/accept";
        String PAIRING_COMPLETE = BASE_URL + "pairing/complete";
        String CHECK_PAIRING_STATUS = BASE_URL + "pairing/status";
    }

    interface PREFERENCE {
        String AUTH_TOKEN = "authentication_token";
        String PAIRING_CODE = "pairing_code";
        String PAIRING_CONFIRMATION_CODE = "pairing_confirmation_code";
        String PAIRED_DEVICE_ID = "paired_device_id";
        String IS_LOGGED_IN = "is_logged_in";
    }
}
