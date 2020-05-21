package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quang.daapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImgChooserAdapter extends RecyclerView.Adapter<ImgChooserAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> imgURL;
    OnItemRemove onItemRemove;

    public ImgChooserAdapter(Context mContext, ArrayList<String> imgURL, OnItemRemove onItemRemove) {
        this.mContext = mContext;
        this.imgURL = imgURL;
        this.onItemRemove = onItemRemove;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_img_chooser,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(mContext).load(imgURL.get(position)).into(holder.imageView);
        holder.btnDelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnDelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemove.OnItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgURL.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageButton btnDelImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            btnDelImg = itemView.findViewById(R.id.btnDelImg);
        }
    }

    public interface OnItemRemove {
        void OnItemRemoved(int position);
    }
}
