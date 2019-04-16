package com.example.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//this is an entity class in ROOM which means a table in the SQLite database.
//Annotations identify how each part of this class relates to an entry in the database.
@Entity(tableName = "image_table")
public class ImageDbModel {


    @NonNull
    @ColumnInfo(name = "image_tag")
    private String imageTag;

    @NonNull
    @ColumnInfo(name = "imageBase64")
    private String imageBase64;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imageId")
    private String imageId;

    public ImageDbModel(@NonNull String imageTag, @NonNull String imageBase64, @NonNull String title, @NonNull String imageId) {
        this.imageTag = imageTag;
        this.imageBase64 = imageBase64;
        this.title = title;
        this.imageId = imageId;
    }

    @NonNull
    public String getImageId() {
        return imageId;
    }

    public void setImageId(@NonNull String imageId) {
        this.imageId = imageId;
    }


    @NonNull
    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(@NonNull String imageTag) {
        this.imageTag = imageTag;
    }

    @NonNull
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(@NonNull String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }
}
