package com.makeus.android.moari.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeus.android.moari.R;
import com.makeus.android.moari.interfaces.DialogRatingInterface;
import com.makeus.android.moari.interfaces.MypageInterface;

public class ReviewRatingDialog implements View.OnClickListener{

    private Context mContext;
    private TextView mTvNo, mTvYes;
    private DialogRatingInterface mInetface;
    private RatingBar mRB;
    private  float rate;

    public Dialog mDialog;

    public ReviewRatingDialog(Context context, float rating, DialogRatingInterface tmpInterface) {
        mContext = context;
        mDialog = new Dialog(mContext);
        this.rate = rating;

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_review_edit_rating);

        mRB = mDialog.findViewById(R.id.dialog_review_ratingbar);
        mTvNo = mDialog.findViewById(R.id.dialog_review_rating_no_tv);
        mTvYes = mDialog.findViewById(R.id.dialog_review_rating_yes_tv);

        mTvNo.setOnClickListener(this);
        mTvYes.setOnClickListener(this);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        mInetface = tmpInterface;

        mRB.setRating(rating);
        mRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_review_rating_yes_tv:
                mDialog.dismiss();
                mInetface.rating(rate);
                break;
            case R.id.dialog_review_rating_no_tv:
                mDialog.dismiss();
                break;
        }
    }
}

