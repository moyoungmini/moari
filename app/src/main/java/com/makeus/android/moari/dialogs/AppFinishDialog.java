package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.interfaces.DialogAppFinishInterface;
import com.makeus.android.moari.interfaces.MypageInterface;

public class AppFinishDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvNo, mTvYes;
    private DialogAppFinishInterface mInetface;

    public Dialog mDialog;

    public AppFinishDialog(Context context, DialogAppFinishInterface tmpInterface) {
        mContext = context;
        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_app_finish);

        mTvNo = mDialog.findViewById(R.id.dialog_app_finish_no_tv);
        mTvYes = mDialog.findViewById(R.id.dialog_app_finish_yes_tv);


        mTvNo.setOnClickListener(this);
        mTvYes.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        mInetface = tmpInterface;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_app_finish_yes_tv:
                mDialog.dismiss();
                mInetface.appFinish();

                break;
            case R.id.dialog_app_finish_no_tv:
                mDialog.dismiss();
                break;
        }
    }
}

