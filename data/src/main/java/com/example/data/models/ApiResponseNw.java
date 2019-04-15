package com.example.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponseNw {
    @SerializedName("photos")
    @Expose
    public PhotosNw photos;
    @SerializedName("stat")
    @Expose
    public String stat;

    public ApiResponseNw(PhotosNw photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public PhotosNw getPhotos() {
        return photos;
    }

    public void setPhotos(PhotosNw photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
