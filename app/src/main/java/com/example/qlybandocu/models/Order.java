package com.example.qlybandocu.models;

public class Order {
    private int id;
    private int total_price;
    private String payment_method;
    private String payment_status;
    private String created_at;

    public int getId() { return id; }
    public int getTotal_price() { return total_price; }
    public String getPayment_method() { return payment_method; }
    public String getPayment_status() { return payment_status; }
    public String getCreated_at() { return created_at; }
}
