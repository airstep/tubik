package com.tgs.tubik.api.lastfm.model.album;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Albums {
    public static final String ROOT = "albums";

    @SerializedName("album")
    private List<Album> list;

    public List<Album> getList() {
        return list;
    }

    public void setList(List<Album> list) {
        this.list = list;
    }
}
