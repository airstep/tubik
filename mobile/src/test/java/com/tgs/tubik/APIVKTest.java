package com.tgs.tubik;

import com.tgs.tubik.api.vk.APIVK;
import com.tgs.tubik.api.vk.model.Token;
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

public class APIVKTest extends APIBaseTest {
    private VKConfig vkConfig;

    private APIVK mApi;
    private static Token mToken;

    private class VKConfig {
        String username = "username";
        String password = "password";
    }

    public APIVKTest() throws IOException {
        if (cache == null) {
            cache = new Cache(new File("./build/tmp/test-ok-cache"), 20 * 1024 * 1024);
            cache.evictAll();
        }
        if (config == null) {
            config = new Config(new VKConfig());
            vkConfig = (VKConfig) config.get();
        }
    }

    @Before
    public void setUp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache);

        OkHttpClient httpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(APIVK.AUTH_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = retrofit.create(APIVK.class);
    }


    @Test
    @Order(order = 1)
    public void auth() {
        mApi.getToken(vkConfig.username, vkConfig.password)
                .subscribe(response -> {
                    mToken = response.body();
                    assertNotNull(mToken);
                });
    }
}
