package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Major;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MajorSelectAdapter extends RecyclerView.Adapter<MajorSelectAdapter.ViewHolder> {
    private Context mContext;
    private List<Major> majors = new ArrayList<>();
    private List<Major> selected = new ArrayList<>();

    public MajorSelectAdapter(Context mContext, List<Major> majors, List<Major> selected) {
        this.mContext = mContext;
        this.majors = majors;
        this.selected = selected;
    }

    public List<Major> getSelected() {
        return selected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_major,parent,false);
        return new MajorSelectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Major major = majors.get(position);
        holder.txtMajor.setOnClickListener(v -> {
            if(selected.contains(major)) {
                selected.remove(major);
                holder.txtMajor.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                holder.layoutMajorItem.setBackgroundColor(0x00000000);

            }else {
                selected.add(major);
                holder.txtMajor.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.layoutMajorItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            }
        });
        holder.txtMajor.setText(major.getMajor());
        if(selected.contains(major)) {
            holder.txtMajor.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.layoutMajorItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            holder.txtMajor.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.layoutMajorItem.setBackgroundColor(0x00000000);
        }
    }

    @Override
    public int getItemCount() {
        return majors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMajor;
        LinearLayout layoutMajorItem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMajor = itemView.findViewById(R.id.txtMajorItem);
            layoutMajorItem = itemView.findViewById(R.id.layout_major_item);
        }
    }
}
