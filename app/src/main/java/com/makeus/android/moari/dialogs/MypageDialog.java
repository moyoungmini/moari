package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.activities.MypageActivity;
import com.makeus.android.moari.interfaces.MypageInterface;

public class MypageDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvMain, mTvNo, mTvYes;
    private int flag;
    private MypageInterface mInetface;

    public Dialog mDialog;

    public MypageDialog(Context context, int isSelect, MypageInterface tmpInterface) {
        mContext = context;
        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_mypage);

        mTvMain = mDialog.findViewById(R.id.dialog_mypage_main_tv);
        mTvNo = mDialog.findViewById(R.id.dialog_mypage_no_tv);
        mTvYes = mDialog.findViewById(R.id.dialog_mypage_yes_tv);

        flag = isSelect;

        if(flag ==0) {
            mTvMain.setText("회원정보를 변경하시겠습니까?");
        }
        else if(flag ==1) {
            mTvMain.setText("탈퇴하시겠습니까?");
        }
        else if(flag ==2) {
            mTvMain.setText("탈퇴시\n계졍을 복구할 수 없습니다.\n정말 탈퇴하시겠습니까?");
        }
        else if(flag ==3) {
            mTvMain.setText("로그아웃하시겠습니까?");
        }
        mTvNo.setOnClickListener(this);
        mTvYes.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        mInetface = tmpInterface;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_mypage_yes_tv:
                mDialog.dismiss();
                if(flag ==0) {
                    mInetface.updateUser();
                }
                // update
                else if(flag ==1) {
                    mDialog.dismiss();
                    MypageDialog dialog = new MypageDialog(mContext, 2, mInetface);
                }
                else if(flag ==2) {
                    mInetface.deleteUser();
                }
                // delete
                else if(flag ==3) {
                    mInetface.logoutUser();
                }
                break;
            case R.id.dialog_mypage_no_tv:
                mDialog.dismiss();
                break;
        }
    }
}

