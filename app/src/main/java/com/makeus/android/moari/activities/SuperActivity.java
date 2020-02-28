package com.makeus.android.moari.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public abstract class SuperActivity extends AppCompatActivity {

    public Gson mGson = new Gson();
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public ProgressDialog pd;

    public abstract void onClick(View v);
    abstract void initViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();

        int uiOption = getWindow().getDecorView().getSystemUiVisibility();

            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;



        decorView.setSystemUiVisibility( uiOption );
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);



    }

    public void showProgressDialog() {
        if(pd == null)
            pd = new ProgressDialog(this);
//        progressDialog.setMessage(getString(R.string.loading_buy_point));
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
    }


    public void dismissProgressDialog() {
        if(pd != null) pd.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }



}
