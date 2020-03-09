package com.makeus.android.moari.responses;

import com.google.gson.annotations.SerializedName;
import com.makeus.android.moari.datas.ReviewListData;

import java.util.ArrayList;

public class ReviewListResponse {

    @SerializedName("isSuccess")
    public boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ReviewListData> getResult() {
        return result;
    }

    @SerializedName("result")
    private ArrayList<ReviewListData> result;


}