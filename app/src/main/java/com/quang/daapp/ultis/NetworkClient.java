package com.quang.daapp.ultis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkClient {
    private static Retrofit retrofit;

    public static final String BASE_URL = "http://192.168.42.30:8080";

    public static final String IMG_URL = "/request/image?imgName=";
    private static String token = "";

    public static void setToken(String token) {
        NetworkClient.token = token;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                                          @Override
                                          public Response intercept(Interceptor.Chain chain) throws IOException {
                                              Request original = chain.request();

                                              Request request = original.newBuilder()
                                                      .header("authorization", token)
                                                      .method(original.method(), original.body())
                                                      .build();

                                              return chain.proceed(request);
                                          }
                                      });
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd")
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static String getImageUrl(String imageName) {
        return BASE_URL + IMG_URL + imageName;
    }





}
