package com.tgs.tubik.api.lastfm.model.tag;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tags {
    public static final String ROOT = "toptags";

    @SerializedName("tag")
    private List<Tag> list;

    public List<Tag> getList() {
        return list;
    }

    public void setList(List<Tag> list) {
        this.list = list;
    }
}
