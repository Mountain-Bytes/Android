package com.example.bookswap;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

public class Auth0LoginActivity extends AppCompatActivity {

    private Auth0 account;
    Button loginButton;
    Button logoutButton;
    Button showProfileButton;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth0_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        account = new Auth0(
                "0hO6FIx1UNfJ44hI7ZCFYm6boNiznOJ7",
                "dev-pnvbwspnhed75xjv.us.auth0.com"
        );

        loginButton = findViewById(R.id.login_button);
        logoutButton = findViewById(R.id.logout_button);
        showProfileButton = findViewById(R.id.profile_button);

        loginButton.setOnClickListener(v -> loginWithBrowser());
        logoutButton.setOnClickListener(v -> logout());
        showProfileButton.setOnClickListener(v -> showUserProfile(accessToken));


    }


    private void loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(account)
                .withScheme("demo")
                .withScope("openid profile email")
                // Launch the authentication passing the callback where the results will be received
                .start(this, new Callback<Credentials, AuthenticationException>() {
                    // Called when there is an authentication failure
                    @Override
                    public void onFailure(@NonNull AuthenticationException exception) {
                        Log.d("Auth0", "Authentication Failed", exception);
                        Toast.makeText(Auth0LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }

                    // Called when authentication completed successfully
                    @Override
                    public void onSuccess(Credentials credentials) {
                        // Get the access token from the credentials object.
                        // This can be used to call APIs
                        accessToken = credentials.getAccessToken();
                        Log.d("Auth0", "Authentication Success with token: " + accessToken);
                        Toast.makeText(Auth0LoginActivity.this, "Authentication Success", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void logout() {
        WebAuthProvider.logout(account)
                .withScheme("demo")
                .start(this, new Callback<Void, AuthenticationException>() {
                    @Override
                    public void onSuccess(Void payload) {
                        Toast.makeText(Auth0LoginActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Log.d("Auth0", "Logout Failed", error);
                        Toast.makeText(Auth0LoginActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showUserProfile(String accessToken) {
        if (accessToken == null) {
            Toast.makeText(Auth0LoginActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }
        AuthenticationAPIClient client = new AuthenticationAPIClient(account);

        // With the access token, call `userInfo` and get the profile from Auth0.
        client.userInfo(accessToken)
                .start(new Callback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onFailure(AuthenticationException exception) {
                        Log.d("Auth0", "Failed to get user profile", exception);
                        Toast.makeText(Auth0LoginActivity.this, "Failed to get user profile", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(UserProfile profile) {
                        // We have the user's profile!
                        String email = profile.getEmail();
                        String name = profile.getName();
                        Toast.makeText(Auth0LoginActivity.this, "User Profile: " + name + " - " + email, Toast.LENGTH_SHORT).show();
                    }
                });
    }



}