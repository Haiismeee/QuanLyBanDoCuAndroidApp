package com.example.qlybandocu.models;

import java.util.List;

public class ProductModel {
    private boolean success;
    private String mesage;
    private List<Products> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public List<Products> getResult() {
        return result;
    }

    public void setResult(List<Products> result) {
        this.result = result;
    }
}
