package com.tgs.tubik.api.lastfm.model.track;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tracks {
    public static String ROOT = "tracks";

    @SerializedName("track")
    private List<Track> list;

    @SerializedName("@attr")
    private TracksAttribute attr;

    public List<Track> getList() {
        return list;
    }

    public void setList(List<Track> list) {
        this.list = list;
    }

    public TracksAttribute getAttr() {
        return attr;
    }

    public void setAttr(TracksAttribute attr) {
        this.attr = attr;
    }
}
