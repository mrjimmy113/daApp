package com.quang.daapp.data.repository;

import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.service.MajorService;
import com.quang.daapp.ultis.NetworkClient;

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
        service.getAllMajor().enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    private void CreateService() {
        if(service == null) {
            service = NetworkClient.getInstance().getRetrofitInstance().create(MajorService.class);
        }
    }
}
