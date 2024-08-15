package com.example.weshops;
// Inside your Order class

public class Order {
    public String orderId;
    public String userName;
    public String userPhone;
    public String userEmail; // Add user email
    public String item;
    public String address;
    public String password;
    public int price;

    // Default constructor required for Firebase
    public Order() {
    }

    public Order(String orderId, String userName, String userPhone, String userEmail, String item, String address, String password, int price) {
        this.orderId = orderId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.item = item;
        this.address = address;
        this.password = password;
        this.price = price;
    }
}
