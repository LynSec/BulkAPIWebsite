package com.example.weshops;

public class StaffReg {
    private final String name;
    private final String password;
    private final String id;

    public StaffReg(String name, String password, String id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }
}
