package com.quang.daapp.data.service;

import com.quang.daapp.data.model.Major;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MajorService {

    @GET("major")
    Call<List<Major>> getAllMajor();
}
