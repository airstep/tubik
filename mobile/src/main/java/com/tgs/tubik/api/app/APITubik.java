package com.tgs.tubik.api.app;

import com.tgs.tubik.api.youtube.model.Profile;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface APITubik {
    String URL = "http://localhost:8080/";

    @GET("/youtube/getToken")
    Observable<Response<Profile>> getYoutubeToken();
}
