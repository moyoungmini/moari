package com.makeus.android.moari.activities;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.adapters.MainCategoryAdapter;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.CategoryUserInfoData;
import com.makeus.android.moari.dialogs.AppFinishDialog;
import com.makeus.android.moari.dialogs.SignupDialog;
import com.makeus.android.moari.interfaces.DialogAppFinishInterface;
import com.makeus.android.moari.interfaces.DialogCategorySelectInterface;
import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.CategoryResponse;
import com.makeus.android.moari.responses.LoginResponse;
import com.makeus.android.moari.utils.GridSpacingItemDecoration;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class MainActivity extends SuperActivity implements View.OnClickListener {

    private TextView mTvSearch, mTvChange, mTvUserInfo, mTvNavigation, mTvCount, mTvGuide;
    private Intent intent;
    private Activity activity;
    private RecyclerView mRVCategory;
    private MainCategoryAdapter mCategoryAdapter;
    private String userName = "";
    private int userCnt = 0;
    private boolean isFirst = true;
    int count =0;
    private DialogCategorySelectInterface selectInterface = new DialogCategorySelectInterface() {
        @Override
        public void change(int id, String name) {
            changeCategory(id,name);
        }

        @Override
        public void remove(int id) {
            if(id==1 || id ==2 || id==3 || id ==4) {
                return;
            }
            // exception handling
            deleteCategory(id);
        }

        @Override
        public void plus(String name) {
            plusCategory(name);
        }
    };
    private DialogAppFinishInterface finishInterface = new DialogAppFinishInterface() {
        @Override
        public void appFinish() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        startFlag();
        initViews();
        initListener();
        InitializeLayout();

    }

    public void startFlag() {
        int flag = getIntent().getIntExtra("flag", -1);

        if(flag ==0) {
            String id = getIntent().getStringExtra("id");
            String pw = getIntent().getStringExtra("pw");
            login(id, pw);
        }
        else {
            getCategory();
        }
        // start dialog when signup finish
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_search_tv:
                intent = new Intent(this, ReviewSearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                break;
            case R.id.main_guide_tv:
                intent = new Intent(this, ImageGuideActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                break;
            case R.id.main_change_tv:
                intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                break;
            case R.id.main_plus_iv:
                intent = new Intent(this, ReviewEditActivity.class);
                intent.putExtra("flag", 0); // insert
                startActivity(intent);
                overridePendingTransition(R.anim.amin_slide_in_down, R.anim.amin_slide_out_up);
                break;
            case R.id.main_logo_iv:
                Intent intent = new Intent(this, CurationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                finish();
                break;
            case R.id.main_instagram_tv:
                Uri uri = Uri.parse("https://www.instagram.com/moari_review/?hl=ko");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com")));
                }
                break;
        }
    }

    @Override
    void initViews() {
        mTvSearch = findViewById(R.id.main_search_tv);
        mTvChange = findViewById(R.id.main_change_tv);
        mRVCategory = findViewById(R.id.main_category_rv);
        mTvUserInfo = findViewById(R.id.main_user_info_tv);
        mTvNavigation = findViewById(R.id.main_hamburgerbar_tv);
        mTvCount = findViewById(R.id.main_count_tv);
        mTvGuide = findViewById(R.id.main_guide_tv);
    }

    public void initListener() {
        mTvSearch.setOnClickListener(this);
        mTvChange.setOnClickListener(this);
        mTvGuide.setOnClickListener(this);
    }

    public void InitializeLayout()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_img);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

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
    protected void onResume() {
        super.onResume();
        if(isFirst) {
            isFirst = false;
        }
        else {
            getCategory();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AppFinishDialog dialog = new AppFinishDialog(activity, finishInterface);
        }
    }
    // set navigation drawaer backpress

    public void login(String id, String pw) {

        JsonObject params = new JsonObject();
        params.addProperty("email", id);
        params.addProperty("password", pw);

        MoariApp.getRetrofitMethod(getApplicationContext()).postLogin(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(LoginResponse res) {

                        if (res.getCode() == 200) {
                            if (res.getJwt() != null) {
                                SharedPreferences mSharedPreferences = getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putString(X_ACCESS_TOKEN, res.getJwt());
                                editor.commit();

                                getCategory();

                                SignupDialog dialog = new SignupDialog(activity);
                                // receive token and show success signup
                            } else {
                                Toast.makeText(MainActivity.this, "receive token error", Toast.LENGTH_SHORT).show();
                                // receive from server token error
                            }
                        } else if (res.getCode() == 403) {
                            SharedPreferences mSharedPreferences = MainActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.remove(X_ACCESS_TOKEN);
                            editor.commit();
                        }
                        // token error
                        else {
                            Toast.makeText(MainActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
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
    // receive jwt when signup

    public void getCategory() {
        MoariApp.getRetrofitMethod(getApplicationContext()).getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final CategoryResponse res) {
                        if (res.getCode() == 200) {
                            CategoryUserInfoData categoryData = res.getUserInfo();
                            count = res.getResult().size();
                            userName = categoryData.getName();
                            userCnt = categoryData.getCnt();
                            ArrayList<CategoryData> list = res.getResult();
                            int count = 8 - list.size();
                            for(int i=0;i<list.size();i++){
                                list.get(i).setSelct(true);
                            }
                            for(int i=0;i<count;i++) {
                                CategoryData tmpData = new CategoryData();
                                tmpData.setCategoryName("추가하기");
                                tmpData.setSelct(false);
                                list.add(tmpData);
                            }

                            mRVCategory.setLayoutManager(new GridLayoutManager(activity,3));
                            mRVCategory.addItemDecoration(new GridSpacingItemDecoration(2, 0, 0));
                            mCategoryAdapter = new MainCategoryAdapter(activity, list, selectInterface);
                            mRVCategory.setAdapter(mCategoryAdapter);
                            mTvUserInfo.setText(userName);
                            mTvNavigation.setText(userName);
                            startCounterAnimator();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void plusCategory(String name) {
        if(count>=8) {
            return;
        }

        JsonObject params = new JsonObject();
        params.addProperty("categoryName", name);

        MoariApp.getRetrofitMethod(getApplicationContext()).plusCategory(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            getCategory();
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void changeCategory(int id, String name) {
        JsonObject params = new JsonObject();
        params.addProperty("categoryName", name);
        MoariApp.getRetrofitMethod(getApplicationContext()).changeCategory(id, RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            getCategory();
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void deleteCategory(int id) {
        JsonObject params = new JsonObject();
        MoariApp.getRetrofitMethod(getApplicationContext()).deleteCategory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            getCategory();
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void startCounterAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, userCnt); //0 is min number, 600 is max number
        animator.setDuration(1000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTvCount.setText(animation.getAnimatedValue().toString()+"개가 모였습니다.");
            }
        });
        animator.start();
    }
}