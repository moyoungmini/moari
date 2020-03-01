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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.dialogs.SignupDialog;
import com.makeus.android.moari.responses.SignupResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class SignupActivity extends SuperActivity {
    private EditText mEtEmail, mEtRePw, mEtPw, mEtName;
    private ImageView mIvMoariAgree, mIvBasicAgree;
    private boolean mIsMoari, mIsBasic;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();

        activity = this;
        mIsBasic = false;
        mIsMoari = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_back_iv:
                finish();
                break;
            case R.id.signup_moari_agree_iv:
                if(mIsMoari) {
                    mIvMoariAgree.setImageResource(R.drawable.check_off_img);
                }
                else {
                    mIvMoariAgree.setImageResource(R.drawable.check_on_img);
                }
                mIsMoari = !mIsMoari;
                break;
            case R.id.signup_basic_iv:
                if(mIsBasic) {
                    mIvBasicAgree.setImageResource(R.drawable.check_off_img);
                }
                else {
                    mIvBasicAgree.setImageResource(R.drawable.check_on_img);
                }
                mIsBasic = !mIsBasic;
                break;
            case R.id.signup_moari_toagree_iv:
            case R.id.signup_moari_toagree_tv:
                break;
            case R.id.signup_moari_tobasic_iv:
            case R.id.signup_moari_tobasic_tv:
                break;
                // web 이동

            case R.id.signup_btn:
                if(mEtEmail.getText().toString().equals("")) {
                    Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(mEtPw.getText().toString().equals("")) {
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(mEtName.getText().toString().equals("")) {
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!mEtPw.getText().toString().equals(mEtRePw.getText().toString())) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(mEtPw.getText().toString().length()<6) {
                    Toast.makeText(this, "비밀번호를 6자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!mIsBasic || !mIsMoari) {
                    Toast.makeText(this, "약관 동의를 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    break;
                }

                signup();
                break;

        }
    }

    @Override
    public void initViews() {
        mEtEmail = findViewById(R.id.signup_email_et);
        mEtPw = findViewById(R.id.signup_pw_et);
        mEtRePw = findViewById(R.id.signup_repw_et);
        mEtName = findViewById(R.id.signup_nickname_et);
        mIvBasicAgree = findViewById(R.id.signup_basic_iv);
        mIvMoariAgree = findViewById(R.id.signup_moari_agree_iv);
    }

    public void signup() {

        JsonObject params = new JsonObject();
        params.addProperty("email", mEtEmail.getText().toString());
        params.addProperty("name", mEtName.getText().toString());
        params.addProperty("password", mEtPw.getText().toString());

        MoariApp.getRetrofitMethod(getApplicationContext()).postSignup(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignupResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(SignupResponse res) {

                        if(res.getCode() == 200) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("flag", 0);
                            intent.putExtra("id", mEtEmail.getText().toString());
                            intent.putExtra("pw", mEtPw.getText().toString());

                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SignupActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
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