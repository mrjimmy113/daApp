package com.quang.daapp.ui.communication;

import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.repository.ChatMessageRepository;
import com.quang.daapp.data.repository.ProblemRequestRepository;
import com.quang.daapp.stomp.ReceiveMessage;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunicationViewModel extends ViewModel {

    private MutableLiveData<List<ReceiveMessage>> chatMessageResult = new MutableLiveData<>();
    private MutableLiveData<ProblemRequestDetail> detailResult = new MutableLiveData<>();
    private ChatMessageRepository repository;
    private ProblemRequestRepository requestRepository;

    public CommunicationViewModel() {
        repository = ChatMessageRepository.getInstance();
        requestRepository = ProblemRequestRepository.getInstance();
    }

    public void getChatMessage(int requestId) {
        chatMessageResult = repository.getMessages(requestId);
    }

    public LiveData<List<ReceiveMessage>> getChatMessageResult() {
        return chatMessageResult;
    }

    public void getDetail(int requestId) {
        detailResult = requestRepository.getRequestDetailById(requestId);
    }

    public LiveData<ProblemRequestDetail> getDetailResult() {
        return detailResult;
    }

}
