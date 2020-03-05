package com.makeus.android.moari.responses;

import com.google.gson.annotations.SerializedName;
import com.makeus.android.moari.datas.ReviewDetailData;

import java.util.ArrayList;

public class ReviewDetailResponse {

    @SerializedName("isSuccess")
    public boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<ReviewDetailData> result;

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ReviewDetailData> getResult() {
        return result;
    }
}
