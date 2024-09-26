package com.example.bookswap;

import android.content.Context;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookInfo extends AppCompatActivity {

    private RecyclerView rec;
    private BookInfoAdapter adapter;
    private OkHttpClient client;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        rec = findViewById(R.id.bookinfo_recyclerview);
        rec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        client = new OkHttpClient();
        gson = new Gson();

        // Replace these with actual values
        String userId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        double latitude = 23.024349;
        double longitude = 72.53;
        int radius = 500;

        fetchBookData(userId, latitude, longitude, radius);

    }


    private void fetchBookData(String userId, double latitude, double longitude, int radius) {
        // The URL to the API
        String url = "https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/explore";

        // Create the JSON payload
        JsonObject payload = new JsonObject();
        payload.addProperty("user_id", userId);
        payload.addProperty("latitude", latitude);
        payload.addProperty("longitude", longitude);
        payload.addProperty("radius", radius);

        // Set up the request
        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // Make the API call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(BookInfo.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    Log.e("BookInfo", "API request failed", e);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(BookInfo.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    });
                    throw new IOException("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                List<Info> books = parseResponse(jsonResponse);

                runOnUiThread(() -> {
                    // Update the adapter with the new data
                    adapter = new BookInfoAdapter(BookInfo.this, books);
                    rec.setAdapter(adapter);
                });
            }
        });
    }

    // Parse the API response to extract book information
    private List<Info> parseResponse(String jsonResponse) {
        List<Info> bookList = new ArrayList<>();

        JsonArray booksArray = JsonParser.parseString(jsonResponse).getAsJsonArray();


        for (int i = 0; i < booksArray.size(); i++) {
            JsonObject bookObject = booksArray.get(i).getAsJsonObject();

            String books = bookObject.get("books").getAsString();
            String name = bookObject.get("name").getAsString();
            String user_id = bookObject.get("user_id").getAsString();

            // Assuming Info constructor takes author, thumbnail, and cover
            bookList.add(new Info(books, name, user_id));
        }

        return bookList;
    }
}