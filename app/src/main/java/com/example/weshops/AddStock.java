package com.example.weshops;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddStock extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    EditText etItemId, etItemPrice, etQuantity;
    Button btnUpdate;
    ImageView imageView;
    Uri imageUri;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        etItemId = findViewById(R.id.etItemId);
        etItemPrice = findViewById(R.id.etItemPrice);
        etQuantity = findViewById(R.id.etQuantity);
        btnUpdate = findViewById(R.id.btnUpdateStock);
        imageView = findViewById(R.id.imageView);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Set an OnClickListener to open the image picker
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = etItemId.getText().toString().trim();
                String itemPrice = etItemPrice.getText().toString().trim();
                String quantity = etQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(itemId) || TextUtils.isEmpty(itemPrice) || TextUtils.isEmpty(quantity)) {
                    Toast.makeText(AddStock.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Update stock using Firebase Database
                    updateStockInFirebase(itemId, itemPrice, quantity);

                    showSuccess();
                    Intent intent = new Intent(AddStock.this, AdminHomePage.class);
                    intent.putExtra("CALLINGACTIVITY", "AddStock");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void updateStockInFirebase(String itemId, String itemPrice, String quantity) {
        // Create an Item object with the entered data
        Item newItem = new Item(Integer.parseInt(itemPrice), Integer.parseInt(quantity));

        // Update stock in Firebase Database
        databaseReference.child(itemId).setValue(newItem);

        // Upload the image to Firebase Storage
        if (imageUri != null) {
            uploadImage(itemId);
        }
    }

    private void uploadImage(String itemId) {
        // Get the reference to the Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("item_images");

        // Create a unique file name for the image using item ID
        String imageName = itemId + "_image.jpg";

        // Create a reference to the image file
        StorageReference imageReference = storageReference.child(imageName);

        // Upload the image to Firebase Storage
        imageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful, get the download URL
                    imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the item in the Firebase Realtime Database with the download URL
                        databaseReference.child(itemId).child("imageUri").setValue(uri.toString());

                        // Notify the user about successful image upload
                        Toast.makeText(AddStock.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        // Handle failures in getting download URL
                        Toast.makeText(AddStock.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failures in image upload
                    Toast.makeText(AddStock.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    public void itemIdNotEntered() {
        Toast.makeText(this, "Please Enter Item Id", Toast.LENGTH_SHORT).show();
    }

    public void quantityNotEntered() {
        Toast.makeText(this, "Please Enter Quantity to add", Toast.LENGTH_SHORT).show();
    }

    public void showSuccess() {
        Toast.makeText(this, "Stock Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddStock.this, AdminHomePage.class);
        intent.putExtra("CALLINGACTIVITY", "back");

        startActivity(intent);
        finish();
    }
}
