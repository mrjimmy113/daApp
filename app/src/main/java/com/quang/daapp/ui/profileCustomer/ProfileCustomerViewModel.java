package com.quang.daapp.ui.profileCustomer;

import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.repository.AccountRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileCustomerViewModel extends ViewModel {

    private MutableLiveData<Customer> customerMutableLiveData;

    private AccountRepository accountRepository;

    public LiveData<Customer> getProfileResult() {return customerMutableLiveData;}

    public ProfileCustomerViewModel() {
        accountRepository = AccountRepository.getInstance();
    }

    public void getProfile(String token) {
        customerMutableLiveData = accountRepository.getProfile(token);
    }


}