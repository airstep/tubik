package com.tgs.tubik.api.vk;

import com.tgs.tubik.api.vk.model.Token;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface APIVK {
    String CLIENT_ID = "2274003";
    String CLIENT_SECRET = "hHbZxrka2uZ6jB1inYsH";

    String AUTH_URL = "https://oauth.vk.com/";
    String SCOPE_DEFAULT = "audio,video,friends,groups";

    @GET("/token?"
            + "grant_type=password"
            + "&scope=" + SCOPE_DEFAULT
            + "&client_id=" + CLIENT_ID
            + "&client_secret=" + CLIENT_SECRET)
    Observable<Response<Token>>
    getToken(@Query("username") String username,
             @Query("password") String password);

}
