package com.makeus.android.moari.datas;

import com.google.gson.annotations.SerializedName;

public class CategoryUserInfoData {

    @SerializedName("name")
    public String name;

    @SerializedName("cnt")
    public int cnt;

    public String getName() {
        return name;
    }

    public int getCnt() {
        return cnt;
    }
}