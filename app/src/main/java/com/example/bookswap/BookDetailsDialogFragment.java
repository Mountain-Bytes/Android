package com.example.bookswap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookDetailsDialogFragment extends DialogFragment {

    private Book book;
    private static final String ADD_TO_LIBRARY_URL = "https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/add_to_library";

    public BookDetailsDialogFragment(Book book) {
        this.book = book;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the custom dialog layout
        View view = inflater.inflate(R.layout.fragment_book_details_dialog, container, false);

        // Initialize the views
        TextView titleTextView = view.findViewById(R.id.dialog_book_title);
        TextView authorTextView = view.findViewById(R.id.dialog_book_author);
        TextView isbnTextView = view.findViewById(R.id.dialog_book_isbn);
        TextView categoriesTextView = view.findViewById(R.id.dialog_book_genre);
        TextView pageCountTextView = view.findViewById(R.id.dialog_book_page_count);
        TextView descriptionTextView = view.findViewById(R.id.dialog_book_description);
        ImageView bookImageView = view.findViewById(R.id.dialog_book_image);
        Button button1 = view.findViewById(R.id.button1);
        ImageButton closeButton = view.findViewById(R.id.close_dialog);

        // Set the data
        titleTextView.setText(book.getTitle());
        // Join authors names
        StringBuilder author = new StringBuilder();
        for (String a : book.getAuthors()) {
            author.append(a);
            if (!a.equals(book.getAuthors()[book.getAuthors().length - 1])) {
                author.append(", ");
            }
        }
        authorTextView.setText(author.toString());
        isbnTextView.setText(String.format("ISBN: %s", book.getIsbn()));
        descriptionTextView.setText(book.getDescription());
        pageCountTextView.setText(String.format("Page Count: %d", book.getPage_count()));
        StringBuilder categories = new StringBuilder();
        for (String c : book.getCategories()) {
            categories.append(c);
            if (!c.equals(book.getCategories()[book.getCategories().length - 1])) {
                categories.append(", ");
            }
        }
        categoriesTextView.setText(categories.toString());

        String thumbnailUrl = book.getImage_links() != null ? book.getImage_links().getThumbnail() : null;
        if (thumbnailUrl != null) {
            Glide.with(requireContext())
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.favourite_book)
                    .error(R.drawable.find_book)
                    .into(bookImageView);
        } else {
            bookImageView.setImageResource(R.drawable.bookread); // Default image if no thumbnail
        }

        // Handle button clicks
        button1.setOnClickListener(v -> {
            // Add the book to the Library
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                if (userEmail != null) {
                    new AddToLibraryTask(book.getTitle(), userEmail).execute();
                } else {
                    Toast.makeText(requireContext(), "Failed to get user email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    // AsyncTask to make network call and add book to library
    private class AddToLibraryTask extends AsyncTask<Void, Void, Boolean> {

        private String bookTitle;
        private String userEmail;

        public AddToLibraryTask(String bookTitle, String userEmail) {
            this.bookTitle = bookTitle;
            this.userEmail = userEmail;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            // Create JSON object for the payload
            JSONObject payload = new JSONObject();
            try {
                payload.put("book_title", bookTitle);
                payload.put("user_uuid", userEmail); // Using user email as uuid in this case
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            // Set media type and request body
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, payload.toString());

            // Create request
            Request request = new Request.Builder()
                    .url(ADD_TO_LIBRARY_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                // Execute the request and check response
                Response response = client.newCall(request).execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(requireContext(), "Book added to Library", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to add book to Library", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
