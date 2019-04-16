package com.example.domain.model;

public class ApiResponse {
    public Photos photos;
    public String stat;

    public ApiResponse(Photos photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "photos=" + photos +
                ", stat='" + stat + '\'' +
                '}';
    }
}
