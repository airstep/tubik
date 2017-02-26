package com.tgs.tubik.api.youtube;

import com.tgs.tubik.api.youtube.model.Content;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface APIYouTube {
    String URL = "https://www.googleapis.com/youtube/v3/";
    String KEY = "AIzaSyCNMLE210lRtK2fJpri8HeIJzcelkq-ASI";

    int MAX_RESULTS = 30;

    @GET("search"
            + "?part=id,snippet"
            + "&maxResults=" + MAX_RESULTS)
    Observable<Response<Content>>
    search(@Query("query") String query);

    @GET("activities"
            + "?home=true"
            + "&part=id,snippet,contentDetails"
            + "&maxResults=" + MAX_RESULTS)
    Observable<Response<Content>>
    getRecommended();

    @GET("videos"
            + "?part=snippet,contentDetails"
            + "&chart=mostPopular"
            + "&maxResults=" + MAX_RESULTS)
    Observable<Response<Content>>
    getMostPopular(@Query("regionCode") String regionCode);

}
