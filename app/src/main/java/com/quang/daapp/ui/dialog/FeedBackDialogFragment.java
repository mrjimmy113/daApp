package com.quang.daapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Estimate;
import com.quang.daapp.data.model.Feedback;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.WebSocketClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FeedBackDialogFragment extends DialogFragment {

    private int channel;

    public FeedBackDialogFragment(int channel) {
        this.channel = channel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_feedback,null);
        final RatingBar ratingBar = view.findViewById(R.id.rb_rating);
        final EditText edtFeedback = view.findViewById(R.id.edtFeedBack);


        final Button btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v -> {
            dismiss();
            String message = NetworkClient.getInstance().getGson().toJson(new Feedback(
                    ratingBar.getRating(),
                    edtFeedback.getText().toString()
                    )
            );
            SendMessage sendMessage = new SendMessage(message, MessageType.COMPLETE);
            WebSocketClient.getInstance().chat(channel,sendMessage);
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
