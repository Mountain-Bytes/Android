package com.example.bookswap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Get the user email from the fireabase auth
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookAdapter = new BookAdapter(this,bookList);
        recyclerView.setAdapter(bookAdapter);

        // Determine the type of list to fetch
        String type = getIntent().getStringExtra("type");
        if (type != null) {
            switch (type) {
                case "library":
                    fetchLibraryData();
                    break;
                case "given":
                    fetchGivenListData();
                    break;
                case "taken":
                    fetchTakenListData();
                    break;
            }
        }

    }

    private void fetchLibraryData() {
        new FetchLibraryDataTask("https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/get_user_library" , userEmail).execute();
    }

    private void fetchGivenListData() {
        new FetchDataTask("https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/get_given_list/" + userEmail).execute();
    }

    private void fetchTakenListData() {
        new FetchDataTask("https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/get_taken_list/" + userEmail).execute();
    }

    private class FetchLibraryDataTask extends AsyncTask<Void, Void, String> {
        private String url;
        private String userEmail;

        FetchLibraryDataTask(String url , String userEmail) {
            this.url = url;
            this.userEmail = userEmail;
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            JSONObject payload = new JSONObject();
            try {
                payload.put("user_uuid", userEmail);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), payload.toString()))
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String string_response = response.body().string();
                    Log.d("response", string_response);
                    return string_response;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                parseAndPopulateData(result);
            } else {
                Toast.makeText(BookListActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        private String url;

        FetchDataTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            JSONObject payload = new JSONObject();
            try {
                payload.put("email", userEmail);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .build();




            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                parseAndPopulateData(result);
            } else {
                Toast.makeText(BookListActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
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

    private void parseAndPopulateData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            // Clear the previous data
            bookList.clear();

            if (jsonObject.has("library")) {
                // For library data
                JSONArray libraryArray = jsonObject.getJSONArray("library");




                bookList = parseBooks(libraryArray.toString());
            } else {
                if (jsonObject.has("given_list")) {
                    // For given list
                    JSONArray givenArray = jsonObject.getJSONArray("given_list");
                    bookList = parseBooks(givenArray.toString());
                } else if (jsonObject.has("taken_list")) {
                    // For taken list
                    JSONArray takenArray = jsonObject.getJSONArray("taken_list");
                    bookList = parseBooks(takenArray.toString());
                }
            }


            // Add the parsed data to the adapter
            bookAdapter = new BookAdapter(this, bookList);
            recyclerView.setAdapter(bookAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}