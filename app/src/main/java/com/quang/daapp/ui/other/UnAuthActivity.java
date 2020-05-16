package com.quang.daapp.ui.other;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.service.AccountService;
import com.quang.daapp.data.service.RetrofitClient;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

public class UnAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_auth);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        Customer c = new Customer("hhhhh","111111","hhhhh","hhhhhh","hhhhh","hhhhh","1997-01-01","hhhh");
        RetrofitClient.getRetrofitInstance().create(AccountService.class).registerCustomer(c).enqueue(new Callback<Number>() {
            @Override
            public void onResponse(Call<Number> call, Response<Number> response) {
                Log.e("H:", response.code() + "");
            }

            @Override
            public void onFailure(Call<Number> call, Throwable t) {
                Log.e("H:", t.getMessage()+ "");
            }
        });



    }
}
