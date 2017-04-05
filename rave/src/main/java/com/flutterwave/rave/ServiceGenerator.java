package com.flutterwave.rave;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by @Po10cio on 29/03/2017 for WPAndroid.
 */

public class ServiceGenerator {

    private static final String TAG = ServiceGenerator.class.getSimpleName();
    private static String BASE_URL = Rave.getBaseUrl();

    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    //get the retrofit retrofitBuilder
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    //get the http client
    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    public static <S> S createService(Class<S> serviceClass) {
        //set the login Level
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(loggingInterceptor);
        httpClientBuilder.cache(provideCache());

        //add the client to the retrofit
        OkHttpClient okhttpClient = httpClientBuilder.build();
        retrofitBuilder.client(okhttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(Rave.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); //10mb
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Could Not create cache");
        }
        return cache;
    }
}
