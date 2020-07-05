package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Estimate;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.ultis.NetworkClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<ReceiveMessage> messages = new ArrayList<>();
    private String otherName;
    private boolean isExpert;
    private StatusEnum statusEnum;
    private IMessageAdapter iMessageAdapter;

    public MessageAdapter(Context mContext, List<ReceiveMessage> messages, String otherName, boolean isExpert, StatusEnum statusEnum) {
        this.mContext = mContext;
        this.messages = messages;
        this.otherName = otherName;
        this.isExpert = isExpert;
        this.statusEnum = statusEnum;
    }

    public MessageAdapter(Context mContext, List<ReceiveMessage> messages, String otherName, boolean isExpert, StatusEnum statusEnum, IMessageAdapter iMessageAdapter) {
        this.mContext = mContext;
        this.messages = messages;
        this.otherName = otherName;
        this.isExpert = isExpert;
        this.statusEnum = statusEnum;
        this.iMessageAdapter = iMessageAdapter;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;

    }

    public void setMessages(List<ReceiveMessage> messages) {
        this.messages = messages;

    }

    public void addMessage(List<ReceiveMessage> messages) {
        this.messages.addAll(messages);
    }

    public List<ReceiveMessage> getMessages() {
        return messages;
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
                holder.layout_estimate_yes.setVisibility(View.GONE);
                holder.layout_estimate.setVisibility(View.GONE);
                holder.txtMessage.setText(message.getMessage());
                holder.txtTime.setText(message.getTime());
                if(isExpert) {
                    if(!message.isExpert()) {
                        holder.txtName.setText(otherName + " ");
                        holder.container1.setGravity(Gravity.START);
                        holder.container2.setGravity(Gravity.START);
                    }else {
                        holder.txtName.setText("You ");
                        holder.container1.setGravity(Gravity.END);
                        holder.container2.setGravity(Gravity.END);
                    }
                }else {
                    if(message.isExpert()) {
                        holder.txtName.setText(otherName + " ");
                        holder.container1.setGravity(Gravity.START);
                        holder.container2.setGravity(Gravity.START);
                    }else {
                        holder.txtName.setText("You ");
                        holder.container1.setGravity(Gravity.END);
                        holder.container2.setGravity(Gravity.END);
                    }
                }
                break;
            }
            case ESTIMATE: {
                holder.container2.setVisibility(View.GONE);
                holder.layout_estimate_yes.setVisibility(View.GONE);
                if(statusEnum == StatusEnum.ACCEPTED) {
                    Estimate estimate = NetworkClient.getInstance().getGson().fromJson(message.getMessage(),Estimate.class);
                    holder.layout_estimate.setVisibility(View.VISIBLE);
                    int hour =(int) estimate.getHour();
                    int minute = (int) ((estimate.getHour() - hour) * 60) ;
                    holder.txtHourEstimate.setText(hour + " Hour " + minute + " Minute");
                    holder.txtTotalEstimate.setText(estimate.getTotal() + " VND");
                    holder.btnAcceptEstimate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(statusEnum.equals(StatusEnum.ACCEPTED)) {
                                iMessageAdapter.OnEstimateYesClick(position);
                            }
                        }
                    });
                    if(isExpert) holder.iv_wait.setVisibility(View.VISIBLE);
                    else holder.btnAcceptEstimate.setVisibility(View.VISIBLE);
                }
                break;
            }
            case ESTIMATE_YES: {
                Estimate estimate = NetworkClient.getInstance().getGson().fromJson(message.getMessage(),Estimate.class);
                holder.layout_estimate_yes.setVisibility(View.VISIBLE);
                holder.layout_estimate.setVisibility(View.GONE);
                holder.container2.setVisibility(View.GONE);
                holder.txtHourEstimateYes.setText(estimate.getHour() + "");
                holder.txtTotalEstimateYes.setText(estimate.getTotal() + " VND");
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ReceiveMessage message) {
        messages.add(0,message);

    }

    public interface IMessageAdapter {
        void OnEstimateYesClick(int pos);
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
        ImageView iv_wait;


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
            iv_wait = itemView.findViewById(R.id.iv_wait);
        }
    }

}
