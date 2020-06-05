package com.quang.daapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Estimate;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.WebSocketClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EstimateDialogFragment extends DialogFragment {

    private int channel;

    public EstimateDialogFragment(int channel) {
        this.channel = channel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_estimate,null);
        final EditText edtEstimateHour = view.findViewById(R.id.edtEstimateHour);
        final EditText edtTotal = view.findViewById(R.id.edtTotal);


        final Button btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String message = NetworkClient.getGson().toJson(new Estimate(
                        Float.parseFloat(edtEstimateHour.getText().toString()),
                        Float.parseFloat(edtTotal.getText().toString())
                        )
                );
                SendMessage sendMessage = new SendMessage(message, MessageType.ESTIMATE);
                WebSocketClient.getInstance().chat(channel,sendMessage);
            }
        });

        final Button btnNo = view.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }
}
