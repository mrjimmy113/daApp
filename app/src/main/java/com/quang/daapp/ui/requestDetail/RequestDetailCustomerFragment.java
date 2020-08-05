package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.ultis.NetworkClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailCustomerFragment extends Fragment {

    private ImageView ivAvatar;
    private TextView txtFullName;
    private TextView txtEmail;
    private TextView txtCity;
    private TextView txtAddress;
    private TextView txtDob;
    private TextView txtLanguage;

    private Customer customer;
    private boolean isViewCreated = false;

    public RequestDetailCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtCity = view.findViewById(R.id.txtCity);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtDob = view.findViewById(R.id.txtDob);
        txtLanguage = view.findViewById(R.id.txtLanguage);
        txtEmail = view.findViewById(R.id.txtEmail);
        if(customer != null) {
            setData();
        }
        isViewCreated = true;
    }

    private void setData() {

        Picasso.get().load(NetworkClient.getImageUrl(customer.getImgName())).into(ivAvatar);
        txtEmail.setText(customer.getEmail());
        txtFullName.setText(customer.getFullName());
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
        txtDob.setText(new SimpleDateFormat("yyyy-MM-dd").format(customer.getDob()));
        txtLanguage.setText(customer.getPrimaryLanguage());
    }

    public void setCustomer(Customer data) {
        customer = data;
        if(isViewCreated) {
            setData();
        }
    }
}
