package com.quang.daapp.ui.other;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quang.daapp.R;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ultis.WebSocketClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class ExpertActivity extends AppCompatActivity {

    private  NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebSocketClient.getInstance().connect(this);
        setContentView(R.layout.activity_expert);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home ||
                    destination.getId() == R.id.navigation_profile_expert ||
                    destination.getId() == R.id.navigation_dashboard ||
                    destination.getId() == R.id.navigation_history

            ) {
                navView.setVisibility(View.VISIBLE);
            }else {
                navView.setVisibility(View.GONE);
            }
        });
        NavigationUI.setupWithNavController(navView, navController);


        }

    public void startSub() {
        for (MutableLiveData<ReceiveMessage> m: WebSocketClient.getInstance().getSubscribes().values()) {
            m.observe(this, message -> {
                if(message.getType() == MessageType.CALLING && (!message.isExpert())) {
                    ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment("Would like to answer a call from", new ConfirmDialogFragment.OnConfirmDialogListener() {
                        @Override
                        public void OnYesListener() {
                            if (message.getType() == MessageType.CALLING) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(getString(R.string.key_request_id), Integer.parseInt(message.getMessage()));
                                bundle.putBoolean(getString(R.string.isExpert), true);
                                bundle.putBoolean("answer", true);
                                navController.navigate(R.id.communicationFragment2,bundle);
                            }
                        }

                        @Override
                        public void OnNoListener() {

                        }
                    });
                    dialogFragment.show(getSupportFragmentManager(),"");
                }
            });
        }
    }
}


