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
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;

    // Constructor
    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the data for the specific position
        Book book = bookList.get(position);

        // Bind the data to the views
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthors()[0]); // Assuming a single author

        String thumbnailUrl = book.getImage_links() != null ? book.getImage_links().getThumbnail() : null;
        String smallThumbnailUrl = book.getImage_links() != null ? book.getImage_links().getSmallThumbnail() : null;
        if (thumbnailUrl != null ) {
            // Log the URL to check if it is correct
            Log.d("BookAdapter", "Thumbnail URL: " + thumbnailUrl);
            Glide.with(context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.favourite_book) // Placeholder image while loading
                    .error(R.drawable.find_book)         // Error image if loading fails
                    .into(holder.thumbnailImageView);

        } else if (smallThumbnailUrl != null) {
            Glide.with(context)
                    .load(smallThumbnailUrl)
                    .placeholder(R.drawable.favourite_book) // Placeholder image while loading
                    .error(R.drawable.find_book)         // Error image if loading fails
                    .into(holder.thumbnailImageView);
        } else {

            // Handle the case where the thumbnail URL is null (e.g., set a placeholder image)
            holder.thumbnailImageView.setImageResource(R.drawable.bookread);
        }

        // Handle item click to show book details
        holder.itemView.setOnClickListener(v -> {
            // Create and show the dialog fragment with book details
            BookDetailsDialogFragment dialogFragment = new BookDetailsDialogFragment(book);
            dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "bookDetails");
        });

    }

    @Override
    public int getItemCount() {
        // Return the size of the list
        return bookList.size();
    }

    // ViewHolder class to hold the views
    public static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView authorTextView;
        ImageView thumbnailImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            titleTextView = itemView.findViewById(R.id.book_name);
            authorTextView = itemView.findViewById(R.id.book_author);
            thumbnailImageView = itemView.findViewById(R.id.book_image);
        }
    }
}
