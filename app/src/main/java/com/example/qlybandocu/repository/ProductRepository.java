package com.example.qlybandocu.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.qlybandocu.models.ProductModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private BanDoCuApi api;

    public ProductRepository() {
        api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);
    }
    public MutableLiveData<ProductModel> getProducts(int idcate){
        MutableLiveData<ProductModel> data = new MutableLiveData<>();
        api.getProducts(idcate).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.d("logg", t.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }
}
