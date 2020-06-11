package com.quang.daapp.ui.communication;

import android.content.pm.PackageManager;
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
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.EstimateDialogFragment;
import com.quang.daapp.ui.dialog.FeedBackDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.viewAdapter.MessageAdapter;
import com.quang.daapp.ultis.CommonUltis;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunicationFragment extends Fragment {


    private NavController navController;
    private RecyclerView recyclerView;
    private int channel;
    private MessageAdapter adapter;
    private CommunicationViewModel viewModel;
    private ProblemRequestDetail detail;
    private ImageButton btnMenu;

    private TextView txtAccept;
    private  TextView txtProcess;
    private TextView txtComplete;
    private  PopupMenu popupMenu;
    private  boolean isExpert = false;
    private   List<ReceiveMessage> receiveMessageList;
    private int estimatePos = 0;

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
        assert getArguments() != null;
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

        view.findViewById(R.id.btnVideoCall).setOnClickListener(v ->
        {
            if(CommonUltis.checkCameraPermission(getContext(),getActivity())) {
                navigateToVideoCall();
            }

        });


        adapter = new MessageAdapter(getContext(), new ArrayList<>(), "Partner", isExpert,StatusEnum.ACCEPTED, pos -> {
            ReceiveMessage mes = adapter.getMessages().get(pos);
            ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment(getString(R.string.mes_estimate_confirm),
                    new ConfirmDialogFragment.OnConfirmDialogListener() {
                @Override
                public void OnYesListener() {
                    WebSocketClient.getInstance().chat(channel,
                            new SendMessage(mes.getMessage(), MessageType.ESTIMATE_YES));
                    estimatePos = pos;
                }

                @Override
                public void OnNoListener() {

                }
            });
            confirmDialogFragment.show(getParentFragmentManager(),getTag());
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        popupMenu = updateMenu(null);

        viewModel.getChatMessage(channel);
        viewModel.getChatMessageResult().observe(getViewLifecycleOwner(),receiveMessages -> {
            if(receiveMessages == null) return;
            receiveMessageList = receiveMessages;
            adapter.setMessages(receiveMessageList);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });

        viewModel.getDetail(channel);
        viewModel.getDetailResult().observe(getViewLifecycleOwner(),problemRequestDetail -> {
            if(problemRequestDetail == null) return;
            detail = problemRequestDetail;
            adapter.setStatusEnum(detail.getStatus());

            txtRequestTitle.setText(problemRequestDetail.getTitle());
            changeStatus(problemRequestDetail.getStatus());
            popupMenu = updateMenu(problemRequestDetail.getStatus());
        });


        view.findViewById(R.id.btnBack).setOnClickListener(v -> navController.popBackStack());

        btnMenu.setOnClickListener(v -> popupMenu.show());

        btnSend.setOnClickListener(v -> {

            WebSocketClient.getInstance().chat(channel,
                    new SendMessage(edtMessage.getText().toString(), MessageType.CHAT));
            edtMessage.setText("");
        });

        WebSocketClient.getInstance().getSubscribeChannelData(channel).observe(getViewLifecycleOwner(), receiveMessage -> {
            switch (receiveMessage.getType()) {
                case CHAT: {

                    break;
                }
                case ESTIMATE: {
                    if(isExpert) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.mes_estimate_sent),R.color.colorSuccess,R.drawable.ic_success);
                        dialogFragment.show(getParentFragmentManager(),getTag());

                    }
                    break;
                }
                case ESTIMATE_YES:{
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(getString(R.string.mes_estimate_yes),R.color.colorSuccess,R.drawable.ic_success);
                    dialogFragment.show(getParentFragmentManager(),getTag());
                    if(detail != null) detail.setStatus(StatusEnum.PROCESSING);
                    adapter.setStatusEnum(StatusEnum.PROCESSING);
                    adapter.notifyItemChanged(estimatePos);
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
                            () -> navController.popBackStack()
                    );
                    dialogFragment.show(getParentFragmentManager(),getTag());
                }
                case CANCEL: {
                    if(receiveMessage.isExpert() == isExpert) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(
                                getString(R.string.mes_cancel), R.color.colorDanger, R.drawable.ic_warning,
                                () -> navController.popBackStack()
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
                    if(receiveMessage.isExpert() == isExpert) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(
                                getString(R.string.mes_complete), R.color.colorSuccess, R.drawable.ic_success,
                                () -> navController.popBackStack()
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
                            () -> navController.popBackStack()
                    );
                    dialogFragment.show(getParentFragmentManager(),getTag());
                    break;
                }
            }
            adapter.addMessage(receiveMessage);

            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
                    ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment(
                            getString(R.string.mes_cancel_confirm),
                            new ConfirmDialogFragment.OnConfirmDialogListener() {
                                @Override
                                public void OnYesListener() {
                                    WebSocketClient.getInstance().chat(channel,
                                            new SendMessage("", MessageType.CANCEL));
                                }

                                @Override
                                public void OnNoListener() {

                                }
                            }
                    );
                    confirmDialogFragment.show(getParentFragmentManager(),getTag());
                    break;
                }
                case R.id.menu_complete: {
                    ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment(
                            getString(R.string.mes_complete_confirm),
                            new ConfirmDialogFragment.OnConfirmDialogListener() {
                                @Override
                                public void OnYesListener() {
                                    if(isExpert) {
                                        WebSocketClient.getInstance().chat(channel,
                                                new SendMessage("", MessageType.COMPLETE));
                                    }else {
                                        FeedBackDialogFragment feedBackDialogFragment = new FeedBackDialogFragment(channel) ;
                                        feedBackDialogFragment.show(getParentFragmentManager(),getTag());
                                    }
                                }

                                @Override
                                public void OnNoListener() {

                                }
                            }
                    );
                    confirmDialogFragment.show(getParentFragmentManager(),getTag());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("CLMNL","CCSCSCSC");
        if (requestCode == CommonUltis.CAMERA_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToVideoCall();
            }  else {
                MessageDialogFragment messageDialogFragment =
                        new MessageDialogFragment(getString(R.string.camera_permission_deny),R.color.colorWarning,R.drawable.ic_warning);
                messageDialogFragment.show(getParentFragmentManager(),getTag());
            }
        }
    }

    private void navigateToVideoCall() {
        Bundle bundle = new Bundle();
        bundle.putInt("channel",channel);
        bundle.putBoolean(getString(R.string.isExpert),isExpert);
        if(isExpert) {
            navController.navigate(R.id.action_communicationFragment2_to_videoCallFragment2,bundle);
        }else {
            navController.navigate(R.id.action_customerCommunicationFragment_to_videoCallFragment3,bundle);
        }
    }
}
