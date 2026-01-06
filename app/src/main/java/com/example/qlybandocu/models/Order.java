package com.example.qlybandocu.models;

import com.google.gson.annotations.SerializedName;

public class Order {

    private int id;

    @SerializedName("total")
    private double total_price;

    @SerializedName("payment_method")
    private String payment_method;

    @SerializedName("status")
    private int status; // ⭐ TRẠNG THÁI ĐƠN

    @SerializedName("dateorder")
    private String created_at;

    public int getId() {
        return id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public int getPayment_status() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }
}
