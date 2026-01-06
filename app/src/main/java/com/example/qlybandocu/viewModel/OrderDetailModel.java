package com.example.qlybandocu.viewModel;

import com.example.qlybandocu.models.OrderDetail;

import java.util.List;

public class OrderDetailModel {

    private boolean success;
    private List<OrderDetail> result;

    public boolean isSuccess() {
        return success;
    }

    public List<OrderDetail> getResult() {
        return result;
    }
}