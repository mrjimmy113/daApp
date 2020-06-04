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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ui.viewAdapter.MessageAdapter;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCommunicationFragment extends Fragment {


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

    public CustomerCommunicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel =
                ViewModelProviders.of(this).get(CommunicationViewModel.class);

        return inflater.inflate(R.layout.fragment_customer_communication, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channel = getArguments().getInt(getString(R.string.key_request_id)) ;
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.recycle_messages);
        final EditText edtMessage= view.findViewById(R.id.edtMessage);
        final ImageButton btnSend = view.findViewById(R.id.btnSend);
        btnMenu = view.findViewById(R.id.btnMenu);
        final TextView txtRequestTitle = view.findViewById(R.id.txtRequestTitle);
        txtAccept = view.findViewById(R.id.txtAccept);
        txtProcess = view.findViewById(R.id.txtProcess);
        txtComplete = view.findViewById(R.id.txtComplete);
        adapter = new MessageAdapter(getContext(), new ArrayList<>(),"Expert", false);
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
                        new SendMessage(false,edtMessage.getText().toString(), MessageType.CHAT));
                edtMessage.setText("");


            }
        });

        WebSocketClient.getInstance().getSubscribeChannelData(channel).observe(getViewLifecycleOwner(), receiveMessage -> {
            switch (receiveMessage.getType()) {
                case CHAT: {
                    adapter.addMessage(receiveMessage);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    break;
                }
                case ESTIMATE: {
                    if(receiveMessage.isExpert()) {
                        //Dialog message you cancel request
                    }
                }
                case ESTIMATE_YES:{

                }
                case ESTIMATE_NO: {
                    if(receiveMessage.isExpert()) {
                        //Remove dialog
                    }
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
                    WebSocketClient.getInstance().chat(channel,
                            new SendMessage(false,"", MessageType.ESTIMATE));
                    break;
                }
                case R.id.menu_cancel: {
                    WebSocketClient.getInstance().chat(channel,
                            new SendMessage(false,"", MessageType.CANCEL));
                    break;
                }
                case R.id.menu_complete: {
                    WebSocketClient.getInstance().chat(channel,
                            new SendMessage(false,"", MessageType.COMPLETE));
                    break;
                }
            }
            return true;
        });
        if(statusEnum != null) {
            switch (statusEnum) {
                case ACCEPTED:{
                    popupMenu.getMenu().removeItem(R.id.menu_complete);
                    popupMenu.getMenu().removeItem(R.id.menu_estimate);
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
