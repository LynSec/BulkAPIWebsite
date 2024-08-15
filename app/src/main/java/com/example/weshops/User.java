package com.example.weshops;

public class User {
    private String name;
    private String email;
    private String password;

    // Required default constructor for Firebase
    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getter methods (optional, but good practice)
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
