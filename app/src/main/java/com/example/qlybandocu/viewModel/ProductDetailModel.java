package com.example.qlybandocu.viewModel;

import com.example.qlybandocu.models.ProductDetail;

import java.util.List;

public class ProductDetailModel {
    private boolean success;
    private String mesage;
    private List<ProductDetail> result;

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

    public List<ProductDetail> getResult() {
        return result;
    }

    public void setResult(List<ProductDetail> result) {
        this.result = result;
    }
}
