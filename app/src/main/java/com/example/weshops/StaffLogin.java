package com.example.weshops;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class StaffLogin extends AppCompatActivity {
    EditText stfname, stfpass;
    TextView stfstatus;
    Button stflogin;
    String sna, spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        stfname = findViewById(R.id.stfname);
        stfpass = findViewById(R.id.stfpass);
        stfstatus = findViewById(R.id.stfstatus);
        stflogin = findViewById(R.id.stflogin);
        stfstatus.setText("");

        stflogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sna = stfname.getText().toString();
                spa = stfpass.getText().toString();

                if (TextUtils.isEmpty(sna))
                    stfstatus.setText("Please enter your name");
                else if (TextUtils.isEmpty(spa))
                    stfstatus.setText("Please enter your password");
                else {
                    // Check staff credentials in Firebase
                    checkStaffCredentials(sna, spa);
                }
            }
        });
    }

    private void checkStaffCredentials(String staffName, String staffPassword) {
        DatabaseReference staffRef = FirebaseDatabase.getInstance().getReference("staff");

        staffRef.orderByChild("name").equalTo(staffName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot staffSnapshot : dataSnapshot.getChildren()) {
                        String storedPassword = staffSnapshot.child("password").getValue(String.class);
                        if (staffPassword.equals(storedPassword)) {
                            // Login successful
                            Intent intent = new Intent(StaffLogin.this, AcceptOrders.class);
                            intent.putExtra("STAFFNAME", staffName);
                            intent.putExtra("STAFFPASSWORD", storedPassword);
                            startActivity(intent);
                        } else {
                            // Invalid credentials
                            stfstatus.setText("Invalid Credentials");
                        }
                    }
                } else {
                    // Staff not found
                    stfstatus.setText("Staff not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(StaffLogin.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(StaffLogin.this, MainActivity.class));
    }
}
