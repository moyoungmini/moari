package com.makeus.android.moari.datas;

public class ReviewListData {
    private String title;
    private String content;
    private double star;

    public ReviewListData() {
        title = "";
        content = "";
        star = 0;
    }
    public ReviewListData(String title, String content, double star) {
        this.title = title;
        this.content = content;
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }
}
