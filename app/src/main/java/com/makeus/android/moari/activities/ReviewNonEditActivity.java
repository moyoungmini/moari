package com.makeus.android.moari.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.ReviewDetailData;
import com.makeus.android.moari.dialogs.MypageDialog;
import com.makeus.android.moari.interfaces.MypageInterface;
import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.CategoryResponse;
import com.makeus.android.moari.responses.ReviewDetailResponse;
import com.makeus.android.moari.responses.UserResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class ReviewNonEditActivity extends SuperActivity {
   private TextView mTvTitle, mTvOneline, mTvCategory, mTvDate, mTvContent;
   private ImageView mIvRating, mIvBackground;
   private Activity activity;
   private ArrayList<CategoryData> mCategoryList = new ArrayList<>();
   private FrameLayout layout;
   private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_non_edit);

        initViews();
        init();
        getCategory();
        getReviewDetail(id);
    }

    public void init() {
        activity =this;
        id = getIntent().getIntExtra("id",-1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_non_edit_back_iv:
                finish();
                break;
            case R.id.review_non_edit_pencil_iv:
                Intent intent = new Intent(this, ReviewEditActivity.class);
                intent.putExtra("id", id);
                finish();
                startActivity(intent);
                break;
            case R.id.review_non_edit_share_iv:
                viewToJPG();
                break;
        }
    }

    public void viewToJPG() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "moari.jpg";
        final File file = new File(myDir, fname);

        try {
            FileOutputStream out = new FileOutputStream(file);
            layout.setDrawingCacheEnabled(true);
            Bitmap finalBitmap = layout.getDrawingCache();
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("VDS", root);
        shareInstagram(fname);
    }

    public void shareInstagram(String filename) {
        String type = "image/*";
        String mediaPath = Environment.getExternalStorageDirectory().toString()+"/"+ filename;
        createInstagramIntent(type, mediaPath);
    }

    @Override
    void initViews() {
       mTvTitle = findViewById(R.id.review_non_edit_title_tv);
       mTvOneline = findViewById(R.id.review_non_edit_oneline_tv);
       mTvCategory = findViewById(R.id.review_non_edit_category_tv);
       mTvDate = findViewById(R.id.review_non_edit_date_tv);
       mTvContent = findViewById(R.id.review_non_edit_content_tv);
       mIvRating = findViewById(R.id.review_non_edit_picture_show_iv);
       mIvBackground = findViewById(R.id.review_non_edit_picture_show_iv);
       layout = findViewById(R.id.review_non_edit_layout);
    }

    public void setRate(float rate) {
        if (rate == 0) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_00);
        } else if (rate == 0.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_01);
        } else if (rate == 1) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_10);
        } else if (rate == 1.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_11);
        } else if (rate == 2) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_20);
        } else if (rate == 2.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_21);
        } else if (rate == 3) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_30);
        } else if (rate == 3.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_31);
        } else if (rate == 4) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_40);
        } else if (rate == 4.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_41);
        } else if (rate == 5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_50);
        }
    }

    public void getReviewDetail(int id) {
        MoariApp.getRetrofitMethod(getApplicationContext()).getReviewDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewDetailResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final ReviewDetailResponse res) {
                        if (res.getCode() == 200) {
                            ReviewDetailData data = res.getResult().get(0);
                            int postId = data.getCategoryType();
                            String date = data.getReviewDate();
                            date = date.replace(".", "-");
                            date = date.replace(" ", "");
                            mTvDate.setText(date);

                            for (int i = 0; i < mCategoryList.size(); i++) {
                                Log.i("TESETET", mCategoryList.get(i).getCategoryName());
                                if (postId == mCategoryList.get(i).getIdcategory()) {
                                    mTvCategory.setText(mCategoryList.get(i).categoryName);
                                    break;
                                }
                            }
                            setRate(data.getGrade());
                            mTvContent.setText(data.getContent());
                            mTvOneline.setText(data.getReview());
                            mTvTitle.setText(data.getTitle());
                            Glide.with(activity)
                                    .load(data.getImage())
                                    .fitCenter()
                                    .into(mIvBackground);

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
                            mCategoryList = res.getResult();
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

    private void createInstagramIntent(String type, String mediaPath){

        Log.i("TEST", mediaPath);
//        mediaPath = "/storage/moari.jpg";
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
//        Uri uri = Uri.fromFile(media);

        Uri uri = FileProvider.getUriForFile(this,"com.makeus.android.moari.provider", media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
}