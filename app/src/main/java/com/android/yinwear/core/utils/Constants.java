package com.android.yinwear.core.utils;

public interface Constants {
    int NETWORK_REQUEST = 11;
    int NETWORK_RESPONSE = 12;
    int SAVE_PERSON_RESPONSE_TO_DB = 13;
    int SAVE_DEVICE_RESPONSE_TO_DB = 14;
    int GET_RESPONSE_FROM_DB = 15;
    int GET_DATA_FROM_DB = 16;

    interface REQUEST {
        int LOGIN_REQUEST = 101;
        int PERSON_REQUEST = 102;
        int DEVICE_REQUEST = 103;
        int DEVICE_LIST_FOR_PERSON_DB_REQUEST = 104;
    }

    interface APP_CONSTANTS {
        int EVENT_RETRY = 1001;
    }

    interface URL {
        String LOGIN = "http://yin2.schwarzsoftware.com.au/cgi-bin/v1_login";
        String PERSONS = "http://yin2.schwarzsoftware.com.au/cgi-bin/v1_persons";
        String DEVICES = "http://yin2.schwarzsoftware.com.au/cgi-bin/v1_devices";
    }

    interface PREFERENCE {
        String AUTH_TOKEN = "authentication_token";
        String ACCOUNT_ID = "yin_account_id";
        String IS_LOGGED_IN = "is_logged_in";
    }
}
