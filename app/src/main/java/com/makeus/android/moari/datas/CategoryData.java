package com.makeus.android.moari.datas;

import com.google.gson.annotations.SerializedName;

public class CategoryData {

    @SerializedName("categoryName")
    public String categoryName;

    @SerializedName("idcategory")
    private int idcategory;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(int idcategory) {
        this.idcategory = idcategory;
    }
}