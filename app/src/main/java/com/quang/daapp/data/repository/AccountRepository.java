package com.quang.daapp.data.repository;

import android.util.Log;

import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.service.AccountService;
import com.quang.daapp.data.service.RetrofitClient;
import com.quang.daapp.ultis.AuthTokenManager;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;
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

    private AccountRepository() {

    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    public MutableLiveData<Customer> getProfile(String token) {
        CreateService();
        final MutableLiveData<Customer> result = new MutableLiveData<>();
        service.getCustomerProfile(token).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });

        return result;
    }


    public MutableLiveData<String> login(String username, String password) {
        // handle login
        CreateService();
        final MutableLiveData<String> result = new MutableLiveData<>();
        service.login(username,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });

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
