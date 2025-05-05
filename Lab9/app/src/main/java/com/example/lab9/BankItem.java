package com.example.lab9;

public class BankItem {
    private final String imageUrl;
    private final String title;
    private final String linkUrl;

    public BankItem(String imageUrl, String title, String linkUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
}