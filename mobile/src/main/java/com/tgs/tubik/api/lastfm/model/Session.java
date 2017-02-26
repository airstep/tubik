package com.tgs.tubik.api.lastfm.model;

public class Session {
    public static final String ROOT = "session";

    private String name;
    private String key;
    private String subscriber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }
}
