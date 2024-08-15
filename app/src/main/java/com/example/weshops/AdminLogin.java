package com.example.weshops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {
    EditText admuser,admpass;
    Button admlog;
    TextView admstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        // Use getSharedPreferences within onCreate
        preferences = getSharedPreferences("ADMIN_DATA", MODE_PRIVATE);
        editor = preferences.edit();

        if (isAdminLoggedIn()){

        }
        admuser= findViewById(R.id.admuser);
        admpass= findViewById(R.id.admpass);
        admlog= findViewById(R.id.admlogin);
        admstatus= findViewById(R.id.admstatus);
        admstatus.setText("");
        admlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((TextUtils.isEmpty(admuser.getText().toString()))||(TextUtils.isEmpty(admpass.getText().toString())))){
                    if (admuser.getText().toString().equals("Sarabjit")&&admpass.getText().toString().equals("Mannat2023@")){
                        Intent intent=new Intent(AdminLogin.this,AdminHomePage.class);
                        intent.putExtra("CALLINGACTIVITY","AdminLogin");
                        editor.putBoolean("IS_ADMIN_LOGGED_IN", true); // Set admin login status
                        editor.apply();
                        startActivity(intent);
                    }else{
                        admstatus.setText("Invalid credentials");
                    }
            }else{
                    admstatus.setText("Please enter all credentials");
                }
        }
    });

}

    private boolean isAdminLoggedIn() {
        // Check if the user is logged in by checking the login status in SharedPreferences
        return preferences.getBoolean("IS_ADMIN_LOGGED_IN", false);
    }

public void onBackPressed(){

        startActivity(new Intent(AdminLogin.this,Home.class));
}
}
