package com.quang.daapp.data.repository;

import android.util.Log;

import com.quang.daapp.R;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.DialogManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRequestCallBack<T> implements Callback<T> {

    private MutableLiveData<T> mutableLiveData;

    MyRequestCallBack(MutableLiveData<T> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        mutableLiveData.postValue(response.body());
    }

    @Override
    public void onFailure(@NonNull Call<T> call, Throwable t) {
        mutableLiveData.postValue(null);
        if(t instanceof ConnectException || t instanceof SocketTimeoutException) {
            MessageDialogFragment dialogTimeOut = new MessageDialogFragment(
                    "Can not connect to server, please check your connection"
                    , R.color.colorDanger, R.drawable.ic_error
            );
            DialogManager.getInstance().showDialog(dialogTimeOut,true);
        }

    }
}
