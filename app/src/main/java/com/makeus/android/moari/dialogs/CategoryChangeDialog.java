package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.interfaces.DialogCategorySelectInterface;

public class CategoryChangeDialog implements View.OnClickListener{

    private Context mContext;
    private EditText mEtCategory;
    private TextView mTvNo, mTvYes;
    private DialogCategorySelectInterface mInetface;
    private int id;
    private String name;
    private boolean select;
    public Dialog mDialog;

    public CategoryChangeDialog(Context context, int id, String name, DialogCategorySelectInterface tmpInterface) {
        mContext = context;
        this.id = id;
        this.name = name;
        mInetface = tmpInterface;
        if(id == -1) {
            select = false; // category 추가
        }
        else {
            select = true;
        }

        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_category_change);

        mEtCategory = mDialog.findViewById(R.id.dialog_category_change_et);
        mTvYes = mDialog.findViewById(R.id.dialog_category_change_yes_tv);
        mTvNo = mDialog.findViewById(R.id.dialog_category_change_no_tv);
        mEtCategory.setText(name);

        mTvYes.setOnClickListener(this);
        mTvNo.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_category_change_yes_tv:
                mDialog.dismiss();
                if(select) {
                    mInetface.change(id, mEtCategory.getText().toString());
                }
                else {
                    mInetface.plus(mEtCategory.getText().toString());
                }
                mInetface.change(id, mEtCategory.getText().toString());
                break;
            case R.id.dialog_category_change_no_tv:
                mDialog.dismiss();
                break;
        }
    }
}

