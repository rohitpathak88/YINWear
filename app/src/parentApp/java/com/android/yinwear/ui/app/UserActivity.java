package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UserAdapter(this, mUserList, new UserAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(Integer position) {
                /*UserDetail userDetail = mUserList.get(position);
                if (TextUtils.isEmpty(userDetail.getPin())) {
                    Intent intentToHome = new Intent(UserActivity.this, HomeActivity.class);
                    intentToHome.putExtra("user", userDetail);
                    startActivity(intentToHome);
                } else {
                    Intent intentToVerification = new Intent(UserActivity.this, PinVerificationActivity.class);
                    intentToVerification.putExtra("user", userDetail);
                    startActivity(intentToVerification);
                }*/
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
