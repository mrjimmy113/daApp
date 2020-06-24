package com.quang.daapp.ui.other;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quang.daapp.R;
import com.quang.daapp.data.repository.ProblemRequestRepository;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.StompConnectionListener;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ultis.CommonUltis;
import com.quang.daapp.ultis.DialogManager;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class ExpertActivity extends AppCompatActivity {

    private  NavController navController;
    private boolean isVideoCalling = false;
    private Context context;
    private AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = this;
        WebSocketClient.getInstance().connect(this);

        WebSocketClient.getInstance().stompClient.setStompConnectionListener(new StompConnectionListener() {
            @Override
            public void onConnected() {
                super.onConnected();
                Log.e("CMN", "CONNECTED");
                findSub();
                startSub();
            }

            @Override
            public void onDisconnected() {
                super.onDisconnected();
                Log.e("CMN", "DISCONNECTED");
                WebSocketClient.getInstance().connect(context);
            }
        });
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
            isVideoCalling =  destination.getId() == R.id.videoCallFragment3;


        });
        NavigationUI.setupWithNavController(navView, navController);

        NetworkClient.getInstance().init(this,this,navController);
        DialogManager.getInstance().init(getSupportFragmentManager());
        CommonUltis.checkCameraPermission(this,this);
        CommonUltis.checkPermissions(this,this);
    }
    public void findSub() {
            ProblemRequestRepository.getInstance().getSubableProfile().observe(activity, new Observer<List<Number>>() {
                @Override
                public void onChanged(List<Number> numbers) {
                    for (Number id:
                            numbers) {
                        Log.e("CMN","Sub " + id.intValue());
                        WebSocketClient.getInstance().subscribe(id.intValue());

                    }
                }
            });
        }

    public void startSub() {
        for (MutableLiveData<ReceiveMessage> m: WebSocketClient.getInstance().getSubscribes().values()) {
            m.observe(this, message -> {
                if(message.getType() == MessageType.CALLING && (!message.isExpert()) && !isVideoCalling) {
                    ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment("Would like to answer a call from", new ConfirmDialogFragment.OnConfirmDialogListener() {
                        @Override
                        public void OnYesListener() {
                            if (message.getType() == MessageType.CALLING) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(getString(R.string.key_request_id), Integer.parseInt(message.getMessage()));
                                bundle.putBoolean(getString(R.string.isExpert), true);
                                bundle.putBoolean("answer", true);
                                navController.navigate(R.id.videoCallFragment2,bundle);
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


