package com.textmaxx.models;

public class ModelChannelList {

    private String title;
    private String image;
    private String VideoId;

    public ModelChannelList(String title, String image, String VideoId) {
        this.title = title;
        this.image = image;
        this.VideoId = VideoId;
    }

    public ModelChannelList() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String VideoId) {
        this.VideoId = VideoId;
    }
}