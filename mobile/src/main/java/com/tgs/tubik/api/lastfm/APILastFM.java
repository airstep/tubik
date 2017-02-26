package com.tgs.tubik.api.lastfm;

import com.tgs.tubik.api.lastfm.model.Session;
import com.tgs.tubik.api.lastfm.model.Token;
import com.tgs.tubik.api.lastfm.model.album.Albums;
import com.tgs.tubik.api.lastfm.model.tag.Tags;
import com.tgs.tubik.api.lastfm.model.track.Tracks;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface APILastFM {

    String URL = "https://ws.audioscrobbler.com/2.0/";

    // read this http://www.last.fm/api/authentication
    String API_KEY = "the key...";
    String SECRET_KEY = "secret key...";

    // auth
    @GET("auth.getToken")
    Observable<Token> getToken();

    @GET("auth.getMobileSession")
    Observable<Map<String, Session>> getMobileSession(@Query("username") String username,
                                               @Query("authToken") String token,
                                               @Query("api_sig") String signature);

    // geo
    @GET("geo.gettoptracks")
    Observable<Map<String, Tracks>> getTopTracksGeo(@Query("country") String country);

    // tag
    @GET("tag.getTopAlbums")
    Observable<Map<String, Albums>> getTopAlbumsByTag(@Query("tag") String tag,
                                                @Query("limit") int limit,
                                                @Query("page") int page);
    @GET("tag.getTopTracks")
    Observable<Map<String, Tracks>> getTopTracksByTag(@Query("tag") String tag,
                                               @Query("limit") int limit,
                                               @Query("page") int page);

    @GET("tag.getTopTags")
    Observable<Map<String, Tags>> getTopTags();
}
