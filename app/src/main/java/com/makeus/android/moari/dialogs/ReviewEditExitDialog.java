package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.interfaces.DialogReviewExitInterface;

public class ReviewEditExitDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvNo, mTvYes;
    private DialogReviewExitInterface mInetface;

    public Dialog mDialog;

    public ReviewEditExitDialog(Context context, DialogReviewExitInterface tmpInterface) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mInetface = tmpInterface;

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_review_edit_exit);

        mTvNo = mDialog.findViewById(R.id.dialog_review_edit_exit_no);
        mTvYes = mDialog.findViewById(R.id.dialog_review_edit_exit_yes);

        mTvNo.setOnClickListener(this);
        mTvYes.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_review_edit_exit_yes:
                mDialog.dismiss();
                mInetface.exit();
                break;
            case R.id.dialog_review_edit_exit_no:
                mDialog.dismiss();
                break;
        }
    }
}

