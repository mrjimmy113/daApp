package com.quang.daapp.data.service;

import com.quang.daapp.data.model.Customer;
import com.quang.daapp.stomp.ReceiveMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ChatMessageService {

    @GET("chatMessage")
    Call<List<ReceiveMessage>> getMessages(@Query("requestId") int requestId, @Query("page") int page);
}
