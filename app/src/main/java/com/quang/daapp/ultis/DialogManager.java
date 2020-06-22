package com.quang.daapp.ultis;

import android.content.DialogInterface;

import com.quang.daapp.ui.dialog.ConfirmDialogFragment;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DialogManager {
    private static DialogManager instance;

    public static DialogManager getInstance() {
        if(instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }

    private FragmentManager fragmentManager;

    public void init(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private DialogFragment currentDialog;
    private Queue<DialogFragment> dialogQueue = new ArrayDeque<>();

    public void showDialog(DialogFragment dialogFragment, boolean clear) {
        if(currentDialog != null) {
            dialogQueue.add(dialogFragment);
        }else {
            currentDialog = dialogFragment;
            dialogFragment.show(fragmentManager,"");
        }

    }
    public void closeDialog() {
        currentDialog = null;
        //if(clear) dialogQueue.clear();
        if(!dialogQueue.isEmpty()) dialogQueue.poll().show(fragmentManager,"");
    }

}
