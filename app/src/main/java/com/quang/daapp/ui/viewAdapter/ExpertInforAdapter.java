package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.service.RetrofitClient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpertInforAdapter extends RecyclerView.Adapter<ExpertInforAdapter.ViewHolder> {

    Context mContext;
    OnExpertInforInterface event;

    public List<Expert> getExpertList() {
        return expertList;
    }

    public void setExpertList(List<Expert> expertList) {
        this.expertList = expertList;
    }

    List<Expert> expertList;

    public ExpertInforAdapter(Context mContext, List<Expert> expertList) {
        this.mContext = mContext;
        this.expertList = expertList;
    }

    public ExpertInforAdapter(Context mContext, List<Expert> expertList,OnExpertInforInterface event) {
        this.mContext = mContext;
        this.event = event;
        this.expertList = expertList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_expert_profile,parent,false);
        return new ExpertInforAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Expert expert = expertList.get(position);
        if(expert.getImgName() != null && !expert.getImgName().trim().isEmpty()) {
            Glide.with(mContext).load(RetrofitClient.getImageUrl(expert.getImgName())).into(holder.iv_avatar);
        }
        holder.txtFullName.setText(expert.getFullName());
        holder.txtMajor.setText(expert.getMajor().getMajor());
        holder.txtFee.setText(expert.getFeePerHour() + "");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.OnProfileClickListener(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return expertList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_avatar;
        TextView txtFullName;
        TextView txtMajor;
        TextView txtFee;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtMajor = itemView.findViewById(R.id.txtMajor);
            txtFee = itemView.findViewById(R.id.txtFee);
            linearLayout = itemView.findViewById(R.id.layout_problem_request);
        }
    }

    public interface OnExpertInforInterface {
        void OnProfileClickListener(int position);

    }


}
