package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.interfaces.DialogCategoryInterface;
import com.makeus.android.moari.interfaces.DialogRatingInterface;

import java.util.ArrayList;

public class ReviewCategoryDialog implements View.OnClickListener{

    private Context mContext;
    private DialogCategoryInterface mInetface;
    private ArrayList<TextView> mTvList  = new ArrayList<>();
    private ArrayList<CategoryData> mReceivedDataList;

    public Dialog mDialog;

    public ReviewCategoryDialog(Context context, ArrayList<CategoryData> list, DialogCategoryInterface tmpInterface) {
        mContext = context;
        mReceivedDataList = list;
        mInetface = tmpInterface;

        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_edit_category);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        init();
        setListener();
    }

    public void init() {
        TextView tv1 = mDialog.findViewById(R.id.dialog_edit_category1);
        TextView tv2 = mDialog.findViewById(R.id.dialog_edit_category2);
        TextView tv3 = mDialog.findViewById(R.id.dialog_edit_category3);
        TextView tv4 = mDialog.findViewById(R.id.dialog_edit_category4);
        TextView tv5 = mDialog.findViewById(R.id.dialog_edit_category5);
        TextView tv6 = mDialog.findViewById(R.id.dialog_edit_category6);
        TextView tv7 = mDialog.findViewById(R.id.dialog_edit_category7);
        TextView tv8 = mDialog.findViewById(R.id.dialog_edit_category8);
        mTvList.add(tv1);
        mTvList.add(tv2);
        mTvList.add(tv3);
        mTvList.add(tv4);
        mTvList.add(tv5);
        mTvList.add(tv6);
        mTvList.add(tv7);
        mTvList.add(tv8);

        for(int i=0;i<mReceivedDataList.size();i++){
            mTvList.get(i).setText(mReceivedDataList.get(i).categoryName);
        }
    }

    public void setListener() {
        for(int i=0;i<mReceivedDataList.size();i++){
            mTvList.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_edit_category1:
                mInetface.selectCategory(mReceivedDataList.get(0).getIdcategory(), mReceivedDataList.get(0).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category2:
                mInetface.selectCategory(mReceivedDataList.get(1).getIdcategory(), mReceivedDataList.get(1).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category3:
                mInetface.selectCategory(mReceivedDataList.get(2).getIdcategory(), mReceivedDataList.get(2).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category4:
                mInetface.selectCategory(mReceivedDataList.get(3).getIdcategory(), mReceivedDataList.get(3).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category5:
                mInetface.selectCategory(mReceivedDataList.get(4).getIdcategory(), mReceivedDataList.get(4).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category6:
                mInetface.selectCategory(mReceivedDataList.get(5).getIdcategory(), mReceivedDataList.get(5).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category7:
                mInetface.selectCategory(mReceivedDataList.get(6).getIdcategory(), mReceivedDataList.get(6).getCategoryName());
                mDialog.dismiss();
                break;
            case R.id.dialog_edit_category8:
                mInetface.selectCategory(mReceivedDataList.get(7).getIdcategory(), mReceivedDataList.get(7).getCategoryName());
                mDialog.dismiss();
                break;

        }
    }
}

