package com.example.myblog;

public class Blog {
    private String title;
    private String desc;
    private String image;
    private String timestamp;
    private String userid;

    public Blog() {
    }

    public Blog(String title, String desc, String image, String timestamp, String userid) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserid() {
        return userid;
    }
}
