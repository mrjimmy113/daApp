package com.quang.daapp.ui.customerProfileEdit;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.repository.AccountRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditCustomerProfileViewModel  extends ViewModel {
    private MutableLiveData<Number> editResult;
    private AccountRepository repository;

    public EditCustomerProfileViewModel() {
        repository = AccountRepository.getInstance();
    }

    public LiveData<Number> getEditResult() {
        return  editResult;
    }

    public void editCustomer(String filePath, Customer customer) {
        editResult = repository.update(filePath,customer);

    }

    public EditCustomerProfileFormState getFormState(String fullName, String address) {
        EditCustomerProfileFormState state = new EditCustomerProfileFormState();
        boolean isValid = true;
        if(fullName.trim().isEmpty()) {
            state.setFullNameError(R.string.invalid_fullName);
            isValid = false;
        }
        if(address.trim().isEmpty()) {
            state.setAddressError(R.string.invalid_address);
            isValid = false;
        }

        state.setDataValid(isValid);

        return state;
    }
}
