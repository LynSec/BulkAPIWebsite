package com.example.weshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AcceptOrders extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tvtopmsg;
    SharedPreferences sharedPreferences;
    DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_orders);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tvtopmsg = findViewById(R.id.tvtopmsg);

        sharedPreferences = getSharedPreferences("LocalData", MODE_PRIVATE);

        // Initialize the DatabaseReference
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Retrieve and display orders
        retrieveAndDisplayOrders();

//        PlaceOrder.getOrder();

        tv1.setText(sharedPreferences.getString("order1", ""));
        tv2.setText(sharedPreferences.getString("order2", ""));
        tv3.setText(sharedPreferences.getString("order3", ""));
        tv4.setText(sharedPreferences.getString("order4", ""));



        tvtopmsg.setText("Tap on an order to start its delivery");

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv1.getText().toString().equals("")) {
                    String details = tv1.getText().toString();
                    addOrderToDeliver(details);
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv2.getText().toString().equals("")) {
                    String details = tv2.getText().toString();
                    addOrderToDeliver(details);
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv3.getText().toString().equals("")) {
                    String details = tv3.getText().toString();
                    addOrderToDeliver(details);
                }
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv4.getText().toString().equals("")) {
                    String details = tv4.getText().toString();
                    addOrderToDeliver(details);
                }
            }
        });
    }

    private void retrieveAndDisplayOrders() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Limit to 4 orders for demonstration purposes
                    if (count > 4) {
                        break;
                    }

                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        displayOrder(order, count);
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(AcceptOrders.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrder(Order order, int count) {
        switch (count) {
            case 1:
                tv1.setText("Order ID: " + order.orderId + "\nItem: " + order.item + "\nPrice: $" + order.price);
                break;
            case 2:
                tv2.setText("Order ID: " + order.orderId + "\nItem: " + order.item + "\nPrice: $" + order.price);
                break;
            case 3:
                tv3.setText("Order ID: " + order.orderId + "\nItem: " + order.item + "\nPrice: $" + order.price);
                break;
            case 4:
                tv4.setText("Order ID: " + order.orderId + "\nItem: " + order.item + "\nPrice: $" + order.price);
                break;
        }
    }

    public void addOrderToDeliver(String details) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deliverOrder", details);
        editor.apply();
        Toast.makeText(this, "Order added to your to deliver list", Toast.LENGTH_SHORT).show();

    }

    public void onBackPressed() {
        startActivity(new Intent(AcceptOrders.this, StaffLogin.class));
    }
}
