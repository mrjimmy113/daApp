package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailImageFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageDetailAdapter adapter;
    private List<String> images;

    public RequestDetailImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyc_img);
        if(images != null) {
            setDataOnView();
        }
    }

    public void setDate(List<String> images) {
        this.images = images;
        if(getView() != null) {
            setDataOnView();
        }

    }

    public void setDataOnView() {
        if(recyclerView != null) {
            adapter = new ImageDetailAdapter(getContext(),images);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        }
    }
}
