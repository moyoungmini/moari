package com.makeus.android.moari.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;

import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;

public class MypageActivity extends SuperActivity {
    private EditText mEtName, mEtPw, mEtRePw;
    private TextView mTvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        initViews();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mypage_back_iv:
                finish();
                break;
            case R.id.mypage_check_iv:
                String name = mEtName.getText().toString();
                String pw = mEtPw.getText().toString();
                String repw = mEtRePw.getText().toString();

                if(name == "") {
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(pw == "") {
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!pw.equals(repw)) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }

                break;
            case R.id.mypage_logout_tv:
                SharedPreferences mSharedPreferences = MypageActivity.this.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.remove(X_ACCESS_TOKEN);
                editor.commit();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.mypage_delete_user_tv:
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

}