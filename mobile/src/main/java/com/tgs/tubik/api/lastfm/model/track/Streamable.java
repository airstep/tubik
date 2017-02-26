package com.tgs.tubik.api.lastfm.model.track;

import com.google.gson.annotations.SerializedName;

public class Streamable {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFulltrack() {
        return fulltrack;
    }

    public void setFulltrack(boolean fulltrack) {
        this.fulltrack = fulltrack;
    }

    @SerializedName("@text")
    String text;

    boolean fulltrack;
}
