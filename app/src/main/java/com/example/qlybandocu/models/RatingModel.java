package com.example.qlybandocu.models;

import com.google.gson.annotations.SerializedName;

public class RatingModel {

    private boolean success;

    @SerializedName("avg_rating")
    private float avg_rating;

    private int total;

    public boolean isSuccess() {
        return success;
    }

    public float getAvg_rating() {
        return avg_rating;
    }

    public int getTotal() {
        return total;
    }
}
