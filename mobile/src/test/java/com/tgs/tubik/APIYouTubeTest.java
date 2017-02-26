package com.tgs.tubik;

import com.tgs.tubik.api.youtube.APIYouTube;
import com.tgs.tubik.api.youtube.interceptor.YoutubeInterceptor;
import com.tgs.tubik.api.youtube.model.Profile;
import com.tgs.tubik.api.youtube.model.Content;
import com.tgs.tubik.order.Order;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Before Run/Debug/Tests,
// you need to go to 'server' folder (in the root)
// and run -> npm i
// then start web server by -> npm start
// in browser go to http://localhost:8080/youtube/authenticate
// and pass authorization request
public class APIYouTubeTest extends APIBaseTest {
    private APIYouTube mApi;
    private static Profile mProfile;
    private static YoutubeInterceptor youtubeInterceptor;

    public APIYouTubeTest() throws IOException {
        if (cache == null) {
            cache = new Cache(new File("./build/tmp/test-ok-cache"), 20 * 1024 * 1024);
            cache.evictAll();
        }
        if (youtubeInterceptor == null)
            youtubeInterceptor = new YoutubeInterceptor();
    }

    @Before
    public void setUp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(youtubeInterceptor)
                .addInterceptor(provideHttpLoggingInterceptor())
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache);

        OkHttpClient httpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(APIYouTube.URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = retrofit.create(APIYouTube.class);
    }

    @Test
    @Order(order = 1)
    public void auth() {
        mApiTubik.getYoutubeToken()
                .subscribe(response -> {
                    mProfile = response.body();
                    assertNotNull(mProfile);

                    String token = mProfile.getAccessToken();
                    assertNotNull(token);

                    youtubeInterceptor.setAccessToken(token);
                });
    }

    @Test
    @Order(order = 2)
    public void search() {
        mApi.search("master")
                .subscribe(response -> {
                    Content data = response.body();
                    assertNotNull(data);
                    assertTrue(data.getItems().size() > 0);
                });
    }

    @Test
    @Order(order = 3)
    public void recommended() {
        mApi.getRecommended()
                .subscribe(response -> {
                    Content data = response.body();
                    assertNotNull(data);
                    assertTrue(data.getItems().size() > 0);
                });
    }

    @Test
    @Order(order = 4)
    public void popular() {
        mApi.getMostPopular("US")
                .subscribe(response -> {
                    Content data = response.body();
                    assertNotNull(data);
                    assertTrue(data.getItems().size() > 0);
                });
    }

}
