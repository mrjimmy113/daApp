package com.quang.daapp.data.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.137.1:8080";



    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
