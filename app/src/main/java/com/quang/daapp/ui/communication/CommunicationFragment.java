package com.quang.daapp.ui.communication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
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
import java.util.concurrent.TimeUnit;

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
    private TextView txtProcess;
    private TextView txtComplete;
    private PopupMenu popupMenu;
    private boolean isExpert = false;
    private List<ReceiveMessage> receiveMessageList;
    private int estimatePos = 0;
    private int page = 0;
    private ProgressBar recycleLoader;

    private boolean answer = false;
    private boolean nextFlag = false;
    private boolean outOfHistory = false;
    private boolean skipFirstMessage = false;

    private Expert expert;

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
        channel = getArguments().getInt(getString(R.string.key_request_id));
        isExpert = getArguments().getBoolean(getString(R.string.isExpert));
        answer = getArguments().getBoolean("answer");
        navController = Navigation.findNavController(view);
        if (answer) {
            navigateToVideoCall();
        }
        recyclerView = view.findViewById(R.id.recycle_messages);
        final EditText edtMessage = view.findViewById(R.id.edtMessage);
        final ImageButton btnSend = view.findViewById(R.id.btnSend);
        btnMenu = view.findViewById(R.id.btnMenu);
        final TextView txtRequestTitle = view.findViewById(R.id.txtRequestTitle);
        txtAccept = view.findViewById(R.id.txtAccept);
        txtProcess = view.findViewById(R.id.txtProcess);
        txtComplete = view.findViewById(R.id.txtComplete);
        recycleLoader = view.findViewById(R.id.recycle_loader);

        view.findViewById(R.id.btnVideoCall).setOnClickListener(v ->
        {
            if (CommonUltis.checkCameraPermission(getContext(), getActivity())) {
                navigateToVideoCall();
            }

        });


        adapter = new MessageAdapter(getContext(), new ArrayList<>(), "Partner", isExpert, StatusEnum.ACCEPTED, pos -> {
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
            confirmDialogFragment.show(getParentFragmentManager(), getTag());
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        popupMenu = new PopupMenu(getActivity(), btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.communication_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_estimate: {
                    EstimateDialogFragment estimateDialogFragment = new EstimateDialogFragment(channel,expert.getFeePerHour());
                    estimateDialogFragment.show(getParentFragmentManager(), getTag());
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
                    confirmDialogFragment.show(getParentFragmentManager(), getTag());
                    break;
                }
                case R.id.menu_complete: {
                    ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment(
                            getString(R.string.mes_complete_confirm),
                            new ConfirmDialogFragment.OnConfirmDialogListener() {
                                @Override
                                public void OnYesListener() {
                                    if (isExpert) {
                                        WebSocketClient.getInstance().chat(channel,
                                                new SendMessage("", MessageType.COMPLETE));
                                    } else {
                                        FeedBackDialogFragment feedBackDialogFragment = new FeedBackDialogFragment(channel);
                                        feedBackDialogFragment.show(getParentFragmentManager(), getTag());
                                    }
                                }

                                @Override
                                public void OnNoListener() {

                                }
                            }
                    );
                    confirmDialogFragment.show(getParentFragmentManager(), getTag());
                    break;
                }
            }
            return true;
        });

        viewModel.getExpert(channel);
        viewModel.getExpertResult().observe(getViewLifecycleOwner(), expert -> {
            if(expert == null) return;
            this.expert = expert;
        });

        viewModel.getChatMessage(channel, page);
        viewModel.getChatMessageResult().observe(getViewLifecycleOwner(), receiveMessages -> {
            if (receiveMessages == null) return;
            adapter.addMessage(receiveMessages);
            adapter.notifyDataSetChanged();
            if (page == 0) {
                recyclerView.scrollToPosition(0);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                boolean canScrollUp = recyclerView.canScrollVertically(-1);
                boolean canScrollDown = recyclerView.canScrollVertically(1);
                if (canScrollDown && !canScrollUp && !outOfHistory) {
                    recycleLoader.setVisibility(View.VISIBLE);

                    if (!nextFlag) {
                        viewModel.getChatMessage(channel, page + 1);
                        viewModel.getChatMessageResult().observe(getViewLifecycleOwner(), receiveMessages -> {
                            if (receiveMessages == null) return;
                            if (receiveMessages.size() > 0) {
                                adapter.addMessage(receiveMessages);
                                adapter.notifyItemRangeInserted(adapter.getItemCount() - receiveMessages.size(),
                                        receiveMessages.size());
                                page++;
                            } else {
                                outOfHistory = true;
                            }
                            nextFlag = false;
                            recycleLoader.setVisibility(View.GONE);

                        });
                        nextFlag = true;
                    }
                } else {
                    recycleLoader.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getDetail(channel);

        viewModel.getDetailResult().observe(getViewLifecycleOwner(), problemRequestDetail -> {
            if (problemRequestDetail == null) return;
            detail = problemRequestDetail;
            adapter.setStatusEnum(detail.getStatus());

            txtRequestTitle.setText(problemRequestDetail.getTitle());
            changeStatus(problemRequestDetail.getStatus());
            updateMenu(problemRequestDetail.getStatus());
        });


        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            back();
        });

        btnMenu.setOnClickListener(v -> popupMenu.show());

        btnSend.setOnClickListener(v -> {
            if(!edtMessage.getText().toString().trim().isEmpty()) {

                WebSocketClient.getInstance().chat(channel,
                        new SendMessage(edtMessage.getText().toString(), MessageType.CHAT));
                edtMessage.setText("");
            }
        });

        if (WebSocketClient.getInstance().getSubscribeChannelData(channel).getValue() != null) {
            skipFirstMessage = true;
        }
        WebSocketClient.getInstance().getSubscribeChannelData(channel).observe(getViewLifecycleOwner(), receiveMessage -> {
            if (skipFirstMessage) {
                skipFirstMessage = false;
                return;
            }
            switch (receiveMessage.getType()) {
                case CHAT:
                case ESTIMATE: {
                    adapter.addMessage(receiveMessage);
                    adapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                    break;
                }
                case ESTIMATE_YES: {
                    if (detail != null) detail.setStatus(StatusEnum.PROCESSING);
                    adapter.setStatusEnum(StatusEnum.PROCESSING);
                    adapter.notifyItemChanged(estimatePos);
                    changeStatus(StatusEnum.PROCESSING);
                    updateMenu(StatusEnum.PROCESSING);
                    adapter.addMessage(receiveMessage);
                    adapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                    break;
                }
                case ESTIMATE_NO: {
                    break;
                }
                case CANCEL_YES: {
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(
                            getString(R.string.mes_cancel_yes), R.color.colorDanger, R.drawable.ic_warning,
                            this::back
                    );
                    dialogFragment.show(getParentFragmentManager(), getTag());
                }
                case CANCEL: {
                    if (receiveMessage.isExpert() == isExpert) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(
                                getString(R.string.mes_cancel), R.color.colorDanger, R.drawable.ic_warning,
                                this::back
                        );
                        dialogFragment.show(getParentFragmentManager(), getTag());
                    } else {
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
                                        back();
                                    }
                                }
                        );
                        dialogFragment.show(getParentFragmentManager(), getTag());
                    }
                    break;
                }
                case COMPLETE: {
                    if (receiveMessage.isExpert() == isExpert) {
                        MessageDialogFragment dialogFragment = new MessageDialogFragment(
                                getString(R.string.mes_complete), R.color.colorSuccess, R.drawable.ic_success,
                                this::back
                        );
                        dialogFragment.show(getParentFragmentManager(), getTag());
                    } else {
                        ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment(
                                getString(R.string.mes_complete_confirm),
                                new ConfirmDialogFragment.OnConfirmDialogListener() {
                                    @Override
                                    public void OnYesListener() {
                                        if(!isExpert) {
                                            FeedBackDialogFragment feedBackDialog = new FeedBackDialogFragment(channel);
                                            feedBackDialog.show(getParentFragmentManager(),getTag());
                                        }else {
                                            SendMessage sendMessage = new SendMessage("", MessageType.COMPLETE);
                                            WebSocketClient.getInstance().chat(channel,sendMessage);
                                        }
                                    }

                                    @Override
                                    public void OnNoListener() {
                                        back();
                                    }
                                }
                        );
                        dialogFragment.show(getParentFragmentManager(), getTag());
                    }
                    break;
                }
                case COMPLETE_YES: {
                    MessageDialogFragment dialogFragment = new MessageDialogFragment(
                            getString(R.string.mes_complete_yes), R.color.colorSuccess, R.drawable.ic_success,
                            this::back
                    );
                    dialogFragment.show(getParentFragmentManager(), getTag());
                    break;
                }
            }

        });

    }

    private void changeStatus(StatusEnum statusEnum) {
        Context context = getContext();
        assert context != null;
        int primaryDark = ContextCompat.getColor(context,R.color.colorPrimaryDark);
        int white = ContextCompat.getColor(context,R.color.colorWhite);
        int primary = ContextCompat.getColor(context,R.color.colorPrimary);
        int success = ContextCompat.getColor(context,R.color.colorSuccess);
        int danger = ContextCompat.getColor(context,R.color.colorDanger);
        txtAccept.setTextColor(primaryDark);
        txtProcess.setTextColor(primaryDark);
        txtComplete.setTextColor(primaryDark);
        txtAccept.setBackgroundColor(white);
        txtProcess.setBackgroundColor(white);
        txtComplete.setBackgroundColor(white);

        switch (statusEnum) {
            case ACCEPTED: {
                txtAccept.setTextColor(white);
                txtAccept.setBackgroundColor(primary);
                break;
            }
            case PROCESSING: {
                txtProcess.setTextColor(white);
                txtProcess.setBackgroundColor(primary);
                break;
            }
            case TMPCANCEL: {
                txtComplete.setText("Cancel");
                txtComplete.setTextColor(white);
                txtComplete.setBackgroundColor(danger);
                break;
            }
            case TMPCOMPLETE: {
                txtComplete.setText("Complete");
                txtComplete.setTextColor(white);
                txtComplete.setBackgroundColor(success);
                break;
            }
        }
    }

    private void updateMenu(StatusEnum statusEnum) {
        if (statusEnum != null) {
            switch (statusEnum) {
                case ACCEPTED: {
                    popupMenu.getMenu().findItem(R.id.menu_complete).setVisible(false);
                    if (!isExpert) {
                        popupMenu.getMenu().findItem(R.id.menu_estimate).setVisible(false);
                    }
                    break;
                }
                case PROCESSING: {
                    popupMenu.getMenu().findItem(R.id.menu_estimate).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_complete).setVisible(true);
                    break;
                }
                case TMPCANCEL: {
                    popupMenu.getMenu().findItem(R.id.menu_estimate).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_complete).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_cancel).setVisible(true);
                    break;
                }
                case TMPCOMPLETE: {
                    popupMenu.getMenu().findItem(R.id.menu_estimate).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_complete).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.menu_cancel).setVisible(false);
                    break;
                }

            }
        }

    }



    private void navigateToVideoCall() {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.key_request_id), channel);
        bundle.putBoolean(getString(R.string.isExpert), isExpert);
        bundle.putBoolean("answer", answer);
        if (isExpert) {
            navController.navigate(R.id.action_communicationFragment2_to_videoCallFragment2, bundle);
        } else {
            navController.navigate(R.id.action_customerCommunicationFragment_to_videoCallFragment3, bundle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
/*        if(videoCall) {
            if(isExpert) navController.navigate(R.id.navigation_home);
            else navController.navigate(R.id.navigation_home_customer);
        }*/
    }

    private void back() {
        if (isExpert) navController.navigate(R.id.navigation_home);
        else navController.navigate(R.id.navigation_home_customer);
    }
}