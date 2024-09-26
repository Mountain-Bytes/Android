package com.example.bookswap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    private Context context;
    private List<Community> channeList;

    public ChannelAdapter(Context context, List<Community> channeList){
        this.channeList = channeList;
        this.context = context;

    }

    @NonNull
    @Override
    public ChannelAdapter.ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_channel,parent,false);
        return  new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        Community channel = channeList.get(position);
        holder.textView.setText(channel.getName());

        Glide.with(context).load(channel.getImageUrl()).circleCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
       return channeList.size();
    }


    public static class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ChannelViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.channel_name);
            imageView = itemView.findViewById(R.id.channel_img);
        }


    }
}

