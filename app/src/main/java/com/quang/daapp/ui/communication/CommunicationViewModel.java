package com.quang.daapp.ui.communication;

import com.quang.daapp.data.model.Expert;
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
    private MutableLiveData<Expert> expertResult = new MutableLiveData<>();
    private ChatMessageRepository repository;
    private ProblemRequestRepository requestRepository;

    public CommunicationViewModel() {
        repository = ChatMessageRepository.getInstance();
        requestRepository = ProblemRequestRepository.getInstance();

    }

    void getExpert(int requestId) {
        expertResult = requestRepository.getExpertProfile(requestId);
    }

    LiveData<Expert> getExpertResult() {
        return expertResult;
    }

    void getChatMessage(int requestId, int page) {
        chatMessageResult = repository.getMessages(requestId, page);
    }

    LiveData<List<ReceiveMessage>> getChatMessageResult() {
        return chatMessageResult;
    }

    void getDetail(int requestId) {
        detailResult = requestRepository.getRequestDetailById(requestId);
    }

    LiveData<ProblemRequestDetail> getDetailResult() {
        return detailResult;
    }

}
