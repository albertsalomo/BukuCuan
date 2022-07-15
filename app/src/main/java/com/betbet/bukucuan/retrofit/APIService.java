package com.betbet.bukucuan.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {

    // Wifi Config

    // XL Quota
    // private final static String BASE_URL = "http://bukucuan.infinityfreeapp.com/BukuCuan/";

    // Home Wifi
    private final static String BASE_URL = "http://192.168.0.12/BukuCuan/";

    public static APIEndPoint endPoint () {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okeHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okeHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create((APIEndPoint.class));
    }
}
