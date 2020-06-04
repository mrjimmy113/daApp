package com.quang.daapp.ui.communication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.quang.daapp.R;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ui.viewAdapter.MessageAdapter;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpertCommunicationFragment extends Fragment {
    NavController navController;
    RecyclerView recyclerView;
    int channel;
    MessageAdapter adapter;
    CommunicationViewModel viewModel;

    public ExpertCommunicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(CommunicationViewModel.class);
        return inflater.inflate(R.layout.fragment_expert_communication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channel = getArguments().getInt(getString(R.string.key_request_id)) ;
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.recycle_messages);
        final EditText edtMessage= view.findViewById(R.id.edtMessage);
        final ImageButton btnSend = view.findViewById(R.id.btnSend);
        adapter = new MessageAdapter(getContext(),new ArrayList<ReceiveMessage>(),"Customer", true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        viewModel.getChatMessage(channel);
        viewModel.getChatMessageResult().observe(getViewLifecycleOwner(),receiveMessages -> {
            adapter.setMessages(receiveMessages);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebSocketClient.getInstance().chat(channel,
                        new SendMessage(true,edtMessage.getText().toString(), MessageType.CHAT));
                edtMessage.setText("");
            }
        });

        WebSocketClient.getInstance().getSubscribeChannelData(channel).observe(getViewLifecycleOwner(), new Observer<ReceiveMessage>() {
            @Override
            public void onChanged(ReceiveMessage receiveMessage) {

            }
        });

    }
}
