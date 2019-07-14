package com.android.yinwear.core.utils;

public interface Constants {
    int NETWORK_REQUEST = 11;
    int NETWORK_RESPONSE = 12;

    interface REQUEST {
        int LOGIN_REQUEST = 101;
        int PERSON_REQUEST = 102;
    }

    interface APP_CONSTANTS {
        int EVENT_RETRY = 1001;
    }

    interface URL {
        String LOGIN = "http://yin2.schwarzsoftware.com.au/cgi-bin/v1_login";
        String PERSONS = "http://yin2.schwarzsoftware.com.au/cgi-bin/v1_persons";
    }

    interface PREFERENCE {
        String AUTH_TOKEN = "authentication_token";
        String ACCOUNT_ID = "yin_account_id";
        String IS_LOGGED_IN = "is_logged_in";
        String PERSON_RESPONSE = "person_list";
    }
}
