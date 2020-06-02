package com.quang.daapp.ui.viewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quang.daapp.R;
import com.quang.daapp.ultis.NetworkClient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImgChooserAdapter extends RecyclerView.Adapter<ImgChooserAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> imgURL;
    ArrayList<String> oldImgDelete = new ArrayList<>();
    ArrayList<String> newImg = new ArrayList<>();
    OnItemRemove onItemRemove;

    public ImgChooserAdapter(Context mContext, ArrayList<String> imgURL, OnItemRemove onItemRemove) {
        this.mContext = mContext;
        this.imgURL = imgURL;
        this.onItemRemove = onItemRemove;
    }

    public void addNewImg(String string) {
        newImg.add(string);
    }

    public ArrayList<String> getNewImg() {
        return newImg;
    }

    public ArrayList<String> getOldImgDelete() {
        return oldImgDelete;
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
                if(imgURL.get(position).contains(NetworkClient.BASE_URL)) {
                    oldImgDelete.add(imgURL.get(position).split("=")[1]);
                }else {
                    newImg.remove(newImg.indexOf(imgURL.get(position)));
                }
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
