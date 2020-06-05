package com.quang.daapp.ui.communication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Estimate;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.EstimateDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.viewAdapter.MessageAdapter;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunicationFragment extends Fragment {


    NavController navController;
    RecyclerView recyclerView;
    int channel;
    MessageAdapter adapter;
    CommunicationViewModel viewModel;
    ProblemRequestDetail detail;
    ImageButton btnMenu;

    TextView txtAccept;
    TextView txtProcess;
    TextView txtComplete;
    PopupMenu popupMenu;
    boolean isExpert = false;

    public CommunicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel =
                ViewModelProviders.of(this).get(CommunicationViewModel.class);

        return inflater.inflate(R.layout.fragment_communication, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channel = getArguments().getInt(getString(R.string.key_request_id)) ;
        isExpert = getArguments().getBoolean(getString(R.string.isExpert));

        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.recycle_messages);
        final EditText edtMessage= view.findViewById(R.id.edtMessage);
        final ImageButton btnSend = view.findViewById(R.id.btnSend);
        btnMenu = view.findViewById(R.id.btnMenu);
        final TextView txtRequestTitle = view.findViewById(R.id.txtRequestTitle);
        txtAccept = view.findViewById(R.id.txtAccept);
        txtProcess = view.findViewById(R.id.txtProcess);
        txtComplete = view.findViewById(R.id.txtComplete);
        adapter = new MessageAdapter(getContext(), new ArrayList<>(),"Partner", isExpert);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        popupMenu = updateMenu(null);

        viewModel.getChatMessage(channel);
        viewModel.getChatMessageResult().observe(getViewLifecycleOwner(),receiveMessages -> {
            if(receiveMessages == null) return;
            adapter.setMessages(receiveMessages);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        viewModel.getDetail(channel);
        viewModel.getDetailResult().observe(getViewLifecycleOwner(),problemRequestDetail -> {
            if(problemRequestDetail == null) return;
            detail = problemRequestDetail;
            txtRequestTitle.setText(problemRequestDetail.getTitle());
            changeStatus(problemRequestDetail.getStatus());
            popupMenu = updateMenu(problemRequestDetail.getStatus());
        });


        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebSocketClient.getInstance().chat(channel,
                        new SendMessage(edtMessage.getText().toString(), MessageType.CHAT));
                edtMessage.setText("");
            }
        });

        WebSocketClient.getInstance().getSubscribeChannelData(channel).observe(getViewLifecycleOwner(), receiveMessage -> {
            switch (receiveMessage.getType()) {
                case CHAT: {
                    adapter.addMessage(receiveMessage);
                    Log.e("CLMN", receiveMessage.isExpert() + " - " + receiveMessage.getMessage());
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    break;
                }
                case ESTIMATE: {
                    if(!isExpert) {
                        Estimate estimate = NetworkClient.getGson().fromJson(receiveMessage.getMessage(),Estimate.class);
                        String message = "You expert estimation is " + estimate.getHour() + " hour to solve which cost " + estimate.getTotal();
                        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment(message, new ConfirmDialogFragment.OnConfirmDialogListener() {
                            @Override
                            public void OnYesListener() {
                                WebSocketClient.getInstance().chat(channel,
                                        new SendMessage(receiveMessage.getMessage(), MessageType.ESTIMATE_YES));
                            }

                            @Override
                            public void OnNoListener() {

                            }
                        });
                        confirmDialogFragment.show(getParentFragmentManager(),getTag());
                    }else {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.mes_estimate_sent),R.color.colorSuccess,R.drawable.ic_success);
                        dialogFragment.show(getParentFragmentManager(),getTag());
                    }
                    break;
                }
                case ESTIMATE_YES:{
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.mes_estimate_yes),R.color.colorSuccess,R.drawable.ic_success);
                    dialogFragment.show(getParentFragmentManager(),getTag());
                    detail.setStatus(StatusEnum.PROCESSING);
                    changeStatus(StatusEnum.PROCESSING);
                    break;
                }
                case ESTIMATE_NO: {
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.mes_estimate_no),R.color.colorWarning,R.drawable.ic_warning);
                    dialogFragment.show(getParentFragmentManager(),getTag());
                    break;
                }
                case CANCEL_YES: {
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(
                            getString(R.string.mes_cancel_yes), R.color.colorDanger, R.drawable.ic_warning,
                            new MessageDialogFragment.OnMyDialogListener() {
                                @Override
                                public void OnOKListener() {
                                    navController.popBackStack();
                                }
                            }
                    );
                    dialogFragment.show(getParentFragmentManager(),getTag());
                }
                case CANCEL: {
                    if(!receiveMessage.isExpert()) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(
                                getString(R.string.mes_cancel), R.color.colorDanger, R.drawable.ic_warning,
                                new MessageDialogFragment.OnMyDialogListener() {
                                    @Override
                                    public void OnOKListener() {
                                        navController.popBackStack();
                                    }
                                }
                        );
                        dialogFragment.show(getParentFragmentManager(),getTag());
                    }else {
                        ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment(
                                getString(R.string.mes_cancel_confirm),
                                new ConfirmDialogFragment.OnConfirmDialogListener() {
                                    @Override
                                    public void OnYesListener() {
                                        WebSocketClient.getInstance().chat(channel,
                                                new SendMessage(edtMessage.getText().toString(), MessageType.CANCEL));
                                    }

                                    @Override
                                    public void OnNoListener() {
                                        navController.popBackStack();
                                    }
                                }
                        );
                        dialogFragment.show(getParentFragmentManager(),getTag());
                    }
                    break;
                }
                case COMPLETE: {
                    if(!receiveMessage.isExpert()) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(
                                getString(R.string.mes_complete), R.color.colorSuccess, R.drawable.ic_success,
                                new MessageDialogFragment.OnMyDialogListener() {
                                    @Override
                                    public void OnOKListener() {
                                        navController.popBackStack();
                                    }
                                }
                        );
                        dialogFragment.show(getParentFragmentManager(),getTag());
                    }else {
                        ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment(
                                getString(R.string.mes_complete_confirm),
                                new ConfirmDialogFragment.OnConfirmDialogListener() {
                                    @Override
                                    public void OnYesListener() {
                                        WebSocketClient.getInstance().chat(channel,
                                                new SendMessage(edtMessage.getText().toString(), MessageType.COMPLETE));
                                    }

                                    @Override
                                    public void OnNoListener() {
                                        navController.popBackStack();
                                    }
                                }
                        );
                        dialogFragment.show(getParentFragmentManager(),getTag());
                    }
                    break;
                }
                case COMPLETE_YES: {
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(
                            getString(R.string.mes_complete_yes), R.color.colorSuccess, R.drawable.ic_success,
                            new MessageDialogFragment.OnMyDialogListener() {
                                @Override
                                public void OnOKListener() {
                                    navController.popBackStack();
                                }
                            }
                    );
                    dialogFragment.show(getParentFragmentManager(),getTag());
                    break;
                }
            }
        });

    }

    private void changeStatus(StatusEnum statusEnum) {
        txtAccept.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        txtProcess.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        txtComplete.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        txtAccept.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        txtProcess.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        txtComplete.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        switch (statusEnum) {
            case ACCEPTED: {
                txtAccept.setTextColor(getResources().getColor(R.color.colorWhite));
                txtAccept.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case PROCESSING: {
                txtProcess.setTextColor(getResources().getColor(R.color.colorWhite));
                txtProcess.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case COMPLETE: {
                txtComplete.setTextColor(getResources().getColor(R.color.colorWhite));
                txtComplete.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
        }
    }

    private PopupMenu updateMenu(StatusEnum statusEnum) {
        PopupMenu popupMenu = new PopupMenu(getActivity(),btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.communication_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_estimate: {
                    EstimateDialogFragment estimateDialogFragment = new EstimateDialogFragment(channel);
                    estimateDialogFragment.show(getParentFragmentManager(),getTag());
                    break;
                }
                case R.id.menu_cancel: {
                    WebSocketClient.getInstance().chat(channel,
                            new SendMessage("", MessageType.CANCEL));
                    break;
                }
                case R.id.menu_complete: {
                    WebSocketClient.getInstance().chat(channel,
                            new SendMessage("", MessageType.COMPLETE));
                    break;
                }
            }
            return true;
        });
        if(statusEnum != null) {
            switch (statusEnum) {
                case ACCEPTED:{
                    popupMenu.getMenu().removeItem(R.id.menu_complete);
                    if(!isExpert) {
                        popupMenu.getMenu().removeItem(R.id.menu_estimate);
                    }
                    break;
                }
                case PROCESSING:{
                    popupMenu.getMenu().removeItem(R.id.menu_estimate);
                    break;
                }

            }
        }
        return popupMenu;
    }
}
