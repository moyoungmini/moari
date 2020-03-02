package com.makeus.android.moari.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.responses.LoginResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;


public class SplashActivity extends SuperActivity {

    private Thread splashThread;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((TextView) findViewById(R.id.version)).setText("v" + getVersionInfo(this));

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                String jwt = pref.getString(X_ACCESS_TOKEN, null);
                if (jwt != null) {
                    login();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    public String getVersionInfo(Context context) {
        String version = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }

    public void login() {

        JsonObject params = new JsonObject();

        MoariApp.getRetrofitMethod(getApplicationContext()).postLogin(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(LoginResponse res) {

                        if (res.getCode() == 200) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        // Auto Login success

                        else if (res.getCode() == 403) {
                            SharedPreferences mSharedPreferences = SplashActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.remove(X_ACCESS_TOKEN);
                            editor.commit();
                            // reset token sharedpreference

                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        // token error
                        else {
                            Toast.makeText(SplashActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {}

    @Override
    void initViews() {}
}
