package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.yinwear.R;
import com.android.yinwear.core.db.entity.UserDetail;
import com.android.yinwear.core.network.model.response.UsersResp;

import java.util.List;

public class UsersFragment extends BaseFragment {

    private static final String TAG = "UserFragment";
    private String mRestCallbackId;
    private List<UserDetail> mUserList;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_users, container, false);
        mRecyclerView = root.findViewById(R.id.list);
        Parcelable userResp = getArguments().getParcelable("user_resp");
        if (userResp != null) {
            mUserList = ((UsersResp) userResp).getUserList();
            populateUserList();
        }
        return root;
    }


    private void populateUserList() {
        if (mUserList == null || mUserList.isEmpty()) {
            Toast.makeText(getContext(), "Empty list", Toast.LENGTH_SHORT).show();
            return;
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new UserAdapter(getContext(), mUserList, new UserAdapter.AdapterCallback() {
            @Override
            public void onItemClicked(Integer position) {
                UserDetail userDetail = mUserList.get(position);
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        mRestCallbackId = mCoreController.registerCallback(mHandler);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCoreController.removeCallback(mRestCallbackId);
    }
}