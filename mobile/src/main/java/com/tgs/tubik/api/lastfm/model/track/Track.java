package com.tgs.tubik.api.lastfm.model.track;

import com.google.gson.annotations.SerializedName;
import com.tgs.tubik.api.lastfm.model.Artist;
import com.tgs.tubik.api.lastfm.model.Image;

import java.util.List;

public class Track {
    String name;
    long duration;
    int listeners;
    String mbid;
    String url;
    List<Image> image;
    Streamable streamable;
    Artist artist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getListeners() {
        return listeners;
    }

    public void setListeners(int listeners) {
        this.listeners = listeners;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Streamable getStreamable() {
        return streamable;
    }

    public void setStreamable(Streamable streamable) {
        this.streamable = streamable;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Image> getImageList() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }
}
