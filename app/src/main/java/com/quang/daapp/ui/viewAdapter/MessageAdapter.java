package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.Message;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context mContext;
    List<Message> messages;
    String otherName;

    public MessageAdapter(Context mContext, List<Message> messages, String otherName) {
        this.mContext = mContext;
        this.messages = messages;
        this.otherName = otherName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_expert_profile,parent,false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  Message message = messages.get(position);
        holder.txtMessage.setText(message.getMessage());
        holder.txtTime.setText(message.getTime().toString());
        if(!message.isYou()) {
            holder.txtName.setText(otherName);
            holder.container1.setGravity(Gravity.START);
            holder.container2.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container1;
        LinearLayout container2;
        TextView txtName;
        TextView txtTime;
        TextView txtMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container1 = itemView.findViewById(R.id.mes_contain_1);
            container2 = itemView.findViewById(R.id.mes_contain_2);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtMesTime);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }

}
