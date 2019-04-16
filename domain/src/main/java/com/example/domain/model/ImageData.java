package com.example.domain.model;

import java.io.Serializable;

public class ImageData implements Serializable {

    String imageBas64;
    String title;
    String imageId;
    String tag;

    public ImageData(String imageBas64, String title, String imageId, String tag) {
        this.imageBas64 = imageBas64;
        this.title = title;
        this.imageId = imageId;
        this.tag = tag;
    }

    public ImageData() {
    }

    public String getImageBas64() {
        return imageBas64;
    }

    public void setImageBas64(String imageBas64) {
        this.imageBas64 = imageBas64;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "imageBas64='" + imageBas64 + '\'' +
                ", title='" + title + '\'' +
                ", imageId='" + imageId + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
