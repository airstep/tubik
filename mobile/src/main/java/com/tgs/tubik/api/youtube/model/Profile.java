package com.tgs.tubik.api.youtube.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("_json")
    @Expose
    private Json json;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}

