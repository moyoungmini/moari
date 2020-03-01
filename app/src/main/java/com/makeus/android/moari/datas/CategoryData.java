package com.makeus.android.moari.datas;

public class CategoryData {
    private String title;
    private String oneLine;
    private String content;
    private double star;

    public CategoryData() {
        title = "";
        oneLine = "";
        content = "";
        star = 0;
    }
    public CategoryData(String title, String oneLine, String content, double star) {
        this.title = title;
        this.oneLine = oneLine;
        this.content = content;
        this.star = star;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOneLine(String oneLine) {
        this.oneLine = oneLine;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public String getOneLine() {
        return oneLine;
    }

    public String getContent() {
        return content;
    }

    public double getStar() {
        return star;
    }
}
