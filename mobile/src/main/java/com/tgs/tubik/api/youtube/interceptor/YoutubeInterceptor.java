package com.tgs.tubik.api.youtube.interceptor;

import com.tgs.tubik.api.youtube.APIYouTube;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeInterceptor implements Interceptor {
    private String accessToken;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl.Builder builder = original.url().newBuilder();

        builder.addQueryParameter("key", APIYouTube.KEY);

        if (accessToken != null)
            builder.addQueryParameter("access_token", accessToken);

        HttpUrl url = builder.build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public void setAccessToken(String value) {
        accessToken = value;
    }
}
