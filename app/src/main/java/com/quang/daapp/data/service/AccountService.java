package com.quang.daapp.data.service;

import com.quang.daapp.data.model.Customer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountService {

    @POST("/account/login")
    Call<String> login(@Field("email") String email, @Field("password") String password);

    @POST("/account/cus")
    Call<Number> registerCustomer(@Body Customer infor);

    @GET("/account/cus")
    Call<Number> registerCustomer();
}
