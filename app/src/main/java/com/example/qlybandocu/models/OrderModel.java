package com.example.qlybandocu.models;

import java.util.List;

public class OrderModel {
    private boolean success;
    private List<Order> result;

    public boolean isSuccess() {
        return success;
    }

    public List<Order> getResult() {
        return result;
    }
}
