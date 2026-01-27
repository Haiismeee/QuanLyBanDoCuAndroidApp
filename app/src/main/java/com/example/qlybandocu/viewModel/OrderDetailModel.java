package com.example.qlybandocu.viewModel;

import com.example.qlybandocu.models.OrderDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailModel {

    @SerializedName("success")
    private boolean success;

    @SerializedName("result")
    private List<OrderDetail> result;

    public boolean isSuccess() {
        return success;
    }

    public List<OrderDetail> getResult() {
        return result;
    }
}
