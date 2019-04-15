package com.example.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoNw {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("secret")
    @Expose
    public String secret;
    @SerializedName("server")
    @Expose
    public String server;
    @SerializedName("farm")
    @Expose
    public Long farm;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("ispublic")
    @Expose
    public Long ispublic;
    @SerializedName("isfriend")
    @Expose
    public Long isfriend;
    @SerializedName("isfamily")
    @Expose
    public Long isfamily;

    public PhotoNw(String id, String owner, String secret, String server, Long farm, String title, Long ispublic, Long isfriend, Long isfamily) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.isfriend = isfriend;
        this.isfamily = isfamily;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Long getFarm() {
        return farm;
    }

    public void setFarm(Long farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getIspublic() {
        return ispublic;
    }

    public void setIspublic(Long ispublic) {
        this.ispublic = ispublic;
    }

    public Long getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Long isfriend) {
        this.isfriend = isfriend;
    }

    public Long getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Long isfamily) {
        this.isfamily = isfamily;
    }
}
