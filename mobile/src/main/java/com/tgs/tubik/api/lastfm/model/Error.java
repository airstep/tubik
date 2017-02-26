package com.tgs.tubik.api.lastfm.model;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;

public class Error {

    @SerializedName("error")
    private int code;

    @SerializedName("message")
    private String message;

    public Error() {}

    public Error(Throwable t) {
        this.message = t.getMessage();
    }

    public Error(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    private List<String> links;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                getLinksString() +
                '}';
    }

    private String getLinksString() {
        String result = "";
        for (String link: links) {
            result += "|" + link;
        }
        return result;
    }

    private static Error parse(Retrofit retrofit, Response response) {
        Error result = new Error();
        Converter<ResponseBody, Error> converter = retrofit.responseBodyConverter(Error.class, new Annotation[0]);
        try {
            result = converter.convert(response.errorBody());
        } catch (IOException ex) {
            return result;
        }
        return result;

    }
    public static Error parse(Retrofit retrofit, Throwable t) {
        if (t instanceof HttpException) {
            Response response = ((HttpException) t).response();
            return parse(retrofit, response);
        }
        return new Error(t);
    }
}