package com.example.bookswap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder> {

    private Context context;
    private List<Book> titleList;

    public  TitleAdapter(Context context,List<Book> titleList){
        this.context = context;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {

        Book title = titleList.get(position);

        // Bind the data to the views
        holder.titleTextView.setText(title.getTitle());
        holder.authorTextView.setText(title.getAuthors()[0]);

        String thumbnailUrl = title.getImage_links() != null? title.getImage_links().getThumbnail():null;
        String smallThumbnailUrl = title.getImage_links() != null?title.getImage_links().getSmallThumbnail():null;
        if(thumbnailUrl != null){
            // Log the URL to check if it is correct
            Log.d("TitleAdapter","Thumbnail URL: "+ thumbnailUrl);
            Glide.with(context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.favourite_book)
                    .error(R.drawable.favourite_book)
                    .into(holder.thumbnailImageView);
        }else if(smallThumbnailUrl != null){
            Glide.with(context)
                    .load(smallThumbnailUrl)
                    .placeholder(R.drawable.favourite_book)
                    .error(R.drawable.find_book)
                    .into(holder.thumbnailImageView);

        }else{
            // Handle the case where the thumbnail URL is null (e.g., set a placeholder image)
            holder.thumbnailImageView.setImageResource(R.drawable.bookread);

        }

        // Handle item click to show book details
        holder.itemView.setOnClickListener(v -> {
            // Create and show the dialog fragment with book details
            BookDetailsDialogFragment dialogFragment = new BookDetailsDialogFragment(title);
            dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "bookDetails");
        });


    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView authorTextView;
        ImageView thumbnailImageView;

        public  TitleViewHolder(@NonNull View itemView){
            super(itemView);

            // Initialize the views
            titleTextView = itemView.findViewById(R.id.book_name);
            authorTextView = itemView.findViewById(R.id.book_author);
            thumbnailImageView = itemView.findViewById(R.id.book_image);


        }
    }
}
