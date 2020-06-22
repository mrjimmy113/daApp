package com.quang.daapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quang.daapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDialogFragment extends MyBaseDialogFragment {

    private String message;
    private OnConfirmDialogListener dialogListener;

    public ConfirmDialogFragment(String message, OnConfirmDialogListener dialogListener) {
        this.message = message;
        this.dialogListener = dialogListener;
    }

    public ConfirmDialogFragment(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm,null);
        final TextView txtMessage = view.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
        final Button btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogListener != null) dialogListener.OnYesListener();
            }
        });

        final Button btnNo = view.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogListener != null) dialogListener.OnNoListener();
            }
        });

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }

    public interface OnConfirmDialogListener {
        void OnYesListener();
        void OnNoListener();
    }
}
