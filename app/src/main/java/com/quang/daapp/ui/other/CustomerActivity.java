package com.quang.daapp.ui.other;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quang.daapp.R;
import com.quang.daapp.data.repository.ProblemRequestRepository;
import com.quang.daapp.data.service.ProblemRequestService;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class CustomerActivity extends AppCompatActivity {

    ConstraintLayout loaderLayout = null;
    BottomNavigationView navView = null;
    NavController navController;
    private boolean isVideoCalling= false;
    private Context context;
    private AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_customer);

        navView = findViewById(R.id.nav_view);
        WebSocketClient.getInstance().connect(this);
        WebSocketClient.getInstance().stompClient.setStompConnectionListener(new StompConnectionListener() {
            @Override
            public void onConnected() {
                super.onConnected();
                Log.e("CMN", "CONNECTED");
                ProblemRequestRepository.getInstance().getSubableProfile().observe(activity, new Observer<List<Number>>() {
                    @Override
                    public void onChanged(List<Number> numbers) {
                        for (Number id:
                             numbers) {
                            WebSocketClient.getInstance().subscribe(id.intValue());

                        }
                    }
                });
            }

            @Override
            public void onDisconnected() {
                super.onDisconnected();
                Log.e("CMN", "DISCONNECTED");
                WebSocketClient.getInstance().connect(context);
            }
        });


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home_customer ||
                    destination.getId() == R.id.navigation_profile_customer ||
                    destination.getId() == R.id.navigation_history

            ) {
                navView.setVisibility(View.VISIBLE);
            }else {
                navView.setVisibility(View.GONE);
            }
            isVideoCalling =  destination.getId() == R.id.videoCallFragment2;
        });

        CommonUltis.checkCameraPermission(this,this);
        CommonUltis.checkPermissions(this,this);
        NetworkClient.getInstance().init(this,this,navController);
        DialogManager.getInstance().init(getSupportFragmentManager());
        NavigationUI.setupWithNavController(navView, navController);
    }
    public void startSub() {
        for (MutableLiveData<ReceiveMessage> m: WebSocketClient.getInstance().getSubscribes().values()) {
            m.observe(this, message -> {
                if(message.getType() == MessageType.CALLING && (message.isExpert())) {
                    ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment("Would like to answer a call from a partner in request: " + message.getMessage(), new ConfirmDialogFragment.OnConfirmDialogListener() {
                        @Override
                        public void OnYesListener() {
                            if (message.getType() == MessageType.CALLING) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(getString(R.string.key_request_id), Integer.parseInt(message.getMessage()));
                                bundle.putBoolean(getString(R.string.isExpert), false);
                                bundle.putBoolean("answer", true);
                                navController.navigate(R.id.videoCallFragment3,bundle);
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
