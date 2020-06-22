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

import com.bumptech.glide.Glide;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.ultis.NetworkClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailExpertFragment extends Fragment {

    private Expert expert;
    private boolean isViewCreated = false;

    private ImageView iv_avatar;
    private TextView txtFullName;
    private TextView txtEmail;
    private TextView txtMajor;
    private TextView txtFee;
    private TextView txtBankName;
    private TextView txtAccountNo;
    private TextView txtDescription;

    public RequestDetailExpertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail_expert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_avatar = view.findViewById(R.id.iv_avatar);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtMajor = view.findViewById(R.id.txtMajor);
        txtFee = view.findViewById(R.id.txtFee);
        txtBankName = view.findViewById(R.id.txtBankName);
        txtAccountNo = view.findViewById(R.id.txtAccountNo);
        txtDescription = view.findViewById(R.id.txtDescription);
        if(expert != null) {
            setData();
        }
        isViewCreated = true;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
        if(isViewCreated) {
            setData();
        }
    }

    private void setData() {
        Glide.with(getContext()).load(NetworkClient.getImageUrl(expert.getImgName())).into(iv_avatar);
        txtEmail.setText(expert.getEmail());
        txtFullName.setText(expert.getFullName());
        StringBuilder strMajor = new StringBuilder();
        for (int i = 0; i < expert.getMajor().size(); i++) {
            strMajor.append(expert.getMajor().get(i));
            if(i < expert.getMajor().size() - 1) {
                strMajor.append(" - ");
            }
        }
        txtMajor.setText(strMajor.toString());
        txtFee.setText(expert.getFeePerHour() + "");
        txtBankName.setText(expert.getBankName());
        txtAccountNo.setText(expert.getBankAccountNo());
        txtDescription.setText(expert.getDescription());
    }
}
