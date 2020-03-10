package com.makeus.android.moari.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.SignupResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class SearchActivity extends SuperActivity {
    private EditText mEtEmail;
    private TextView mTvResultEmail, mTvResultPw;
    private Button mBtnEmail, mBtnPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back_iv:
                finish();
                break;
            case R.id.search_email_check_btn:
                validationUser();
                break;
            case R.id.search_pw_recall_btn:
                temporaryPw();
                break;
        }
    }

    public void init() {
        mBtnPw.setVisibility(View.GONE);
    }

    @Override
    void initViews() {
        mEtEmail = findViewById(R.id.search_email_et);
        mTvResultEmail = findViewById(R.id.search_email_result_tv);
        mTvResultPw = findViewById(R.id.search_pw_result_tv);
        mBtnEmail = findViewById(R.id.search_email_check_btn);
        mBtnPw = findViewById(R.id.search_pw_recall_btn);
    }

    public void validationUser() {

        JsonObject params = new JsonObject();
        params.addProperty("email", mEtEmail.getText().toString());

        MoariApp.getRetrofitMethod(getApplicationContext()).postValidationUser(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
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

                        mTvResultEmail.setText("* " + res.getMessage());
                        if(res.getCode() == 200) {
                           mBtnPw.setVisibility(View.VISIBLE);
                            mEtEmail.setFocusable(false);
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

    public void temporaryPw() {

        JsonObject params = new JsonObject();
        params.addProperty("email", mEtEmail.getText().toString());

        MoariApp.getRetrofitMethod(getApplicationContext()).postFindUser(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
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

                        mTvResultPw.setText("* " + res.getMessage());
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