package com.example.weshops;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DisplayItem extends AppCompatActivity {
    ImageView imageView;
    TextView imageDetails, imagePrice, stockQuantity;
    Button orderShownItem;
    DatabaseReference stocksReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        imageView = findViewById(R.id.item_display);
        imageDetails = findViewById(R.id.item_details_display);
        imagePrice = findViewById(R.id.item_price_display);
        orderShownItem = findViewById(R.id.order_shown_item);
        stockQuantity = findViewById(R.id.stock_quantity);


        imageView.setImageResource(getIntent().getIntExtra("image_id", 0));
        imageDetails.setText(getIntent().getStringExtra("item_details"));
        imagePrice.setText(Integer.toString(getIntent().getIntExtra("item_price", 0)));
        stockQuantity.setText(Integer.toString(getIntent().getIntExtra("itemQuantity", 0)));

        final String item_details = getIntent().getStringExtra("item_details");
        final int item_price = getIntent().getIntExtra("item_price", 0);

        stocksReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference = FirebaseDatabase.getInstance().getReference("items"); // Add this line


        fetchStockQuantity(item_details);

        orderShownItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayItem.this, PlaceOrder.class);
                intent.putExtra("CUSTNAME", getIntent().getStringExtra("NAME"));
                intent.putExtra("CUSTPH", getIntent().getStringExtra("PHONE"));
                intent.putExtra("CUSTPASS", getIntent().getStringExtra("PASSWORD"));
                intent.putExtra("ITEMDET", imageDetails.getText());
                intent.putExtra("CALLING_ACTIVITY", getIntent().getStringExtra("CALLING_ACTIVITY"));
                intent.putExtra("image_id", getIntent().getIntExtra("image_id", 0));
                intent.putExtra("item_details", item_details);
                intent.putExtra("item_price", item_price);
                startActivity(intent);
            }
        });
    }

    private void fetchStockQuantity(final String itemDetails) {
        databaseReference.child(itemDetails).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the item exists in the database
                if (dataSnapshot.exists()) {
                    // Get item details as a HashMap
                    HashMap<String, Object> itemData = (HashMap<String, Object>) dataSnapshot.getValue();

                    // Check if the "itemQuantity" field exists in the HashMap
                    if (itemData != null && itemData.containsKey("itemQuantity")) {
                        // Get item quantity and update UI
                        Object itemQuantityValue = itemData.get("itemQuantity");

                        // Check if the value is a number
                        if (itemQuantityValue instanceof Number) {
                            long itemQuantityLong = ((Number) itemQuantityValue).longValue();
                            stockQuantity.setText("Stock: " + itemQuantityLong);

                            // Disable order button if stock is 0
                            if (itemQuantityLong == 0) {
                                orderShownItem.setEnabled(false);
                                orderShownItem.setText("CURRENTLY OUT OF STOCK");
                            }
                        } else {
                            // Handle the case where the value is not a number
                            Toast.makeText(DisplayItem.this, "Invalid item quantity format", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where "itemQuantity" field is missing
                        Toast.makeText(DisplayItem.this, "Missing item quantity field", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Item not found in the database
                    Toast.makeText(DisplayItem.this, "Item found in the database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayItem.this, "Failed to fetch item quantity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(DisplayItem.this, getClassFromCallingActivity());
        intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
        intent.putExtra("PHONE", getIntent().getStringExtra("PHONE"));
        intent.putExtra("PASSWORD", getIntent().getStringExtra("PASSWORD"));
        startActivity(intent);
    }

    private Class<?> getClassFromCallingActivity() {
        String ca = getIntent().getStringExtra("CALLING_ACTIVITY");
        switch (ca) {
            case "Books":
                return Books.class;
            // Add cases for other calling activities if needed
            default:
                return MainActivity.class; // Replace with your default activity
        }
    }
}
