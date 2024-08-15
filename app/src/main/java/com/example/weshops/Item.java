package com.example.weshops;

public class Item {
    private int itemPrice;
    private int itemQuantity;
    private String imageUri; // Added field for image URL

    public Item(int i, int parseInt) {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(int itemPrice, int itemQuantity, String imageUri) {
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.imageUri = imageUri;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public String getImageUri() {
        return imageUri;
    }
}
