package com.example.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    Button logoutButton;
    TextView nameTextView, emailTextView, phoneTextView, bioTextView;
    TextView tvLibraryCount, tvGivenCount, tvTakenCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        nameTextView = findViewById(R.id.tv_name);
        emailTextView = findViewById(R.id.tv_email);
        phoneTextView = findViewById(R.id.tv_phone);
        bioTextView = findViewById(R.id.tv_bio);
        tvLibraryCount = findViewById(R.id.tv_library_count);
        tvGivenCount = findViewById(R.id.tv_given_count);
        tvTakenCount = findViewById(R.id.tv_taken_count);

        // Set onClickListener for the three LinearLayouts
        LinearLayout libraryLayout = findViewById(R.id.library_layout);
        LinearLayout givenLayout = findViewById(R.id.given_layout);
        LinearLayout takenLayout = findViewById(R.id.taken_layout);

        libraryLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, BookListActivity.class);
            intent.putExtra("type", "library"); // Pass the type to indicate the source of data
            startActivity(intent);
        });

        givenLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, BookListActivity.class);
            intent.putExtra("type", "given");
            startActivity(intent);
        });

        takenLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, BookListActivity.class);
            intent.putExtra("type", "taken");
            startActivity(intent);
        });



        logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Fetch and display user profile
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                new GetProfileTask().execute(email);
            }
        }
    }

    // AsyncTask to fetch profile from the server
    private class GetProfileTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String email = params[0];
            OkHttpClient client = new OkHttpClient();

            String url = "https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/get_profile/" + email;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    return jsonObject.getJSONObject("profile"); // Returning the "profile" object from the JSON
                }
            } catch (IOException | JSONException e) {
                Log.e("ProfileActivity", "Error fetching profile data", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject profile) {
            if (profile != null) {
                try {
                    // Update the UI with fetched profile data
                    String firstName = profile.getString("first_name");
                    String lastName = profile.getString("last_name");
                    String email = profile.getString("email");
                    String phone = profile.getString("phone");
                    String bio = profile.getString("bio");
                    int libraryCount = profile.getJSONArray("library").length();
                    int givenCount = profile.getJSONArray("given_list").length();
                    int takenCount = profile.getJSONArray("taken_list").length();

                    nameTextView.setText(firstName + " " + lastName);
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);
                    bioTextView.setText(bio);
                    tvLibraryCount.setText(String.valueOf(libraryCount));
                    tvGivenCount.setText(String.valueOf(givenCount));
                    tvTakenCount.setText(String.valueOf(takenCount));

                } catch (JSONException e) {
                    Log.e("ProfileActivity", "Error parsing profile data", e);
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}