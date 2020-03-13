package com.makeus.android.moari.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.adapters.MainCategoryAdapter;
import com.makeus.android.moari.adapters.ReviewListAdapter;
import com.makeus.android.moari.dialogs.MypageDialog;
import com.makeus.android.moari.interfaces.MypageInterface;
import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.ReviewListResponse;
import com.makeus.android.moari.responses.UserResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class ReviewListActivity extends SuperActivity {
    private TextView mTvCategory;
    private RecyclerView mRVCategory;
    private ReviewListAdapter mCategoryAdapter;
    private int categoryId;
    private String categoryName;
    private Activity activity;
    private Intent intent;

    private int mPage = 0;
    boolean mNoMoreItem = false;
    boolean mLoadLock = false;
    // for paging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        initViews();
        init();
        getReviewList();
//        getUser();
    }

    public void init() {
        activity =this;
        categoryId = getIntent().getIntExtra("categoryId",-1);
        categoryName = getIntent().getStringExtra("categoryName");

        mTvCategory.setText(categoryName);

        mRVCategory.setLayoutManager(new GridLayoutManager(activity,2));
        mCategoryAdapter = new ReviewListAdapter(activity);
        mRVCategory.setAdapter(mCategoryAdapter);
        mRVCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(((GridLayoutManager)mRVCategory.getLayoutManager())==null){
                    return;
                }
                int lastVisiblePosition = ((GridLayoutManager)mRVCategory.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if(mRVCategory.getAdapter()==null){
                    return;
                }
                int itemTotalCount = mRVCategory.getAdapter().getItemCount();

                if(lastVisiblePosition > itemTotalCount * 0.7){
                    if (!mLoadLock) {
                        mLoadLock = true;
                        if (!mNoMoreItem) {
                            getReviewList();
                        }
                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onstart", "1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_list_plus_iv:
                intent = new Intent(this, ReviewEditActivity.class);
                intent.putExtra("flag", 0); // insert
                startActivity(intent);
                break;
            case R.id.review_list_back_iv:
                finish();
                break;
        }
    }

    @Override
    void initViews() {
        mTvCategory = findViewById(R.id.review_list_category_tv);
        mRVCategory = findViewById(R.id.review_list_recyclerview);
    }

    public void getReviewList() {
        MoariApp.getRetrofitMethod(getApplicationContext()).getReviewList(categoryId, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final ReviewListResponse res) {
                        if (res.getCode() == 200) {
                            if(res.getResult().size() ==0 || res.getResult().size() % 10 !=0) {
                                mNoMoreItem =  true;
                            }
                            mCategoryAdapter.addListData(res.getResult());
                            mCategoryAdapter.notifyItemRangeChanged(0, mCategoryAdapter.getListData().size());
                            mPage += res.getResult().size();
                            mLoadLock = false;
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // go to login activity when token error

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

    public void deleteReview(int id) {
        MoariApp.getRetrofitMethod(getApplicationContext()).deleteReview(id)
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

//    public void getUser() {
//        MoariApp.getRetrofitMethod(getApplicationContext()).getUser()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<UserResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mCompositeDisposable.add(d);
//                        showProgressDialog();
//                    }
//
//                    @Override
//                    public void onNext(final UserResponse res) {
//                        if (res.getCode() == 200) {
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mTvEmail.setText(res.getUserInfo().get(0).getEmail());
//                                    mEtName.setText(res.getUserInfo().get(0).getName());
//                                }
//                            });
//                        } else {
//                            intent = new Intent(activity, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                        // go to login activity when token error
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();
//
//                        dismissProgressDialog();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        dismissProgressDialog();
//                    }
//                });
//    }
}