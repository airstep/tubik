package com.tgs.tubik;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tgs.tubik.api.app.APITubik;
import com.tgs.tubik.api.lastfm.model.Error;
import com.tgs.tubik.order.OrderedRunner;
import com.tgs.tubik.tools.logger.Log;

import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

    Config config;

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

    class Config {
        private File mConfigFile;
        private Object mTarget;

        @TargetApi(Build.VERSION_CODES.KITKAT)
        Config(Object target) throws IOException {
            String name = target.getClass().getSimpleName();
            mTarget = target;

            File configFolder = new File("./config");
            if (!configFolder.exists()) {
                if (configFolder.mkdirs()) {
                    Log.d(Config.class.getSimpleName(), "VK config folder created!");
                } else {
                    Log.e(Config.class.getSimpleName(), "Can not create VK config folder!");
                }
            }
            mConfigFile = new File("./config/" + name + ".json");
            if (!mConfigFile.exists()) {
                try {
                    if (mConfigFile.createNewFile()) {
                        Log.d(Config.class.getSimpleName(), "VK config file created. Please fill it!");
                    } else {
                        Log.e(Config.class.getSimpleName(), "Can not create VK config file!");
                    }
                    try (Writer writer = new FileWriter(mConfigFile)) {
                        Gson gson = new GsonBuilder().create();
                        gson.toJson(target, writer);
                    }
                    throw new IOException("Please fill config file with valid data: " + mConfigFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        Object get() {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(mConfigFile)) {
                return gson.fromJson(reader, mTarget.getClass());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
