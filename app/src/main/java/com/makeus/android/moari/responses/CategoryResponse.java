package com.makeus.android.moari.responses;

import com.google.gson.annotations.SerializedName;
import com.makeus.android.moari.datas.CategoryData;

import java.util.ArrayList;

public class CategoryResponse {

    @SerializedName("isSuccess")
    public boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<CategoryData> result;

    public ArrayList<CategoryData> getResult() {
        return result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
