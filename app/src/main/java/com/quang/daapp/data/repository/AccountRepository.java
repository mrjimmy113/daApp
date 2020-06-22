package com.quang.daapp.data.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.service.AccountService;
import com.quang.daapp.ultis.NetworkClient;

import java.io.File;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        service.getCustomerProfile(token).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    public MutableLiveData<Number> changePassword(String token, String currentPassword, String newPassword) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        service.changePassword(token,currentPassword,newPassword).enqueue(new MyRequestCallBack<>(result));
        return result;
    }

    public MutableLiveData<String> login(String username, String password) {
        // handle login
        CreateService();
        final MutableLiveData<String> result = new MutableLiveData<>();
        service.login(username,password).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    public MutableLiveData<Number> registerCustomer(Customer model) {
        CreateService();
        final  MutableLiveData<Number> result = new MutableLiveData<>();
        service.registerCustomer(model).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    public MutableLiveData<Number> registerExpert(Expert model) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        service.registerExpert(model).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    public  MutableLiveData<Number> update(String filePath, Customer customer) {
        CreateService();
        final  MutableLiveData<Number> result = new MutableLiveData<>();
        MultipartBody.Part f = null;
        if(filePath != null) {
            File file = new File(filePath);
            RequestBody filePart = RequestBody.create(MediaType.parse("image/jpeg"),file);
            f = MultipartBody.Part.createFormData("file", file.getName(),filePart);
        }
        Gson gson = NetworkClient.getInstance().getGson();

        service.updateCustomer(f,RequestBody.create(MediaType.parse("application/json"), gson.toJson(customer))).enqueue(new MyRequestCallBack<>(result));
        return  result;
    }

    public  MutableLiveData<Number> updateExpert(String filePath, Expert expert) {
        CreateService();
        final  MutableLiveData<Number> result = new MutableLiveData<>();
        MultipartBody.Part f = null;
        if(filePath != null) {
            File file = new File(filePath);
            RequestBody filePart = RequestBody.create(MediaType.parse("image/jpeg"),file);
            f = MultipartBody.Part.createFormData("file", file.getName(),filePart);
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd")
                .create();

        service.updateExpert(f,RequestBody.create(MediaType.parse("application/json"), gson.toJson(expert))).enqueue(new MyRequestCallBack<>(result));

        return  result;
    }

    public MutableLiveData<Expert> getExpertProfile() {
        CreateService();
        final MutableLiveData<Expert> result = new MutableLiveData<>();
        service.getExpertProfile().enqueue(new MyRequestCallBack<>(result));
        return  result;
    }

    public MutableLiveData<Number> forgetPassword(String email) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        service.forgetPassword(email).enqueue(new MyRequestCallBack<>(result));
        return result;
    }

    private void CreateService() {
        if(service == null) {
            service = NetworkClient.getInstance().getRetrofitInstance().create(AccountService.class);
        }
    }
}
