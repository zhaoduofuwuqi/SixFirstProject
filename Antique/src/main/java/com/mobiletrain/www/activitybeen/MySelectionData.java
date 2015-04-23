package com.mobiletrain.www.activitybeen;

/**
 * Created by aaa on 15-4-22.
 */
public class MySelectionData {
    private int _id;
    private String name;
    private String image_url;
    private String url;

    public MySelectionData() {
    }

    public MySelectionData(String name, String image_url, String url) {
        this.name = name;
        this.image_url = image_url;
        this.url = url;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
