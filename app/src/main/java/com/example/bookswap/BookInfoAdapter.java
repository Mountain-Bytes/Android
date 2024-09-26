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

public class BookInfoAdapter extends RecyclerView.Adapter<BookInfoAdapter.InfoViewHolder> {

    private Context context;
    private List<Info> InfoList;

    public BookInfoAdapter(Context context, List<Info> InfoList){
        this.context = context;
        this.InfoList = InfoList;
    }

    @NonNull
    @Override
    public BookInfoAdapter.InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed,parent,false);
        return  new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookInfoAdapter.InfoViewHolder holder, int position) {
        Info info = InfoList.get(position);
        holder.book_name.setText(info.getName());
        holder.user_mail.setText(info.getUser_id());
        holder.user_name.setText(info.getname());

    }

    @Override
    public int getItemCount() {
        return InfoList.size();
    }

    public  static class InfoViewHolder extends  RecyclerView.ViewHolder{
        TextView book_name;
        TextView user_name;
        TextView user_mail;

        public  InfoViewHolder(@NonNull View itemView){
            super(itemView);

            book_name = itemView.findViewById(R.id.book_title);
            user_name = itemView.findViewById(R.id.username);
            user_mail = itemView.findViewById(R.id.user_location);

        }
    }
}
