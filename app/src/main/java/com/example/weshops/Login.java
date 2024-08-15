package com.example.weshops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText password;
    private EditText email;
    private TextView reset;
    private Button loginBtn;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.btnCreatesignin);
        reset = findViewById(R.id.reset);

        // Use getSharedPreferences with the correct context
        preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        editor = preferences.edit();

        if (isUserLoggedIn()) {
            gotoWelcome();
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private boolean isUserLoggedIn() {
        // Check if the user is logged in by checking the login status in SharedPreferences
        return preferences.getBoolean("IS_LOGGED_IN", false);
    }
    private void validate() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (!TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(passwordText)) {
            // Check if the entered email exists in the database
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            usersRef.orderByChild("email").equalTo(emailText).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Email exists, now check the password
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String storedPassword = userSnapshot.child("password").getValue(String.class);
                            if (passwordText.equals(storedPassword)) {
                                // Password matches, user is authenticated

                                // Get the user ID
                                String userId = userSnapshot.getKey();

                                // Store the user ID in SharedPreferences
                                editor.putString("USER_ID", userId);
                                editor.putBoolean("IS_LOGGED_IN", true); // Set login status
                                editor.apply();

                                Toast.makeText(Login.this, "USER_ID: " + userId, Toast.LENGTH_SHORT).show();

                                gotoWelcome();
                            } else {
                                // Password does not match
                                Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Email not found in the database
                        Toast.makeText(Login.this, "Email not registered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(Login.this, "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoRegister() {
        startActivity(new Intent(Login.this, Registration.class));
    }

    private void gotoWelcome() {
        startActivity(new Intent(Login.this, Home.class));
    }
}
