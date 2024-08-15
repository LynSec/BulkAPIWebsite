package com.example.weshops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {
    EditText etName, etEmail, etPassword;
    Button register;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        register = findViewById(R.id.btnregister);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });
    }

    public void reg() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        } else {
            saveUserDataToFirebase(name, email, password);

            saveUserDataLocally(name, email, password);
            Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(RegisterPage.this, Login.class);
            startActivity(profileIntent);
        }
    }

    private void saveUserDataToFirebase(String name, String email, String password) {
        // Check if any text is null or empty
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Invalid user details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get reference to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Create a safe user ID for Firebase without '.' and '@'
        String safeEmail = email.replace(".", "_dot_").replace("@", "_at_");
        String userId = safeEmail;

        // Create a User object with the provided details
        User user = new User(name, email, password);

        // Save the user data to the database
        databaseReference.child(userId).setValue(user);

        // Store the user ID in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ID", userId);
        editor.apply();

        Toast.makeText(this, "User ID = " + userId, Toast.LENGTH_SHORT).show();
    }

    private void saveUserDataLocally(String name, String email, String password) {
        // Check if any text is null or empty
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Invalid user details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use SharedPreferences to store user data locally
        SharedPreferences.Editor editor = preferences.edit();

        // Store user details using a unique key (e.g., email)
        editor.putString(email, name + "/" + email + "/" + password);
        editor.apply();
    }
}
