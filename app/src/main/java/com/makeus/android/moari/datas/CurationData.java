package com.makeus.android.moari.datas;

import com.google.gson.annotations.SerializedName;

public class CurationData {
    @SerializedName("idboard")
    private int idboard;

    @SerializedName("categoryType")
    private int categoryType;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("image")
    private String image;

    @SerializedName("grade")
    private double grade;

    @SerializedName("review")
    private String review;

    @SerializedName("status")
    private String status;

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

    public double getGrade() {
        return grade;
    }

    public String getReview() {
        return review;
    }

    public String getStatus() {
        return status;
    }

    public String getReviewDate() {
        return reviewDate;
    }
}
