package com.makeus.android.moari.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeus.android.moari.R;

public class SearchActivity extends SuperActivity {
    private EditText mEtEmail;
    private TextView mTvResultEmail, mTvResultPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back_iv:
                finish();
                break;
            case R.id.search_email_check_btn:
                break;
            case R.id.search_pw_recall_btn:
                break;
        }
    }

    public void init() {
        mTvResultEmail.setVisibility(View.INVISIBLE);
        mTvResultPw.setVisibility(View.INVISIBLE);
    }

    @Override
    void initViews() {
        mEtEmail = findViewById(R.id.search_email_et);
        mTvResultEmail = findViewById(R.id.search_email_result_tv);
        mTvResultPw = findViewById(R.id.search_pw_result_tv);
    }

}