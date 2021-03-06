package com.quang.daapp.data.repository;

import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.service.ChatMessageService;
import com.quang.daapp.data.service.MajorService;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.ultis.NetworkClient;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessageRepository {

    private static ChatMessageRepository instance;
    private ChatMessageService service;

    public static ChatMessageRepository getInstance() {
        if(instance == null) {
            instance = new ChatMessageRepository();
        }
        return instance;
    }

    public MutableLiveData<List<ReceiveMessage>> getMessages(int requestId, int page) {
        CreateService();
        final  MutableLiveData<List<ReceiveMessage>> mutableLiveData = new MutableLiveData<>();
        service.getMessages(requestId, page).enqueue(new MyRequestCallBack<>(mutableLiveData));
        return mutableLiveData;
    }


    private void CreateService() {
        if(service == null) {
            service = NetworkClient.getInstance().getRetrofitInstance().create(ChatMessageService.class);
        }
    }
}
