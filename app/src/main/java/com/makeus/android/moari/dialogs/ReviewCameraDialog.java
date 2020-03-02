package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.interfaces.DialogCameraInterface;
import com.makeus.android.moari.interfaces.MypageInterface;

public class ReviewCameraDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvAlbum, mTvCamera;
    private DialogCameraInterface mInetface;

    public Dialog mDialog;

    public ReviewCameraDialog(Context context, DialogCameraInterface tmpInterface) {
        mContext = context;
        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_camera_review_edit);

        mTvAlbum = mDialog.findViewById(R.id.dialog_review_album_tv);
        mTvCamera = mDialog.findViewById(R.id.dialog_review_camera_tv);

        mTvAlbum.setOnClickListener(this);
        mTvCamera.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        this.mInetface = tmpInterface;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_review_album_tv:
                mInetface.album();
                mDialog.dismiss();
                break;
            case R.id.dialog_review_camera_tv:
                mInetface.camera();
                mDialog.dismiss();
                break;
        }
    }
}

