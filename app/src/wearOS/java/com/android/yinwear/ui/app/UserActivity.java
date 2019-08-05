package com.android.yinwear.ui.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.android.yinwear.R;
import com.android.yinwear.core.db.entity.UserDetail;
import com.android.yinwear.core.network.model.response.UsersResp;

import java.util.List;

public class UserActivity extends BaseActivity {

    private static final String TAG = "UserActivity";
    private String mRestCallbackId;
    private List<UserDetail> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        String strUserResp = sharedPref.getString(Constants.PREFERENCE.USER_RESPONSE, "");
//        if (!TextUtils.isEmpty(strUserResp)) {
//            UsersResp userResp = (UsersResp) Utility.getDataObj(strUserResp, UsersResp.class);
//            mUserList = userResp.getUserList();
//        }
        Parcelable userResp = getIntent().getParcelableExtra("user_resp");
        if (userResp != null) {
            mUserList = ((UsersResp) userResp).getUserList();
            populateUserList();
        }
    }

    private void populateUserList() {
        if (mUserList == null || mUserList.isEmpty()) {
            Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            return;
        }
        WearableRecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UserAdapter(this, mUserList, new UserAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(Integer position) {
                UserDetail userDetail = mUserList.get(position);
                if (TextUtils.isEmpty(userDetail.getPin())) {
                    Intent intentToHome = new Intent(UserActivity.this, HomeActivity.class);
                    intentToHome.putExtra("user", userDetail);
                    startActivity(intentToHome);
//                    finish();
                } else {
                    Intent intentToVerification = new Intent(UserActivity.this, PinVerificationActivity.class);
                    intentToVerification.putExtra("user", userDetail);
                    startActivity(intentToVerification);
//                    finish();
                }
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRestCallbackId = mCoreController.registerCallback(mHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }

    @Override
    protected boolean handleMessage1(Message msg) {
        switch (msg.what) {
        }
        return super.handleMessage1(msg);
    }
}
