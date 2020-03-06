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

public class CategoryDeleteDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvNo, mTvYes, mTvName;
    private DialogCategorySelectInterface mInetface;
    private int id;
    private String name;
    public Dialog mDialog;

    public CategoryDeleteDialog(Context context, int id, String name, DialogCategorySelectInterface tmpInterface) {
        mContext = context;
        this.id = id;
        this.name = name;
        mInetface = tmpInterface;

        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_category_delete);

        mTvYes = mDialog.findViewById(R.id.dialog_category_delete_yes_tv);
        mTvNo = mDialog.findViewById(R.id.dialog_category_delete_no_tv);
        mTvName = mDialog.findViewById(R.id.dialog_category_delete_tv);

        mTvName.setText(name+" 카테고리를\n 삭제하시겠습니까?");

        mTvYes.setOnClickListener(this);
        mTvNo.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_category_delete_yes_tv:
                mDialog.dismiss();
                mInetface.remove(id);
                break;
            case R.id.dialog_category_delete_no_tv:
                mDialog.dismiss();
                break;
        }
    }
}

