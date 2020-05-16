package com.quang.daapp.data.repository;

import android.util.Log;

import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.service.AccountService;
import com.quang.daapp.data.service.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class AccountRepository {

    private static volatile AccountRepository instance;
    private AccountService service;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private Object user = null;

    // private constructor : singleton access
    private AccountRepository() {

    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;

    }

    private void setLoggedInUser(Object user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public  String test() throws IOException {
        return "a";
    }

    public Object login(String username, String password) {
        // handle login
        Object result = null;

        return result;
    }

    public void registerCustomer(Customer customer) {
        CreateService();
        service.registerCustomer(customer).enqueue(new Callback<Number>() {
            @Override
            public void onResponse(Call<Number> call, Response<Number> response) {
                Log.e("Hello", response.code() + "");
            }

            @Override
            public void onFailure(Call<Number> call, Throwable t) {
                Log.e("Error:", t.getMessage());
            }
        });
    }

    private void CreateService() {
        if(service == null) {
            service = RetrofitClient.getRetrofitInstance().create(AccountService.class);
        }
    }
}
