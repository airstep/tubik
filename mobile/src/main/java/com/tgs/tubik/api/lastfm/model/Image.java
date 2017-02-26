package com.tgs.tubik.api.lastfm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Image {
    @SerializedName("#text")
    String url;

    ImageSize size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageSize getSize() {
        return size;
    }

    public void setSize(ImageSize size) {
        this.size = size;
    }

    public enum ImageSize {
        @SerializedName("small") SMALL,
        @SerializedName("medium") MEDIUM,
        @SerializedName("large") LARGE,
        @SerializedName("extralarge") EXTRALARGE;

        public static String getLinkBy(ImageSize size, List<Image> imageList) {
            for (Image image: imageList)
                if (image.getSize() == size) return image.getUrl();
            return null;
        }
    }
}
