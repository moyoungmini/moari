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
    public Dialog mDialog;

    private TextView mTvFinish;
    public MypageDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_mypage);

        mTvFinish = mDialog.findViewById(R.id.dialog_mypage_main_tv);
        mTvFinish.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        mDialog.dismiss();
    }
}

