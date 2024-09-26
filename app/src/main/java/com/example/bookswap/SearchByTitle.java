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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class SearchByTitle extends Fragment {

    private static final String API_URL ="https://aayushisingh04.pythonanywhere.com/search_by_title";

    private RecyclerView recyclerSearchResults;
    private TitleAdapter searchResultAdapter;
    private EditText titlename;
    private Button searchButton;
    private Button clearButton;
    private ProgressBar progressBar;

    public SearchByTitle() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_by_title, container, false);

        recyclerSearchResults = view.findViewById(R.id.search_by_title_result_recyclerview);
        titlename = view.findViewById(R.id.et_title_name);
        searchButton = view.findViewById(R.id.btn_search_title);
        clearButton = view.findViewById(R.id.btn_clear_resultsTitle);
        progressBar = view.findViewById(R.id.progress_bar);

        // Hide the clear button and progress bar initially
        clearButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titlename.getText().toString();

                Log.d("SeachByTitle", "Titlename" + title);

                progressBar.setVisibility(View.VISIBLE);

                Observable.fromCallable(() -> SearchTitle(title))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                titles -> {
                                    //Hide Progress bar
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("SearchByTitle","Title "+title);
                                    // OnSuccess: Update the UI with the search results and show the clear button
                                    searchResultAdapter = new TitleAdapter(getContext(), titles);
                                    recyclerSearchResults.setAdapter(searchResultAdapter);
                                    clearButton.setVisibility(View.VISIBLE);
                                    searchButton.setVisibility(View.GONE);
                                },
                                throwable -> {
                                    // Hide progress bar and show error message
                                    progressBar.setVisibility(View.GONE);
                                    Log.e("SearchByTitle", "Error fetching data", throwable);
                                    Toast toast = Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                        );


            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear search results and hide the clear button
                searchResultAdapter = new TitleAdapter(getContext(),new ArrayList<>());
                recyclerSearchResults.setAdapter(searchResultAdapter);
                searchButton.setVisibility(View.VISIBLE);

                // Hide the clear button and restore the initial state (optional)
                clearButton.setVisibility(View.GONE);

            }
        });

        return view;
    }

    private List<Book> SearchTitle(String title) throws IOException{

        Log.d("SearchByTitle","searchBooks: author"+title);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,"{\"title\":\"" + title + "\"}");
        Request request = new Request.Builder()
                .url(SearchByTitle.API_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .build();

        Response response = client.newCall(request).execute();

        //log
        Log.d("SearchByTitle","searchTitle: title: "+ response);


        if(response.isSuccessful()){
            assert  response.body() != null;
            String jsonResponse = response.body().string();
            Log.d("SearchByTitle", "searchTitle: jsonResponse: " + jsonResponse);
            return parseTitle(jsonResponse);
        }else{
            Log.e("SearchByTitle", "searchTitle: Error: " + response);
            throw new IOException("Unexpected code"+response);
        }



    }

    private List<Book> parseTitle(String jsonResponse) {
        List<Book> titleList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        try{
            // Parse the entire response as a JsonArray
            JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

            // Loop through each element in the JsonArray
            for (JsonElement jsonElement : jsonArray) {
                try {
                    // Try to parse each individual book entry
                    Book book = gson.fromJson(jsonElement, Book.class);
                    titleList.add(book); // Add to list if successfully parsed
                } catch ( JsonParseException e) {
                    // Catch parsing exceptions for individual entries and log them
                    Log.e("parseTitle", "Error parsing title entry: " + jsonElement.toString(), e);
                    // Continue to the next entry
                }
            }

        } catch (JsonParseException e) {
            // Handle the case where the entire response is invalid
            Log.e("ParseTitle", "Error parsing the entire response", e);
        }

        return titleList;
    }
}


