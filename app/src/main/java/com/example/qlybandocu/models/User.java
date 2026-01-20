package com.example.qlybandocu.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}