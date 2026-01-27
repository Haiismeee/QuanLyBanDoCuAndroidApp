package com.example.qlybandocu.models;

public class User {

    // ===== FIELDS (BẮT BUỘC PHẢI CÓ) =====
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    // ===== GETTERS =====
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    // ===== SETTERS (DÙNG CHO UPDATE PROFILE) =====
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
