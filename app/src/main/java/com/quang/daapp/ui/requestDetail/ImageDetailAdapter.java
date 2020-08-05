package com.quang.daapp.ui.requestDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.quang.daapp.R;
import com.quang.daapp.ultis.NetworkClient;
import com.squareup.picasso.Picasso;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageDetailAdapter extends RecyclerView.Adapter<ImageDetailAdapter.ViewHolder> {

    Context mContext;
    List<String> imgURL;

    public ImageDetailAdapter(Context context, List<String> images) {
        mContext = context;
        imgURL = images;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_img_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Picasso.get().load(NetworkClient.getImageUrl(imgURL.get(position))).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imgURL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }
}
