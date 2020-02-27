package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.makeus.android.moari.R;

public class MypageDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvMain, mTvNo, mTvYes;
    private  int flag;

    public Dialog mDialog;

    public MypageDialog(Context context, String text, int isSelect) {
        mContext = context;
        mDialog = new Dialog(mContext);  

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_mypage);

        mTvMain = mDialog.findViewById(R.id.dialog_mypage_main_tv);
        mTvNo = mDialog.findViewById(R.id.dialog_mypage_no_tv);
        mTvYes = mDialog.findViewById(R.id.dialog_mypage_yes_tv);

        mTvMain.setText(text);
        flag = isSelect;

        mTvNo.setOnClickListener(this);
        mTvYes.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_mypage_yes_tv:
                break;
            case R.id.dialog_mypage_no_tv:
                mDialog.dismiss();
                break;
        }
    }
}

