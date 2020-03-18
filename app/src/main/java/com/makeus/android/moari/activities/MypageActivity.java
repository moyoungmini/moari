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
import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.dialogs.MypageDialog;
import com.makeus.android.moari.interfaces.MypageInterface;
import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.LoginResponse;
import com.makeus.android.moari.responses.UserResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class MypageActivity extends SuperActivity {
    private EditText mEtName, mEtPw, mEtRePw;
    private TextView mTvEmail;
    private Intent intent;
    private Activity activity;
    private MypageDialog dialog;
    private MypageInterface mypageInterface = new MypageInterface() {
        @Override
        public void deleteUser() {
            delete();
        }

        @Override
        public void updateUser() {
            update();
        }

        @Override
        public void logoutUser() {
            SharedPreferences mSharedPreferences = MypageActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.remove(X_ACCESS_TOKEN);
            editor.commit();
            // remove token

            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // set intent flag
            startActivity(intent);
            overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
        }
    };
    // dialog interface

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        initViews();
        getUser();
        init();
    }

    public void init() {
        activity =this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mypage_back_iv:
                finish();
                overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
                break;
            case R.id.mypage_check_iv:
                String name = mEtName.getText().toString();
                String pw = mEtPw.getText().toString();
                String repw = mEtRePw.getText().toString();

                if (name == "") {
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (pw == "") {
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!pw.equals(repw)) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                // validation

                dialog = new MypageDialog(this, 0, mypageInterface);
                break;

            case R.id.mypage_logout_tv:
                dialog = new MypageDialog(this, 3, mypageInterface);
                break;

            case R.id.mypage_delete_user_tv:
                dialog = new MypageDialog(this, 1, mypageInterface);
                break;
        }
    }

    @Override
    void initViews() {
        mEtName = findViewById(R.id.mypage_name_et);
        mEtPw = findViewById(R.id.mypage_pw_et);
        mEtRePw = findViewById(R.id.mypage_re_pw_et);
        mTvEmail = findViewById(R.id.mypage_email_tv);
    }

    public void getUser() {
        MoariApp.getRetrofitMethod(getApplicationContext()).getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final UserResponse res) {
                        if (res.getCode() == 200) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mTvEmail.setText(res.getUserInfo().get(0).getEmail());
                                    mEtName.setText(res.getUserInfo().get(0).getName());
                                }
                            });
                        } else {
                            intent = new Intent(activity, LoginActivity.class);
                            startActivity(intent);
                            finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
    }

    public void update() {

        JsonObject params = new JsonObject();
        params.addProperty("name", mEtName.getText().toString());
        params.addProperty("password", mEtPw.getText().toString());

        MoariApp.getRetrofitMethod(getApplicationContext()).updateUser(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
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
                            intent = new Intent(activity, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            SharedPreferences mSharedPreferences = MypageActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.remove(X_ACCESS_TOKEN);
                            editor.commit();
                            // reset token when token error

                            intent = new Intent(activity, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
                            finish();
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

    public void delete() {
        MoariApp.getRetrofitMethod(getApplicationContext()).deleteUser()
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
                        SharedPreferences mSharedPreferences = MypageActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.remove(X_ACCESS_TOKEN);
                        editor.commit();
                        if (res.getCode() == 200) {
                            intent = new Intent(activity, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
                            finish();
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
}