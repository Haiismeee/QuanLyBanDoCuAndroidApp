package com.example.qlybandocu.models;

import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("idproduct")
    private int idproduct;

    @SerializedName("productname")
    private String productName;

    @SerializedName("product_image")
    private String productImage;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private double price;

    public int getIdproduct() {
        return idproduct;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
