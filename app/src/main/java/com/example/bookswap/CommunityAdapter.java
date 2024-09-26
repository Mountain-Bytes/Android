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

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private Context context;
    private List<Community> ComList;

    public CommunityAdapter(Context context, List<Community> ComList){
        this.context = context;
        this.ComList = ComList;

    }


    @NonNull
    @Override
    public CommunityAdapter.CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community,parent,false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.CommunityViewHolder holder, int position) {
        Community community = ComList.get(position);
        holder.textView.setText(community.getName());

        Glide.with(context).load(community.getImageUrl()).circleCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return ComList.size();
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.comm_name);
            imageView = itemView.findViewById(R.id.channel_image);

        }
    }
}
