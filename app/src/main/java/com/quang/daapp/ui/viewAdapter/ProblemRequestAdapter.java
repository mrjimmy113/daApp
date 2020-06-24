package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProblemRequestAdapter extends RecyclerView.Adapter<ProblemRequestAdapter.ViewHolder> {
    Context mContext;
    List<ProblemRequest> requestList;
    ProblemRequestAdapterEvent event;

    public ProblemRequestAdapter(Context mContext, List<ProblemRequest> requestList, ProblemRequestAdapterEvent event) {
        this.mContext = mContext;
        this.requestList = requestList;
        this.event = event;
    }

    public List<ProblemRequest> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<ProblemRequest> requestList) {
        this.requestList = requestList;
    }

    public void addRequestList(List<ProblemRequest> list) {
        int lastIndex = this.requestList.size();
        this.requestList.addAll(list);
        notifyItemRangeInserted(lastIndex, lastIndex + this.requestList.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_problem_request,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ProblemRequest request = requestList.get(position);
        holder.txtTitle.setText(request.getTitle());
        holder.txtDescription.setText(request.getDescription());
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        holder.txtCreatedDate.setText(format.format(request.getCreatedDate()));
        holder.txtEndDate.setText(format.format(request.getDeadlineDate()));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.OnMainLayoutClick(requestList.get(position).getRequestId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        TextView txtTitle;
        TextView txtDescription;
        TextView txtCreatedDate;
        TextView txtEndDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtCreatedDate = itemView.findViewById(R.id.txtCreatedDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            mainLayout = itemView.findViewById(R.id.layout_problem_request);
        }
    }

    public interface  ProblemRequestAdapterEvent {
        void OnMainLayoutClick(int id);
    }
}
