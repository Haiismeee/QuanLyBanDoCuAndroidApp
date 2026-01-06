package com.example.qlybandocu.models;

import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("idproduct")
    private int idproduct;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private double price;

    public int getIdproduct() {
        return idproduct;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
