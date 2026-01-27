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
    @SerializedName("buyer_name")
    private String buyerName;

    @SerializedName("buyer_phone")
    private String buyerPhone;

    @SerializedName("buyer_address")
    private String buyerAddress;
    @SerializedName("seller_name")
    private String sellerName;

    @SerializedName("seller_phone")
    private String sellerPhone;

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }


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
