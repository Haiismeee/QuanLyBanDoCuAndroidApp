package com.example.qlybandocu.models;

public class Products {
    private int id;
    private String strProduct, strProductThumb;
    private int idProduct, idcategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrProduct() {
        return strProduct;
    }

    public void setStrProduct(String strProduct) {
        this.strProduct = strProduct;
    }

    public String getStrProductThumb() {
        return strProductThumb;
    }

    public void setStrProductThumb(String strProductThumb) {
        this.strProductThumb = strProductThumb;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(int idcategory) {
        this.idcategory = idcategory;
    }
}
