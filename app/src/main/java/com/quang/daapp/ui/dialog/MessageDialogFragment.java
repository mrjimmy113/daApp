package com.quang.daapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quang.daapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MessageDialogFragment extends DialogFragment {

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
        View view = inflater.inflate(R.layout.message_dialog,null);


        final Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                myDialogListener.OnOKListener();
            }
        });

        final TextView txtMessage = view.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        final ImageView imageView = view.findViewById(R.id.iv_dialog_result);
        imageView.setBackgroundResource(bgColor);
        imageView.setImageResource(icon);

        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface OnMyDialogListener {
        void OnOKListener();
    }
}


