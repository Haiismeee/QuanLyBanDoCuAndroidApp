package com.example.qlybandocu.models;

import java.util.List;

public class MyPostModel {
    private boolean success;
    private List<Products> result;

    public boolean isSuccess() {
        return success;
    }

    public List<Products> getResult() {
        return result;
    }
}
