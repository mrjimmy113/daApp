package com.quang.daapp.data.service;

import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.ProblemRequestDetail;

import java.io.File;
import java.sql.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProblemRequestService {
    @Multipart
    @POST("/request")
    Call<Number> createNewRequest(@Part MultipartBody.Part[] files, @Part("endDate") RequestBody date,
                                  @Part("title") RequestBody title, @Part("description") RequestBody description,
                                  @Part("majorId") int id
                                  );

    @GET("/request")
    Call<List<ProblemRequest>> getCurrentProblemRequest();

    @GET("/request/detail")
    Call<ProblemRequestDetail> getRequestDetail(@Query("id") int requestId);

    @GET("/request/search")
    Call<List<ProblemRequest>> expertSearch(@Query("major") int majorId, @Query("city") String city,
                                            @Query("language") String language, @Query("time") int time);
}
