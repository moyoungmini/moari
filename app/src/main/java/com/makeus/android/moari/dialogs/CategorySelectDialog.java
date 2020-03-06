package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.interfaces.DialogCategorySelectInterface;
import com.makeus.android.moari.interfaces.MypageInterface;

public class CategorySelectDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvChange, mTvDelete;
    private DialogCategorySelectInterface mInetface;
    private int id;
    private String name;
    public Dialog mDialog;

    public CategorySelectDialog(Context context, int id, String name, DialogCategorySelectInterface tmpInterface) {
        mContext = context;
        mInetface = tmpInterface;
        this.id = id;
        this.name = name;

        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_category_select);

        mTvChange = mDialog.findViewById(R.id.dialog_category_select_change_tv);
        mTvDelete = mDialog.findViewById(R.id.dialog_category_select_delete_tv);

        mTvChange.setOnClickListener(this);
        mTvDelete.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_category_select_change_tv:
                mDialog.dismiss();
                CategoryChangeDialog categoryDialog = new CategoryChangeDialog(mContext, id, mInetface);
                break;
            case R.id.dialog_mypage_no_tv:
                mDialog.dismiss();
                CategoryDeleteDialog deleteDialog = new CategoryDeleteDialog(mContext, id, name, mInetface);
                break;
        }
    }
}

