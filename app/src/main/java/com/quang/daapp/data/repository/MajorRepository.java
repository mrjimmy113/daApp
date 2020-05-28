package com.quang.daapp.data.repository;

import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.service.MajorService;
import com.quang.daapp.data.service.RetrofitClient;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MajorRepository {

    private static volatile MajorRepository instance;
    private MajorService service;

    public static MajorRepository getInstance() {
        if(instance == null) {
            instance = new MajorRepository();
        }

        return instance;
    }

    public MutableLiveData<List<Major>> getAllMajor() {
        CreateService();
        final MutableLiveData<List<Major>> result = new MutableLiveData<>();
        service.getAllMajor().enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {

            }
        });

        return result;
    }

    private void CreateService() {
        if(service == null) {
            service = RetrofitClient.getRetrofitInstance().create(MajorService.class);
        }
    }
}
