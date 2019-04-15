package com.example.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotosNw {
    @SerializedName("page")
    @Expose
    public Long page;
    @SerializedName("pages")
    @Expose
    public Long pages;
    @SerializedName("perpage")
    @Expose
    public Long perpage;
    @SerializedName("total")
    @Expose
    public String total;
    @SerializedName("photo")
    @Expose
    public List<PhotoNw> photo = null;

    public PhotosNw(Long page, Long pages, Long perpage, String total, List<PhotoNw> photo) {
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.photo = photo;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public Long getPerpage() {
        return perpage;
    }

    public void setPerpage(Long perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<PhotoNw> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoNw> photo) {
        this.photo = photo;
    }
}
