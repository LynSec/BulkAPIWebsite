package com.example.weshops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Registration extends AppCompatActivity {

    private EditText user_name,n;
    private EditText email,conf_password;
    private EditText password;
    private Button btnRegister;
    //private TextInputEditText user_name, etPassword, etCPassword;
//    private MaterialButton btnRegister;
//    private AppCompatTextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        user_name = findViewById(R.id.username);
        n = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.confirm_password);
        btnRegister = findViewById(R.id.btnCreateAccount);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        String name = n.getText().toString();
        String email_in = email.getText().toString();
        String password_in = password.getText().toString();
        String re_password = conf_password.getText().toString();
        String username = user_name.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email_in) && !TextUtils.isEmpty(password_in)){
            if (email_in.contains("@")){
                if (!username.contains(" ")){
                    if (password_in.equals(re_password)){
                        startRegistration(name, email_in, password_in, username);
                    }else {
                        Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this, getString(R.string.no_space), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, getString(R.string.email_not_valid), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, getString(R.string.check_fields), Toast.LENGTH_LONG).show();
        }
    }
        private void startRegistration(String name, String email_in, String password_in, String username) {
            SharedPreferences sharedPreferences = this.getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("NAME",name);
            editor.putString("EMAIL",email_in);
            editor.putString("USERNAME",username);
            editor.putString("PASSWORD",password_in);
            editor.putBoolean("ISLOGGEDIN",true);
            editor.apply();
            Toast.makeText(this, "Registration Successfull", Toast.LENGTH_LONG).show();
            gotoLogin();
        }
    private void gotoLogin() {
        startActivity(new Intent(Registration.this, Login.class));
        finish();
    }
}
