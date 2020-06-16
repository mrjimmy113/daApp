package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.viewAdapter.MessageAdapter;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailMessageFragment extends Fragment {


    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isExpert;
    private List<ReceiveMessage> receiveMessageList = new ArrayList<>();


    public RequestDetailMessageFragment() {
        // Required empty public constructor
    }



    public RequestDetailMessageFragment(boolean isExpert) {
        this.isExpert = isExpert;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_messages);
        adapter = new MessageAdapter(getContext(), receiveMessageList, "Partner", isExpert, StatusEnum.TMPCOMPLETE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true));
    }

    public void setList(List<ReceiveMessage> list) {
        receiveMessageList = list;
        if(adapter != null) {
            adapter.setMessages(list);
            adapter.notifyDataSetChanged();
        }
    }
}
