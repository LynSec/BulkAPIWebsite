package com.example.weshops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class About extends AppCompatActivity {

    Button fb, tw, qr, ld, gt, ig;
    TextView username, userEmail;
    String userName;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        fb = findViewById(R.id.fb);
        username = findViewById(R.id.textView14);
        userEmail = findViewById(R.id.textView15);
        gt = findViewById(R.id.gt);
        qr = findViewById(R.id.qr);
        tw = findViewById(R.id.tw);
        ig = findViewById(R.id.ig);
        ld = findViewById(R.id.ld);

        preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        // Check if the user is logged in
        if (isUserLoggedIn()) {
            // User is logged in, proceed with social media links
            String userID = preferences.getString("USER_ID", "default_value_if_not_found");


            // Fetch and display user details from Firebase
            fetchAndDisplayUserDetails(userID);

            Toast.makeText(this, "USER ID " + userID, Toast.LENGTH_SHORT).show();

        } else {
            // User is not logged in, show a message or redirect to login page
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            // You can also redirect to the login page if needed
            // startActivity(new Intent(About.this, LoginActivity.class));
        }
    }


    private boolean isUserLoggedIn() {
        // Check if the user is logged in by checking the login status in SharedPreferences
        return preferences.getBoolean("IS_LOGGED_IN", false);
    }

    private void fetchAndDisplayUserDetails(String userID) {
        // Get reference to Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userID);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    // Display the user details in the EditText fields
                    username.setText(user.getName());
                    userName = user.getName();
                    displaySocialMediaLinks(userName);
                    userEmail.setText(user.getEmail());
                } else {
                    Toast.makeText(About.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(About.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displaySocialMediaLinks(String userName) {
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaLink("https://www.instagram.com/" + userName);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaLink("https://www.facebook.com/profile.php?id=" + userName);
            }
        });

        gt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaLink("https://github.com/" + userName);
            }
        });

        ld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaLink("https://in.linkedin.com/in/" + userName);
            }
        });

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaLink("https://twitter.com/" + userName);
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMediaLink("https://www.quora.com/profile/" + userName);
            }
        });
    }

    private void openSocialMediaLink(String link) {
        if (link != null && !link.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Social media link not available", Toast.LENGTH_SHORT).show();
        }
    }
}
