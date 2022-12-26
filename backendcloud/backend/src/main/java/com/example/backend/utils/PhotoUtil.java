package com.example.backend.utils;

public class PhotoUtil {
    private int id;
    private String url;

    public PhotoUtil(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
