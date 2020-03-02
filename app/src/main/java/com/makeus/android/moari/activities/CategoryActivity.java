package com.makeus.android.moari.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.makeus.android.moari.R;
import com.makeus.android.moari.adapters.CategoryAdapter;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.dialogs.SignupDialog;

import java.util.ArrayList;

public class CategoryActivity extends SuperActivity implements View.OnClickListener {

    private TextView mTvSearch, mTvChange;
    private Intent intent;
    private ViewPager2 mViewPager;
    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        startFlag();
        initViews();
        initListener();
        InitializeLayout();
        init();

    }

    public void init() {
        ArrayList<CategoryData> list = new ArrayList<>();
        for(int i=0;i<10;i++) {
            CategoryData tmp = new CategoryData();
            tmp.setTitle("TESTSETEST");
            list.add(tmp);
        }
        mCategoryAdapter = new CategoryAdapter(this, list);
        mViewPager.setAdapter(mCategoryAdapter);
        mViewPager.setOrientation(mViewPager.ORIENTATION_VERTICAL);
    }

    public void startFlag() {
        int flag = getIntent().getIntExtra("flag", -1);

        if(flag ==0) {
            SignupDialog dialog = new SignupDialog(this);
        }
        // when signup finish
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_search_tv:
                break;
            case R.id.category_change_tv:
                intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                break;
            case R.id.category_plus_iv:
                intent = new Intent(this, ReviewEditActivity.class);
                startActivity(intent);
                break;
            case R.id.category_logo_iv:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        }
    }

    @Override
    void initViews() {
        mTvSearch = findViewById(R.id.category_search_tv);
        mTvChange = findViewById(R.id.category_change_tv);
        mViewPager = findViewById(R.id.category_viewpager);
    }

    public void initListener() {
        mTvSearch.setOnClickListener(this);
        mTvChange.setOnClickListener(this);
    }

    public void InitializeLayout()
    {
        Toolbar toolbar = findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_img);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.category_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.category_nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawLayout,
                toolbar,
                R.string.open,
                R.string.closed
        );

        drawLayout.addDrawerListener(actionBarDrawerToggle);
    }
    // set toolbar & main_view

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.category_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // set navigation drawaer backpress
}