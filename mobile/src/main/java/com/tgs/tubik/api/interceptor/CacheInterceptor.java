package com.tgs.tubik.api.interceptor;

import android.content.Context;

import com.tgs.tubik.BuildConfig;
import com.tgs.tubik.tools.Tools;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    private Context context;
    private boolean isOnline;

    public CacheInterceptor() {}

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (isOnline || (context != null && Tools.isOnline(context)))
            return chain.proceed(request);

        CacheControl cacheControl = new CacheControl.Builder()
                .maxStale(1, BuildConfig.DEBUG ? TimeUnit.MINUTES : TimeUnit.HOURS)
                .onlyIfCached()
                .build();

        request = request.newBuilder()
                .cacheControl(cacheControl)
                .build();

        return chain.proceed(request);
    }

    public void setOnline(boolean value) {
        isOnline = value;
    }
}