package com.makeus.android.moari.datas;

import com.google.gson.annotations.SerializedName;

public class ReviewDetailData {

    @SerializedName("idboard")
    public int idboard;

    @SerializedName("categoryType")
    private int categoryType;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("image")
    private String image;

    @SerializedName("grade")
    private float grade;

    @SerializedName("review")
    private String review;

    @SerializedName("reviewDate")
    private String reviewDate;

    public int getIdboard() {
        return idboard;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public float getGrade() {
        return grade;
    }

    public String getReview() {
        return review;
    }

    public String getReviewDate() {
        return reviewDate;
    }
}