package com.makeus.android.moari.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationView;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.adapters.CategoryAdapter;
import com.makeus.android.moari.datas.CurationData;
import com.makeus.android.moari.responses.CategoryResponse;
import com.makeus.android.moari.responses.CurationResponse;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class CurationActivity extends SuperActivity implements View.OnClickListener {

    private TextView mTvSearch, mTvChange;
    private Intent intent;
    private ViewPager2 mViewPager;
    private CategoryAdapter mCategoryAdapter;
    ArrayList<CurationData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initViews();
        initListener();
        InitializeLayout();
        getCur();
    }

    public void init() {

        mCategoryAdapter = new CategoryAdapter(this, list);
        mViewPager.setAdapter(mCategoryAdapter);
        mViewPager.setOrientation(mViewPager.ORIENTATION_VERTICAL);
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
                intent.putExtra("flag", 0); // insert
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

    public void getCur() {
        MoariApp.getRetrofitMethod(getApplicationContext()).getCuration()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CurationResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final CurationResponse res) {
                        if (res.getCode() == 200) {
                            list = res.getResult();
                            Log.i("TESF", "SDV");
                            init();
                        }
                        else {
                            Toast.makeText(CurationActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();

                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                    }
                });
    }
}