package com.example.bookswap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class SearchByAuthor extends Fragment {

    private static final String API_URL = "https://aayushisingh04.pythonanywhere.com/search";

    private RecyclerView recyclerPopularAuthors;
    private RecyclerView recyclerAuthorsForYou;
    private RecyclerView recyclerSearchResults;
    private AuthorAdapter popularAuthorsAdapter;
    private AuthorAdapter authorsForYouAdapter;
    private BookAdapter searchResultsAdapter;


    private EditText authorName;
    private Button searchButton;
    private Button clearButton;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_by_author, container, false);

        // Initialize RecyclerViews
        recyclerPopularAuthors = view.findViewById(R.id.recycler_popular_authors);
        recyclerAuthorsForYou = view.findViewById(R.id.recycler_authors_for_you);
        recyclerSearchResults = view.findViewById(R.id.recycler_search_result_author);
        authorName = view.findViewById(R.id.et_author_name);
        searchButton = view.findViewById(R.id.btn_search_author);
        clearButton = view.findViewById(R.id.btn_clear_results);
        progressBar = view.findViewById(R.id.progress_bar);

        // Hide the clear button and progress bar initially
        clearButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);


        // Set Layout Managers
        recyclerPopularAuthors.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL, false));
        recyclerAuthorsForYou.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL, false));
        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));


        // Load data
        List<Author> popularAuthors = loadPopularAuthors();
        List<Author> authorsForYou = loadAuthorsForYou();

        // Set up adapters
        popularAuthorsAdapter = new AuthorAdapter(getContext(), popularAuthors);
        authorsForYouAdapter = new AuthorAdapter(getContext(), authorsForYou);

        // Set adapters to RecyclerViews
        recyclerPopularAuthors.setAdapter(popularAuthorsAdapter);
        recyclerAuthorsForYou.setAdapter(authorsForYouAdapter);

        // Set up search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String author = authorName.getText().toString();

                // log the author name to check if it is correct
                Log.d("SearchByAuthor", "Author name: " + author);

                progressBar.setVisibility(View.VISIBLE);

                Observable.fromCallable(() -> searchBooks(author))
                        .subscribeOn(Schedulers.io()) // Perform the search on an IO thread
                        .observeOn(AndroidSchedulers.mainThread()) // Observe the results on the main thread
                        .subscribe(
                                books -> {
                                    // Hide progress bar
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("SearchByAuthor", "Books: " + books);
                                    // OnSuccess: Update the UI with the search results and show the clear button
                                    searchResultsAdapter = new BookAdapter(getContext(), books);
                                    recyclerSearchResults.setAdapter(searchResultsAdapter);
                                    clearButton.setVisibility(View.VISIBLE);
                                    searchButton.setVisibility(View.GONE);
                                },
                                throwable -> {
                                    // Hide progress bar and show error message
                                    progressBar.setVisibility(View.GONE);
                                    Log.e("SearchByAuthor", "Error fetching data", throwable);
                                    Toast toast = Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                        );


            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear search results and hide the clear button
                searchResultsAdapter = new BookAdapter(getContext(), new ArrayList<>());
                recyclerSearchResults.setAdapter(searchResultsAdapter);
                recyclerPopularAuthors.setAdapter(popularAuthorsAdapter);
                recyclerAuthorsForYou.setAdapter(authorsForYouAdapter);
                searchButton.setVisibility(View.VISIBLE);

                // Hide the clear button and restore the initial state (optional)
                clearButton.setVisibility(View.GONE);
            }
        });

        return view;
    }






    // Mock method to load popular authors data
    private List<Author> loadPopularAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("J. K. Rowling", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcS3bDP-AbnH7l2pjO-SwJkEFQu9dxDE6V8wyJ_5x_lfkVpTMA80"));
        authors.add(new Author("Jane Austen", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSbTRdUEIdnEchoXZV9pJe-Bky5CG6r5AmAVFdd_Uvrd3gWv2Kk"));
        authors.add(new Author("Stephen King", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFNn6FcqJgnTCBDjX9vK_qRnle-dHO1Jp7uEEqISfNOm8bbjoW"));
        authors.add(new Author("William Shakespeare", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqYviiqeC2-wZiAudkPo7rxFzskS0w8TEBhay0r09IPAP1kL4f"));
        // Add more authors as needed
        return authors;
    }

    // Mock method to load authors for you data
    private List<Author> loadAuthorsForYou() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Author A", "https://picsum.photos/200/300?random=5"));
        authors.add(new Author("Author B", "https://picsum.photos/200/300?random=6"));
        authors.add(new Author("Author 1", "https://picsum.photos/200/300?random=7"));
        authors.add(new Author("Author 2", "https://picsum.photos/200/300?random=8"));
        // Add more authors as needed
        return authors;
    }


    private List<Book> searchBooks(String author) throws IOException {

        //log
        Log.d("SearchByAuthor", "searchBooks: author: " + author);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"author\":\"" + author + "\"}");
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        //log
        Log.d("SearchByAuthor", "searchBooks: response: " + response);

        if (response.isSuccessful()) {
            assert response.body() != null;
            String jsonResponse = response.body().string();
            Log.d("SearchByAuthor", "searchBooks: jsonResponse: " + jsonResponse);
            return parseBooks(jsonResponse);
        } else {
            Log.e("SearchByAuthor", "searchBooks: Error: " + response);
            throw new IOException("Unexpected code " + response);
        }
    }

    private List<Book> parseBooks(String jsonResponse) {
        List<Book> bookList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        try {
            // Parse the entire response as a JsonArray
            JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

            // Loop through each element in the JsonArray
            for (JsonElement jsonElement : jsonArray) {
                try {
                    // Try to parse each individual book entry
                    Book book = gson.fromJson(jsonElement, Book.class);
                    bookList.add(book); // Add to list if successfully parsed
                } catch ( JsonParseException e) {
                    // Catch parsing exceptions for individual entries and log them
                    Log.e("parseBooks", "Error parsing book entry: " + jsonElement.toString(), e);
                    // Continue to the next entry
                }
            }
        } catch (JsonParseException e) {
            // Handle the case where the entire response is invalid
            Log.e("parseBooks", "Error parsing the entire response", e);
        }

        return bookList; // Return the list with all successfully parsed books
    }

}
