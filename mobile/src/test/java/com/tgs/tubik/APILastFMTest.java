package com.tgs.tubik;

import com.tgs.tubik.api.interceptor.CacheInterceptor;
import com.tgs.tubik.api.lastfm.APILastFM;
import com.tgs.tubik.api.lastfm.interceptor.LastFMKeyInterceptor;
import com.tgs.tubik.api.lastfm.model.Error;
import com.tgs.tubik.api.lastfm.model.Session;
import com.tgs.tubik.api.lastfm.model.album.Albums;
import com.tgs.tubik.api.lastfm.model.tag.Tags;
import com.tgs.tubik.api.lastfm.model.track.Tracks;
import com.tgs.tubik.order.Order;
import com.tgs.tubik.order.OrderedRunner;
import com.tgs.tubik.tools.StringUtilities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(OrderedRunner.class)
public class APILastFMTest {
    private Cache cache;

    private APILastFM mApi;
    private static Session mSession;
    private Retrofit retrofit;

    public APILastFMTest() throws IOException {
        if (cache == null) {
            cache = new Cache(new File("./build/tmp/test-ok-cache"), 20 * 1024 * 1024);
            cache.evictAll();
        }
    }

    @Before
    public void setUp() {
        int CONNECTION_TIMEOUT = 15;

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new LastFMKeyInterceptor())
                .addInterceptor(provideHttpLoggingInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache);

        OkHttpClient httpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(APILastFM.URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        mApi = retrofit.create(APILastFM.class);
    }

    @Test
    @Order(order = 1)
    public void getToken() {
        mApi.getToken()
                .subscribe(
                        Assert::assertNotNull,
                        this::checkFail,
                        this::checkCache
                );
    }

    @Test
    @Order(order = 2)
    public void getMobileSession() {
        String username = "backneomind";
        String password = "du7zooR3";

        if (!StringUtilities.isMD5(password))
            password = StringUtilities.md5(password);
        String authToken = StringUtilities.md5(username + password);
        Map<String, String> params = StringUtilities.map("api_key", APILastFM.API_KEY, "username", username, "authToken", authToken);
        String sig = createSignature("auth.getMobileSession", params, APILastFM.SECRET_KEY);

        mApi.getMobileSession(username, authToken, sig)
                .subscribe(
                        response -> {
                            mSession = response.get(Session.ROOT);
                            assertNotNull(mSession);
                            assertTrue(mSession.getName().length() > 0);
                            assertTrue(mSession.getKey().length() > 0);
                        },
                        this::checkFail,
                        this::checkCache
                );
    }


    @Test
    @Order(order = 3)
    public void getTopTracksGeo() {
        mApi.getTopTracksGeo("ukraine").subscribe(
                response -> {
                    Tracks tracks = response.get(Tracks.ROOT);
                    assertNotNull(tracks);
                    assertTrue(tracks.getList().size() > 0);
                    assertTrue(tracks.getAttr().getTotal() > 0);
                },
                this::checkFail
        );
    }

    @Test
    @Order(order = 5)
    public void getTopAlbumsTag() {
        mApi.getTopAlbumsByTag("rap", 100, 1)
                .subscribe(
                        response -> {
                            Albums albums = response.get(Albums.ROOT);
                            assertNotNull(albums);
                            assertTrue(albums.getList().size() > 0);
                        },
                        throwable -> fail(throwable.getMessage()),
                        this::checkCache
                );
    }

    @Test
    @Order(order = 5)
    public void getTopTracksByTag() {
        mApi.getTopTracksByTag("rap", 100, 1)
                .subscribe(
                        response -> {
                            Tracks albums = response.get(Tracks.ROOT);
                            assertNotNull(albums);
                            assertTrue(albums.getList().size() > 0);
                        },
                        this::checkFail,
                        this::checkCache
                );
    }

    @Test
    @Order(order = 6)
    public void getTopTags() {
        mApi.getTopTags()
                .subscribe(
                        response -> {
                            Tags tags = response.get(Tags.ROOT);
                            assertNotNull(tags);
                            assertTrue(tags.getList().size() > 0);
                        }
                );
    }

    private void checkCache() {
        try {
            assertTrue(cache.size() > 0);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    // helpers

    private String createSignature(String method, Map<String, String> params, String secret) {
        params = new TreeMap<>(params);
        params.put("method", method);
        StringBuilder b = new StringBuilder(100);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            b.append(entry.getKey());
            b.append(entry.getValue());
        }
        b.append(secret);
        return StringUtilities.md5(b.toString());
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(System.out::println);
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return httpLoggingInterceptor;
    }

    private void checkFail(Throwable e) {
        Error err = Error.parse(retrofit, e);
        if (err.getMessage() != null)
            fail(err.getMessage());
        else
            fail(e.getMessage());
    }
}