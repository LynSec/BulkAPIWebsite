package com.example.weshops;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaceOrder extends AppCompatActivity {
    TextView cname, cphone, ordspec, ordError, ordprice;
    EditText addr;
    String userName, userEmail,n,ph,it,pa;
    String addressWithLocation;
    Integer itp;
    Button plord;
    double latitude, longitude;
    private DatabaseReference ordersReference;
    SharedPreferences preferences;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        // Initialize ordersReference
        ordersReference = FirebaseDatabase.getInstance().getReference("Orders");
        preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String userID = preferences.getString("USER_ID", "default_value_if_not_found");
        fetchAndDisplayUserDetails(userID);

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        cname = findViewById(R.id.custname);
        cphone = findViewById(R.id.custphone);
        ordspec = findViewById(R.id.itemdet);
        ordprice = findViewById(R.id.itemprice);
        addr = findViewById(R.id.etadd);
        plord = findViewById(R.id.btnplord);
        ordError = findViewById(R.id.ordError);
        ordError.setText("");

        final String n = getIntent().getStringExtra("CUSTNAME");
        final String ph = getIntent().getStringExtra("CUSTPH");
        final String pa = getIntent().getStringExtra("CUSTPASS");
        final String it = getIntent().getStringExtra("ITEMDET");
        final int itp = getIntent().getIntExtra("item_price", 0);

        cname.setText(n);
        cphone.setText(ph);
        ordspec.setText(it);
        ordprice.setText(Integer.toString(itp));

        plord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addr.getText().toString())) {
                    ordError.setText("Please enter your address");
                } else {
                    // Request location updates
                    requestLocationUpdates();

                    Toast.makeText(PlaceOrder.this, "User ID : " + userEmail, Toast.LENGTH_SHORT).show();

                    //storeOrderInFirebase(n, ph, userEmail, it, addr.getText().toString(), pa, itp);

                    show();
                    finish();
                }
            }
        });

    }

    private void requestLocationUpdates() {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Create a LocationRequest object
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000); // 10 seconds

            // Create a LocationCallback
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        // Handle the location
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        addressWithLocation = addr.getText().toString() +
                                "\nLatitude: " + latitude +
                                "\nLongitude: " + longitude;

                        Toast.makeText(PlaceOrder.this, "Location: "+ addressWithLocation, Toast.LENGTH_SHORT).show();


                        // Store order in Firebase with the updated address
                        storeOrderInFirebase(n, ph, userEmail, it, addressWithLocation, pa, itp);

                        // Remove location updates after obtaining the first location
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                    }
                }
            };

            // Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
    private void fetchAndDisplayUserDetails(String userID) {
        // Get reference to Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userID);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    userName = user.getName();
                    userEmail = user.getEmail();
                } else {
                    Toast.makeText(PlaceOrder.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlaceOrder.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void storeOrderInFirebase(String name, String phone, String email, String item, String address, String password, int price) {
        // Generate a unique key for the order
        String orderId = ordersReference.push().getKey();
        // Create an Order object
        Order order = new Order(orderId, name, phone, email, item, address, password, price);
        // Store the order in Firebase under "Orders" node with the unique key
        ordersReference.child(orderId).setValue(order);
    }

    public void show() {
        Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates again
                requestLocationUpdates();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
