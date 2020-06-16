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
import android.widget.RatingBar;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequestDetail;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailInforFragment extends Fragment {

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtCreatedDate;
    private TextView txtEndDate;
    private RatingBar rbRating;
    private TextView txtFeedBack;
    private ProblemRequestDetail detail;



    public RequestDetailInforFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail_infor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTitle = view.findViewById(R.id.txtTitle);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtCreatedDate = view.findViewById(R.id.txtCreatedDate);
        txtEndDate = view.findViewById(R.id.txtEndDate);
        rbRating = view.findViewById(R.id.rb_rating);
        txtFeedBack = view.findViewById(R.id.txtFeedBack);
        if(detail != null) {
            setDateOnView();
        }



    }

    public void setData(ProblemRequestDetail detail) {
        this.detail = detail;
        if(getView() != null) {
            setDateOnView();
        }

    }

    private void setDateOnView() {
        txtTitle.setText(detail.getTitle());
        txtDescription.setText(detail.getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtCreatedDate.setText(dateFormat.format(detail.getCreatedDate()));
        txtEndDate.setText(dateFormat.format(detail.getDeadlineDate()));
        txtFeedBack.setText(detail.getFeedBack());
        rbRating.setRating(detail.getRating());
    }
}
