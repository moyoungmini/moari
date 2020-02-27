package com.makeus.android.moari.activities;


import android.os.Bundle;
import android.view.View;

import com.makeus.android.moari.R;
import com.makeus.android.moari.dialogs.SignupDialog;

public class MainActivity extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFlag();
    }

    public void startFlag() {
        int flag = getIntent().getIntExtra("flag", -1);

        if(flag ==0) {
            SignupDialog dialog = new SignupDialog(this);
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    void initViews() {

    }


    //get dept list
}