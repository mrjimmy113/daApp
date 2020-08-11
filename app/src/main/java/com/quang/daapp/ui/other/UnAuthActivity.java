package com.quang.daapp.ui.other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.quang.daapp.R;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.CommonUltis;
import com.quang.daapp.ultis.DialogManager;
import com.quang.daapp.ultis.NetworkClient;

public class UnAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_auth);
        NetworkClient.getInstance().init(this,this, Navigation.findNavController(this,R.id.nav_host_fragment));
        DialogManager.getInstance().init(getSupportFragmentManager());
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int i =0; i < permissions.length; i++) {
            Log.e("CLMN","CLMN" + permissions[i] + " - " + grantResults[i]);
            if(grantResults[i] == -1) {
                MessageDialogFragment messageDialogFragment = new MessageDialogFragment("Fix it need camera,audio, storage permission to work, please allow our app to use them", R.color.colorWarning, R.drawable.ic_warning, new MessageDialogFragment.OnMyDialogListener() {
                    @Override
                    public void OnOKListener() {
                        checkPermission();
                    }
                });
                DialogManager.getInstance().showDialog(messageDialogFragment,false);
                return;
            }
        }
    }

    private void checkPermission() {
        CommonUltis.checkPermissions(this,this);
    }
}
