package com.quang.daapp.ui.other;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.viewAdapter.ProblemRequestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestListFragment extends Fragment {
    private boolean isOpen = false;
    private TextView title;
    private ImageView ivUp;
    private ImageView ivDown;
    private TextView txtCount;
    private RecyclerView recyclerView;
    private ProblemRequestAdapter adapter;
    private List<ProblemRequest> list = new ArrayList<>();
    private NavController navController;
    private Toolbar tool_bar;
    private OnRequestListListener event;
    private boolean ongoingRequestFlag = false;
    public RequestListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.txtRequestList);
        ivUp = view.findViewById(R.id.iv_up);
        ivDown = view.findViewById(R.id.iv_down);
        txtCount = view.findViewById(R.id.txtCount);
        recyclerView = view.findViewById(R.id.rv_problem_request);
        tool_bar = view.findViewById(R.id.tool_bar);

        tool_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClose();
            }
        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean canScrollUp = recyclerView.canScrollVertically(-1);
                boolean canScrollDown = recyclerView.canScrollVertically(1);
                if(canScrollUp && !canScrollDown) {
                    if(!ongoingRequestFlag) {
                        ongoingRequestFlag = true;
                        event.OnScrollToBottom().observe(getViewLifecycleOwner(), requestList -> {
                            ongoingRequestFlag = false;
                            if(requestList == null) return;
                            if(requestList.size() > 0) {
                                adapter.addRequestList(requestList);
                                event.OnGetRequestSuccess(true);
                            }else {
                                event.OnGetRequestSuccess(false);
                            }
                        });
                    }
                }
            }
        });
        adapter = new ProblemRequestAdapter(getView().getContext(), list, new ProblemRequestAdapter.ProblemRequestAdapterEvent() {
            @Override
            public void OnMainLayoutClick(int id) {
                event.OnRequestClickListener(id);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

    }

    public void setEvent(OnRequestListListener event) {
        this.event = event;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setList(List<ProblemRequest> list) {
        this.list = list;
        adapter.setRequestList(list);
        txtCount.setText(list.size() + "");
        adapter.notifyDataSetChanged();
    }

    public void openClose() {
        if(adapter.getItemCount() > 0) {
            if(isOpen) {
                ivUp.setVisibility(View.VISIBLE);
                ivDown.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                isOpen = false;
            }else {
                ivUp.setVisibility(View.GONE);
                ivDown.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                isOpen = true;
            }
        }
    }

    public interface OnRequestListListener {
        void OnRequestClickListener(int id);
        LiveData<List<ProblemRequest>> OnScrollToBottom();
        void OnGetRequestSuccess(boolean isSuccess);
    }

}
