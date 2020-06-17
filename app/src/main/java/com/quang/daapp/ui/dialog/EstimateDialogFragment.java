package com.quang.daapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Estimate;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.WebSocketClient;

import java.text.ParseException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EstimateDialogFragment extends DialogFragment {

    private int channel;
    private float fee;
    private Integer[] hours = {0,1,2,3,4,5,6,7,8,9,10,11,12};
    private Integer[] minutes = {5,10,15,20,25,30,35,40,45,50,55};
    private float totalMoney = 0;
    private float totalHour = 0;

    public EstimateDialogFragment(int channel, float fee) {
        this.channel = channel;
        this.fee = fee;
    }

    private TextView txtTotal;
    private Spinner spnHour;
    private Spinner spnMinute;
    private  Button btnYes;
    private TextView txtError;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_estimate,null);
        txtTotal = view.findViewById(R.id.txtTotal);
        spnHour = view.findViewById(R.id.spnHour);
        spnMinute = view.findViewById(R.id.spnMinute);
        txtError = view.findViewById(R.id.txtError);

        ArrayAdapter<Integer> adapterHour = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, hours);
        adapterHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnHour.setAdapter(adapterHour);

        ArrayAdapter<Integer> adapterMinute = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, minutes);
        adapterMinute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMinute.setAdapter(adapterHour);

        spnHour.setOnItemClickListener((parent, view12, position, id) -> onEstimateChange());
        spnMinute.setOnItemClickListener((parent, view1, position, id) -> {
            onEstimateChange();
        });


        btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v -> {
            if(!validate()) {
                txtError.setVisibility(View.VISIBLE);
                return;
            }
            dismiss();
            String message = NetworkClient.getGson().toJson(new Estimate(
                    totalHour,
                    totalMoney
                    )
            );
            SendMessage sendMessage = new SendMessage(message, MessageType.ESTIMATE);
            WebSocketClient.getInstance().chat(channel,sendMessage);
        });

        final Button btnNo = view.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(v -> dismiss());

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }

    private void onEstimateChange() {
        int hour = (Integer) spnHour.getSelectedItem();
        int minute = (Integer) spnMinute.getSelectedItem();
        totalHour = hour + (float)minute / 60;
        totalMoney = totalHour * fee;
        txtError.setVisibility(View.INVISIBLE);
        txtTotal.setText(totalMoney + "");

    }

    private boolean validate() {
        if(totalMoney == 0) return  false;
        return true;
    }
}
