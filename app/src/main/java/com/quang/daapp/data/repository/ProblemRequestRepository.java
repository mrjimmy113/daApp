package com.quang.daapp.data.repository;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.data.service.ProblemRequestService;
import com.quang.daapp.ultis.NetworkClient;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProblemRequestRepository {

    private static volatile ProblemRequestRepository instance;
    private ProblemRequestService service;

    private ProblemRequestRepository() {

    }

    public static ProblemRequestRepository getInstance() {
        if (instance == null) {
            instance = new ProblemRequestRepository();
        }
        return instance;
    }

    public  MutableLiveData<ProblemRequestDetail> getRequestDetailById(int id) {
        CreateService();
        final MutableLiveData<ProblemRequestDetail> result = new MutableLiveData<>();
        service.getRequestDetail(id).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    public  MutableLiveData<List<ProblemRequest>> getCurrentUserRequest() {
        CreateService();
        final MutableLiveData<List<ProblemRequest>> result = new MutableLiveData<>();
        service.getCurrentProblemRequest().enqueue(new MyRequestCallBack<>(result));

        return  result;
    }

    public  MutableLiveData<List<ProblemRequest>> getCurrentUserAppliedRequest() {
        CreateService();
        final MutableLiveData<List<ProblemRequest>> result = new MutableLiveData<>();
        service.getCurrentAppliedProblemRequest().enqueue(new MyRequestCallBack<>(result));

        return  result;
    }

    public  MutableLiveData<List<ProblemRequest>> getCurrentUserRequestWithStatus(StatusEnum[] statusEnum) {
        CreateService();
        final MutableLiveData<List<ProblemRequest>> result = new MutableLiveData<>();
        service.getCurrentProblemRequestWithStatus(statusEnum).enqueue(new MyRequestCallBack<>(result));
        return  result;
    }

    public MutableLiveData<Number> createNewRequest(String[] files, Date endDate, String title, String description, int id) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        service.createNewRequest(getMultipartBodyPart(files),
                RequestBody.create(MediaType.parse("text/plain"),(new SimpleDateFormat("yyyy-MM-dd")).format(endDate).toString()),
                RequestBody.create(MediaType.parse("text/plain"),title),
                RequestBody.create(MediaType.parse("text/plain"),description),
                id
                ).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    public MutableLiveData<Number> updateRequest(String[] files, int requestId, Date endDate, String title, String description, int id, ArrayList<String> delImgs) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        Gson gson = new GsonBuilder().create();
        service.updateRequest(getMultipartBodyPart(files),requestId,
                RequestBody.create(MediaType.parse("text/plain"),(new SimpleDateFormat("yyyy-MM-dd")).format(endDate).toString()),
                RequestBody.create(MediaType.parse("text/plain"),title),
                RequestBody.create(MediaType.parse("text/plain"),description),
                id, RequestBody.create(MediaType.parse("text/plain"),gson.toJson(delImgs))

        ).enqueue(new MyRequestCallBack<>(result));

        return result;
    }


    public MutableLiveData<List<ProblemRequest>> expertSearch(int major, String city, String language, int time) {
        CreateService();
        final MutableLiveData<List<ProblemRequest>> result = new MutableLiveData<>();
        service.expertSearch(major, city, language, time).enqueue(new MyRequestCallBack<>(result));
        return result;
    }

    public MutableLiveData<Number> expertApply(int requestId) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        service.expertApply(requestId).enqueue(new MyRequestCallBack<>(result));
        return  result;
    }

    public MutableLiveData<List<Expert>> getApplicant(int requestId) {
        CreateService();
        final MutableLiveData<List<Expert>> result = new MutableLiveData<>();
        service.getApplicants(requestId).enqueue(new MyRequestCallBack<>(result));
        return result;
    }

    public MutableLiveData<Number> acceptExpert(int requestId, int expertId) {
        CreateService();
        final MutableLiveData<Number> result = new MutableLiveData<>();
        service.acceptExpert(requestId,expertId).enqueue(new MyRequestCallBack<>(result));
        return result;
    }

    public MutableLiveData<Customer> getCustomerProfile(int requestId) {
        CreateService();
        final MutableLiveData<Customer> result = new MutableLiveData<>();
        service.getCustomerProfile(requestId).enqueue(new MyRequestCallBack<>(result));
        return result;
    }

    public MutableLiveData<Expert> getExpertProfile(int requestId) {
        CreateService();
        final MutableLiveData<Expert> result = new MutableLiveData<>();
        service.getExpertProfile(requestId).enqueue(new MyRequestCallBack<>(result));

        return result;
    }

    private void CreateService() {
        if(service == null) {
            service = NetworkClient.getInstance().getRetrofitInstance().create(ProblemRequestService.class);
        }

    }

    private MultipartBody.Part[] getMultipartBodyPart(String[] sources) {
        MultipartBody.Part[] result = new MultipartBody.Part[sources.length];
        for (int i = 0 ; i < result.length;i++) {
            File file = new File(sources[i]);
            RequestBody filePart = RequestBody.create(MediaType.parse("image/jpeg"),file);
            MultipartBody.Part f = MultipartBody.Part.createFormData("files", file.getName(),filePart);
            result[i] = f;
        }
        return result;
    }

    private RequestBody[] arrStringtoResponseBodyArr(String[] strings) {
        RequestBody[] result = new RequestBody[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = RequestBody.create(MediaType.parse("image/jpeg"),strings[i]);
        }
        return result;
    }




}
