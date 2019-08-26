package com.android.yinwear.ui.app;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.viewpager.widget.ViewPager;

import com.android.yinwear.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupViewPager();
    }

    private void setupViewPager() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);

        DevicesFragment devicesFragment = new DevicesFragment();
        sectionsPagerAdapter.addFrag(devicesFragment);

        UsersFragment usersFragment = new UsersFragment();
        Parcelable userResp = getIntent().getParcelableExtra("user_resp");
        Bundle usersFragmentBundle = new Bundle();
        usersFragmentBundle.putParcelable("user_resp", userResp);
        usersFragment.setArguments(usersFragmentBundle);
        sectionsPagerAdapter.addFrag(usersFragment);

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}