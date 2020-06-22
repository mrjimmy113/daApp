package com.quang.daapp.ui.dialog;

import android.content.DialogInterface;
import android.util.Log;

import com.quang.daapp.ultis.DialogManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyBaseDialogFragment extends DialogFragment {
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        DialogManager.getInstance().closeDialog();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        DialogManager.getInstance().closeDialog();
    }
}
