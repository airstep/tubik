package com.tgs.tubik.api.lastfm.interceptor;

import com.tgs.tubik.api.lastfm.APILastFM;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LastFMKeyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl currentURL = original.url();
        String method = currentURL.pathSegments().get(1);

        HttpUrl originalHttpUrl = HttpUrl.parse(APILastFM.URL);
        HttpUrl.Builder builder = originalHttpUrl.newBuilder()
                .addQueryParameter("method", method)
                .addQueryParameter("api_key", APILastFM.API_KEY)
                .addQueryParameter("format", "json");

        for (String name: currentURL.queryParameterNames())
            builder.addQueryParameter(name, currentURL.queryParameter(name));

        HttpUrl url = builder.build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
