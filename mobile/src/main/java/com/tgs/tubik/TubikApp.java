package com.tgs.tubik;

import android.app.Application;
import android.os.Environment;

import com.tgs.tubik.api.interceptor.CacheInterceptor;
import com.tgs.tubik.api.lastfm.APILastFM;
import com.tgs.tubik.api.lastfm.interceptor.LastFMKeyInterceptor;
import com.tgs.tubik.tools.GlobalErrorHandler;
import com.tgs.tubik.tools.RxThreadingCallAdapterFactory;
import com.tgs.tubik.tools.logger.Log;
import com.tgs.tubik.tools.logger.LogWrapper;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TubikApp extends Application {

    private static final String TAG = "Tubik Application Instance";

    private APILastFM mAPILastFM;
    private OkHttpClient httpClientToLastFM;
    private Cache cache;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new GlobalErrorHandler(this));
        //testUncaughtExceptionHandler();

        initializeLogging();
        initAPILastFM();
    }

    public void testUncaughtExceptionHandler() {
        try {
            Thread testThread = new Thread() {
                public void run()
                {
                    throw new RuntimeException("Expected!");
                }
            };

            testThread.start();
            testThread.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Initialize Logging! Ready!");
    }

    private void initAPILastFM() {
        httpClientToLastFM = new OkHttpClient();

        cache = new Cache(new File(getCacheDir(), "cache-lastfm"), 50 * 1024 * 1024);

        if (BuildConfig.DEBUG) {
            try {
                cache.evictAll();
            } catch (IOException e) {
                Log.e("On remove previous lastfm cache", e.getMessage());
            }
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new LastFMKeyInterceptor())
                .addNetworkInterceptor(new CacheInterceptor(this))
                .cache(cache);

        httpClientToLastFM = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APILastFM.URL)
                .client(httpClientToLastFM)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxThreadingCallAdapterFactory.create())
                .build();

        mAPILastFM = retrofit.create(APILastFM.class);
    }

    public APILastFM getAPILastFM() {
        return mAPILastFM;
    }

    public File getHomePath() {
        File result = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                getString(R.string.app_path));

        if (result.exists()) return result;
        if (result.mkdirs()) return result;
        Log.e(TAG, "Can not create application path! Check write permission!");

        result = new File(Environment.getDownloadCacheDirectory() + File.separator +
                getString(R.string.app_path));

        if (result.exists()) return result;
        if (result.mkdirs()) return result;
        Log.e(TAG, String.format(Locale.US, "Can not create application path in DOWNLOAD CACHE!!! WTF!!! %s", result.getAbsolutePath()));

        Log.i(TAG, String.format(Locale.US, "So, use default cache directory!!! %s", getCacheDir().getAbsolutePath()));

        return getCacheDir();
    }

    public String getHomePath(String chain) {
        return getHomePath().getAbsolutePath() + File.separator  + chain;
    }
}