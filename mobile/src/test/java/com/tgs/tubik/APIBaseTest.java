package com.tgs.tubik;

import com.tgs.tubik.api.app.APITubik;
import com.tgs.tubik.api.lastfm.model.Error;
import com.tgs.tubik.order.OrderedRunner;

import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
import static org.junit.Assert.fail;

@RunWith(OrderedRunner.class)
public class APIBaseTest {
    Cache cache;
    Retrofit retrofit;

    APITubik mApiTubik;
    Retrofit retrofitTubik;

    final int CONNECTION_TIMEOUT = 15;

    public APIBaseTest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient httpClient = builder.build();

        retrofitTubik = new Retrofit.Builder()
                .baseUrl(APITubik.URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApiTubik = retrofitTubik.create(APITubik.class);
    }

    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(System.out::println);
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return httpLoggingInterceptor;
    }

    void checkFail(Throwable e) {
        Error err = Error.parse(retrofit, e);
        if (err.getMessage() != null)
            fail(err.getMessage());
        else
            fail(e.getMessage());
    }

    void checkAPITubikFail(Throwable e) {
        Error err = Error.parse(retrofitTubik, e);
        if (err.getMessage() != null)
            fail(err.getMessage());
        else
            fail(e.getMessage());
    }
}
