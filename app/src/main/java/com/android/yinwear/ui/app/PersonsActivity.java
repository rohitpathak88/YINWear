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
import com.android.yinwear.core.db.entity.PersonDetail;
import com.android.yinwear.core.network.model.response.PersonsResp;

import java.util.List;

public class PersonsActivity extends BaseActivity {

    private static final String TAG = "PersonsActivity";
    private String mRestCallbackId;
    private List<PersonDetail> mPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        String strPersonResp = sharedPref.getString(Constants.PREFERENCE.PERSON_RESPONSE, "");
//        if (!TextUtils.isEmpty(strPersonResp)) {
//            PersonsResp personResp = (PersonsResp) Utility.getDataObj(strPersonResp, PersonsResp.class);
//            mPersonList = personResp.getPersonList();
//        }
        Parcelable personResp = getIntent().getParcelableExtra("person_resp");
        if (personResp != null) {
            mPersonList = ((PersonsResp) personResp).getPersonList();
            populatePersonList();
        }
    }

    private void populatePersonList() {
        if (mPersonList == null || mPersonList.isEmpty()) {
            Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            return;
        }
        WearableRecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PersonsAdapter(this, mPersonList, new PersonsAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(Integer position) {
                PersonDetail personDetail = mPersonList.get(position);
                if (TextUtils.isEmpty(personDetail.getPin())) {
                    Intent intentToHome = new Intent(PersonsActivity.this, HomeActivity.class);
                    intentToHome.putExtra("person", personDetail);
                    startActivity(intentToHome);
//                    finish();
                } else {
                    Intent intentToVerification = new Intent(PersonsActivity.this, PinVerificationActivity.class);
                    intentToVerification.putExtra("person", personDetail);
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
