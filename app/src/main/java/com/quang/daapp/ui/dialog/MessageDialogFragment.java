package com.quang.daapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.ultis.DialogManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MessageDialogFragment extends MyBaseDialogFragment {

    private String message;
    private int bgColor;
    private int icon;
    private OnMyDialogListener myDialogListener;

    public MessageDialogFragment(String message, int bgColor, int icon, OnMyDialogListener myDialogListener) {
        this.message = message;
        this.bgColor = bgColor;
        this.icon = icon;
        this.myDialogListener = myDialogListener;
    }

    public MessageDialogFragment(String message, int bgColor, int icon) {
        this.message = message;
        this.bgColor = bgColor;
        this.icon = icon;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_message,null);

        final Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        final TextView txtMessage = view.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        final ImageView imageView = view.findViewById(R.id.iv_dialog_result);
        imageView.setBackgroundResource(bgColor);
        imageView.setImageResource(icon);

        builder.setView(view);

        Dialog  dialog= builder.create();


        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(myDialogListener != null) myDialogListener.OnOKListener();

    }



    public interface OnMyDialogListener {
        void OnOKListener();
    }
}


