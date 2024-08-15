package com.example.weshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
// Inside MainActivity.java
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button adm_login = findViewById(R.id.btnreg);
        Button user_login= findViewById(R.id.btnlog);

        // Initialize SharedPreferences
        preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        // Check if the user is logged in and adjust button visibility
        updateButtonVisibility();
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        adm_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(loginIntent);
            }
        });

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Login.class);
                startActivity(profileIntent);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the navigation item click listener
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        openHomePage();
                        break;
                    case R.id.nav_account:
                        openProfileActivity();
                        break;
                    case R.id.nav_staff:
                        openStaffActivity();
                        break;
                    case R.id.nav_admin:
                        openAdminActivity();
                        break;
                    case R.id.nav_books:
                        openBooksActivity();
                        break;
                    case R.id.nav_logout:
                        clearLoginDetails();
                        break;

                }

                // Close the drawer after handling item click
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    private void clearLoginDetails() {
        // Clear login details in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("USER_ID");
        editor.remove("IS_LOGGED_IN");
        editor.remove("IS_ADMIN_LOGGED_IN");
        editor.apply();

        updateButtonVisibility();
    }
    private void updateButtonVisibility() {
        // Get the user login status from SharedPreferences
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

        if (isLoggedIn) {
            // User is logged in, hide the register and logout buttons
            findViewById(R.id.btnreg).setVisibility(View.GONE);
            findViewById(R.id.btnlog).setVisibility(View.GONE);
        } else {
            // User is not logged in, show the register and logout buttons
            findViewById(R.id.btnreg).setVisibility(View.VISIBLE);
            findViewById(R.id.btnlog).setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check the user's login status when the activity starts
        updateButtonVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void openStaffActivity() {
        Intent profileIntent = new Intent(MainActivity.this, StaffLogin.class);
        startActivity(profileIntent);
    }
    private void openBooksActivity() {
        Intent profileIntent = new Intent(MainActivity.this, Books.class);
        startActivity(profileIntent);
    }
    private void openShopActivity() {
        Intent profileIntent = new Intent(MainActivity.this, Home.class);
        startActivity(profileIntent);
    }
    private void openHomePage() {
        Intent profileIntent = new Intent(MainActivity.this, Home.class);
        startActivity(profileIntent);
    }
    private void openProfileActivity() {
        Intent profileIntent = new Intent(MainActivity.this, About.class);
        startActivity(profileIntent);
    }
    private void openAdminActivity() {
        Intent profileIntent = new Intent(MainActivity.this, AdminLogin.class);
        startActivity(profileIntent);
    }
}
