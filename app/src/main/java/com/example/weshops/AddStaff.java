package com.example.weshops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStaff extends AppCompatActivity {
    EditText stfName, stfPass, stfRePass;
    TextView backMain;
    Button regStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        stfName = findViewById(R.id.reg_staff_name);
        stfPass = findViewById(R.id.reg_staff_pass);
        backMain = findViewById(R.id.back_main);
        stfRePass = findViewById(R.id.reg_staff_repass);
        regStaff = findViewById(R.id.btn_reg_staff);

        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStaff.this, AdminHomePage.class);
                intent.putExtra("CALLINGACTIVITY", "back");
                startActivity(intent);
            }
        });

        regStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStaff();
            }
        });
    }

    public void registerStaff() {
        String name = stfName.getText().toString();
        String pass = stfPass.getText().toString();
        String repass = stfRePass.getText().toString();

        if (TextUtils.isEmpty(name))
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(pass))
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        else if (!pass.equals(repass))
            Toast.makeText(this, "Confirmed password does not match the given password", Toast.LENGTH_SHORT).show();
        else {
            saveStaffToFirebase(name, pass);
            Toast.makeText(this, "Staff Registered", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveStaffToFirebase(String name, String password) {
        // Get reference to Firebase Realtime Database
        DatabaseReference staffRef = FirebaseDatabase.getInstance().getReference("staff");

        // Create a unique key for the staff member
        String staffId = staffRef.push().getKey();

        // Create a StaffReg object with the staff information
        StaffReg staff = new StaffReg(name, password, staffId);

        // Save the staff information to Firebase
        staffRef.child(staffId).setValue(staff);
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddStaff.this, AdminHomePage.class);
        intent.putExtra("CALLINGACTIVITY", "back");
        startActivity(intent);
    }
}
