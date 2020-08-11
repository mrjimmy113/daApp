package com.quang.daapp.ui.other;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;
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
    private Context context;
    private AppCompatActivity activity;
    private boolean isShowCall= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = this;
        NetworkClient.getInstance().init(this,this,navController);
        DialogManager.getInstance().init(getSupportFragmentManager());
        WebSocketClient.getInstance().connect(this);

        WebSocketClient.getInstance().stompClient.setStompConnectionListener(new StompConnectionListener() {
            @Override
            public void onConnected() {
                super.onConnected();
                Log.e("CMN", "CONNECTED");
                findSub();

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



        });
        NavigationUI.setupWithNavController(navView, navController);



    }

    @Override
    protected void onResume() {
        super.onResume();

        if(WebSocketClient.getInstance().stompClient.isStompConnected()) {
            findSub();
        }

    }
    public void findSub() {
            ProblemRequestRepository.getInstance().getSubableProfile().observe(activity, new Observer<List<Number>>() {
                @Override
                public void onChanged(List<Number> numbers) {
                    WebSocketClient.getInstance().clear();
                    if(numbers == null) return;
                    for (Number id:
                            numbers) {
                        Log.e("CMN","Sub " + id.intValue());
                        WebSocketClient.getInstance().subscribe(id.intValue());

                    }
                    startSub();
                }
            });
        }

    public void startSub() {
        for (MutableLiveData<ReceiveMessage> m: WebSocketClient.getInstance().getSubscribes().values()) {
            m.observe(this, message -> {
                if(message.getType() == MessageType.CALLING && !message.isExpert() &&
                    navController.getCurrentDestination().getId() != R.id.videoCallFragment2
                        && !isShowCall
                ) {
                    isShowCall = true;
                    ProblemRequest problemRequest = NetworkClient.getInstance().getGson().fromJson(message.getMessage(),ProblemRequest.class);
                    ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment("Would like to answer a call from partner in " + problemRequest.getTitle(), new ConfirmDialogFragment.OnConfirmDialogListener() {
                        @Override
                        public void OnYesListener() {
                            isShowCall = false;
                            if (message.getType() == MessageType.CALLING) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(getString(R.string.key_request_id), problemRequest.getRequestId());
                                bundle.putBoolean(getString(R.string.isExpert), true);
                                bundle.putBoolean("answer", true);
                                navController.navigate(R.id.videoCallFragment2,bundle);
                            }
                        }

                        @Override
                        public void OnNoListener() {
                            isShowCall = false;
                        }
                    });
                    //dialogFragment.show(getSupportFragmentManager(),"");
                    DialogManager.getInstance().showDialog(dialogFragment,true);
                }
            });
        }
    }
}


