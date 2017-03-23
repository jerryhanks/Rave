package com.flutterwave.rave.service;


import com.flutterwave.rave.RaveDialogConfig;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Shittu on 19/12/2016.
 */

public class RaveRestClient {
    private static final String BASE_URL = RaveDialogConfig.BASE_URL;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .build();
        return getClient().newCall(request).execute();
    }

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return getClient().newCall(request).execute();
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (!RaveDialogConfig.isProduction()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(logging);

        return builder.build();

    }

}
