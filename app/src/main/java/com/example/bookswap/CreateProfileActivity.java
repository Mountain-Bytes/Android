package com.example.bookswap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CreateProfileActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhone, etBio;
    private ImageView imageView;
    private Button btnSubmit;
    private Bitmap selectedImageBitmap;
    private double latitude = 0.0, longitude = 0.0; // Set default values for now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        // Initialize views
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etBio = findViewById(R.id.et_bio);
        btnSubmit = findViewById(R.id.btn_submit);


        // Set OnClickListener for the submit button
        btnSubmit.setOnClickListener(v -> {
            // Collect data from the input fields
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String bio = etBio.getText().toString().trim();

            // Validate data before sending
            if (validateInputs(firstName, lastName, email, phone, bio)) {
                // Create a profile data object (optional)
                ProfileData profileData = new ProfileData(firstName, lastName, email, phone, bio);

                // Send data to the API
                new CreateProfileTask().execute(profileData);

                Toast.makeText(CreateProfileActivity.this, "Profile created successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CreateProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(CreateProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to validate input fields
    private boolean validateInputs(String firstName, String lastName, String email, String phone, String bio) {
        return !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !bio.isEmpty();
    }

    // AsyncTask to send data to the API
    private class CreateProfileTask extends AsyncTask<ProfileData, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ProfileData... params) {
            ProfileData profileData = params[0];
            try {
                // API URL
                URL url = new URL("https://2ba3-2401-4900-50ab-a2ec-f1dc-b1b5-e703-2f8c.ngrok-free.app/create_profile");

                // Create HTTP connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Create JSON object with profile data
                JSONObject profileJson = new JSONObject();
                profileJson.put("uuid", profileData.getEmail()); // You can use a proper UUID here if needed
                profileJson.put("first_name", profileData.getFirstName());
                profileJson.put("last_name", profileData.getLastName());
                profileJson.put("latitude", latitude);
                profileJson.put("longitude", longitude);
                profileJson.put("email", profileData.getEmail());
                profileJson.put("phone", profileData.getPhone());
                profileJson.put("bio", profileData.getBio());

                // Write data to the request body
                OutputStream os = conn.getOutputStream();
                os.write(profileJson.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                // Check the response code
                int responseCode = conn.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;

            } catch (Exception e) {
                Log.e("CreateProfileTask", "Error sending profile data", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(CreateProfileActivity.this, "Profile created successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateProfileActivity.this, "Failed to create profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Profile data class to hold input values
    private static class ProfileData {
        private String firstName, lastName, email, phone, bio;

        public ProfileData(String firstName, String lastName, String email, String phone, String bio) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.bio = bio;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getBio() {
            return bio;
        }
    }

}