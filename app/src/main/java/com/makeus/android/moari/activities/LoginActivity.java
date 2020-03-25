package com.makeus.android.moari.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.datas.ImageGuideData;
import com.makeus.android.moari.responses.LoginResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class LoginActivity extends SuperActivity {
    private EditText mEtEmail, mEtPw;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
        boolean isGuide = pref.getBoolean("guide_access", true);
        if (isGuide) {
            Intent guideIntent = new Intent(this, ImageGuideActivity.class);
            startActivity(guideIntent);
        }


        initViews();

        mEtPw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loginFun();
                    return true;
                }
                return false;
            }
        });
    }

    public void loginFun() {
        if (mEtEmail.getText().toString().equals("")) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEtPw.getText().toString().equals("")) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // login validation
        login();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                loginFun();
                break;
            case R.id.login_to_search_tv:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                break;
            case R.id.login_to_signup_tv:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                break;

        }
    }

    @Override
    void initViews() {
        mEtEmail = findViewById(R.id.login_email_et);
        mEtPw = findViewById(R.id.login_pw_et);
    }

    public void login() {
        JsonObject params = new JsonObject();
        params.addProperty("email", mEtEmail.getText().toString());
        params.addProperty("password", mEtPw.getText().toString());

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
                                SharedPreferences mSharedPreferences = LoginActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putString(X_ACCESS_TOKEN, res.getJwt());
                                editor.commit();
                                // renewal token

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.amin_slide_in_down, R.anim.amin_slide_out_up);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "receive token error", Toast.LENGTH_SHORT).show();
                            }
                            // token error but login success
                        } else if (res.getCode() == 403) {
                            SharedPreferences mSharedPreferences = LoginActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.remove(X_ACCESS_TOKEN);
                            editor.commit();
                        }
                        // remove token to token error
                        else {
                            Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
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