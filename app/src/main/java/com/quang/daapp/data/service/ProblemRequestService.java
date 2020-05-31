package com.quang.daapp.data.service;

import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.model.StatusEnum;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
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
import retrofit2.http.PUT;
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

    @Multipart
    @PUT("/request")
    Call<Number> updateRequest(@Part MultipartBody.Part[] files,
                               @Part("requestId") int requestId,
                               @Part("endDate") RequestBody date,
                                  @Part("title") RequestBody title, @Part("description") RequestBody description,
                                  @Part("majorId") int id,@Part("delImgs") RequestBody delImgs
    );


    @GET("/request")
    Call<List<ProblemRequest>> getCurrentProblemRequest();

    @GET("/request/applied")
    Call<List<ProblemRequest>> getCurrentAppliedProblemRequest();

    @GET("/request/status")
    Call<List<ProblemRequest>> getCurrentProblemRequestWithStatus(@Query("status")StatusEnum statusEnum);

    @GET("/request/detail")
    Call<ProblemRequestDetail> getRequestDetail(@Query("id") int requestId);

    @GET("/request/search")
    Call<List<ProblemRequest>> expertSearch(@Query("major") int majorId, @Query("city") String city,
                                            @Query("language") String language, @Query("time") int time);

    @FormUrlEncoded
    @POST("/request/apply")
    Call<Number> expertApply(@Field("requestId") int requestId);

    @GET("/request/applicant")
    Call<List<Expert>> getApplicants(@Query("requestId") int requestId);

    @FormUrlEncoded
    @PUT("/request/accept")
    Call<Number> acceptExpert(@Field("requestId") int requestId, @Field("expertId") int expertId);
}
