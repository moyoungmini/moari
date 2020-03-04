package com.makeus.android.moari.responses;

import com.google.gson.annotations.SerializedName;
import com.makeus.android.moari.datas.CurationData;

import java.util.ArrayList;

public class CurationResponse {

    @SerializedName("isSuccess")
    public boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<CurationData> result;

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<CurationData> getResult() {
        return result;
    }
}
