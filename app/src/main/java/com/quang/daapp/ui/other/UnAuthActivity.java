package com.quang.daapp.ui.other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.os.Bundle;

import com.quang.daapp.R;
import com.quang.daapp.ultis.DialogManager;
import com.quang.daapp.ultis.NetworkClient;

public class UnAuthActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_auth);
        NetworkClient.getInstance().init(this,this, Navigation.findNavController(this,R.id.nav_host_fragment));
        DialogManager.getInstance().init(getSupportFragmentManager());

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        DialogManager.getInstance().closeDialog();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        DialogManager.getInstance().closeDialog();
    }
}
