package com.makeus.android.moari.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.makeus.android.moari.adapters.ReviewListAdapter;
import com.makeus.android.moari.adapters.ReviewSearchListAdapter;
import com.makeus.android.moari.responses.ReviewListResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class ReviewSearchActivity extends SuperActivity {
    private EditText mEtSearch;
    private RecyclerView mRVCategory;
    public ReviewSearchListAdapter mCategoryAdapter;
    public Activity activity;
    private Intent intent;

    public int mPage = 0;
    public boolean mNoMoreItem = false;
    public boolean mLoadLock = false;
    // for paging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_search);

        initViews();
        init();

        mEtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mPage =0;
                    mNoMoreItem = false;
                    mLoadLock = false;
                    getReviewList();
                    return true;
                }
                return false;
            }
        });

//        getUser();
    }

    public void init() {
        activity =this;

        mRVCategory.setLayoutManager(new GridLayoutManager(activity,2));
        mCategoryAdapter = new ReviewSearchListAdapter(activity);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_search_back_iv:
               finish();
                overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
                break;
            case R.id.review_search_erase_iv:
                mEtSearch.setText("");
                break;
        }
    }

    @Override
    void initViews() {
        mRVCategory = findViewById(R.id.review_search_rv);
        mEtSearch = findViewById(R.id.review_search_et);
    }

    public void getReviewList() {
        MoariApp.getRetrofitMethod(getApplicationContext()).getSearchList(mPage, mEtSearch.getText().toString())
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