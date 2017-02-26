package com.tgs.tubik.api.lastfm.model.album;

import com.tgs.tubik.api.lastfm.model.Artist;
import com.tgs.tubik.api.lastfm.model.Image;

import java.util.List;

public class Album {
    String name;
    String mbid;
    String url;
    Artist artist;
    List<Image> image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
