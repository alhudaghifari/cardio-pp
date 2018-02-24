package com.urbannightdev.cardiopp.helper.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbannightdev.cardiopp.helper.API.AppLink;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ghifar on 24/02/18.
 */

public class RetrofitClientUtils {
    static Retrofit retrofit = null;

    public static Retrofit client() {
        Gson gson = new GsonBuilder().
                setLenient().create();
        HttpLoggingInterceptor logging =
                new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(logging);

        retrofit = new Retrofit.Builder().
                baseUrl(AppLink.SERVER_LINK).
                client(okHttpClient.build()).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
        return retrofit;
    }
}
