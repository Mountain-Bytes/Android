package com.example.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashboardActivity extends AppCompatActivity {

    ImageButton profileButton;

    CardView profileCard;

    CardView searchCard;


    CardView learncard;

    CardView practiceCard;

    CardView helpCard;

    ImageButton chatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            startActivity(ProfileActivity.class);
        });

        profileCard = findViewById(R.id.profileCard);
        profileCard.setOnClickListener(v -> {
            startActivity(ProfileActivity.class);
        });

        searchCard = findViewById(R.id.BookSearchCard);
        searchCard.setOnClickListener(v -> {
            startActivity(BookSearchActivity.class);
        });


        learncard = findViewById(R.id.interestsCard);
        learncard.setOnClickListener(v -> {
            startActivity(CommunityActivity.class);
        });

        practiceCard = findViewById(R.id.practiceCard);
        practiceCard.setOnClickListener(v->{
            startActivity(BookInfo.class);
        });
      
      
        chatButton = findViewById(R.id.chat);
        chatButton.setOnClickListener(v -> {
            startActivity(MessageActivity.class);

        });

        helpCard = findViewById(R.id.helpCard);
        helpCard.setOnClickListener(v -> {
            startActivity(Auth0LoginActivity.class);
        });

    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}