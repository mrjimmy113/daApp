package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Estimate;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.ultis.NetworkClient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context mContext;
    List<ReceiveMessage> messages;
    String otherName;
    boolean isExpert;

    public MessageAdapter(Context mContext, List<ReceiveMessage> messages, String otherName, boolean isExpert) {
        this.mContext = mContext;
        this.messages = messages;
        this.otherName = otherName;
        this.isExpert = isExpert;
    }

    public void setMessages(List<ReceiveMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_message,parent,false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReceiveMessage message = messages.get(position);
        switch (message.getType()) {
            case CHAT: {
                holder.container2.setVisibility(View.VISIBLE);
                holder.txtMessage.setText(message.getMessage());
                holder.txtTime.setText(message.getTime().toString());
                if(isExpert) {
                    if(!message.isExpert()) {
                        holder.txtName.setText(otherName);
                        holder.container1.setGravity(Gravity.START);
                        holder.container2.setGravity(Gravity.START);
                    }
                }else {
                    if(message.isExpert()) {
                        holder.txtName.setText(otherName);
                        holder.container1.setGravity(Gravity.START);
                        holder.container2.setGravity(Gravity.START);
                    }
                }
                break;
            }
            case ESTIMATE: {
                Estimate estimate = NetworkClient.getGson().fromJson(message.getMessage(),Estimate.class);
                holder.layout_estimate.setVisibility(View.VISIBLE);
                holder.txtHourEstimate.setText(estimate.getHour() + "");
                holder.txtTotalEstimate.setText(estimate.getTotal() + "");
                holder.btnAcceptEstimate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            }
            case ESTIMATE_YES: {
                Estimate estimate = NetworkClient.getGson().fromJson(message.getMessage(),Estimate.class);
                holder.layout_estimate_yes.setVisibility(View.VISIBLE);
                holder.txtHourEstimateYes.setText(estimate.getHour() + "");
                holder.txtTotalEstimateYes.setText(estimate.getTotal() + "");
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ReceiveMessage message) {
        messages.add(message);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container1;
        LinearLayout container2;
        LinearLayout layout_estimate;
        LinearLayout layout_estimate_yes;
        TextView txtName;
        TextView txtTime;
        TextView txtMessage;
        TextView txtHourEstimate;
        TextView txtTotalEstimate;
        ImageButton btnAcceptEstimate;
        TextView txtHourEstimateYes;
        TextView txtTotalEstimateYes;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container1 = itemView.findViewById(R.id.mes_contain_1);
            container2 = itemView.findViewById(R.id.mes_contain_2);
            layout_estimate = itemView.findViewById(R.id.layout_estimate);
            layout_estimate_yes = itemView.findViewById(R.id.layout_estimate_yes);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtMesTime);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtHourEstimate = itemView.findViewById(R.id.txtHourEstimate);
            txtHourEstimateYes = itemView.findViewById(R.id.txtHourEstimateYes);
            txtTotalEstimate = itemView.findViewById(R.id.txtTotalEstimate);
            txtTotalEstimateYes = itemView.findViewById(R.id.txtTotalEstimateYes);
            btnAcceptEstimate = itemView.findViewById(R.id.btnAcceptEstimate);
        }
    }

}
