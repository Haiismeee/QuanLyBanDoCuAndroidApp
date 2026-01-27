package com.example.qlybandocu.repository;

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

    // ===== GET PRODUCT BY CATEGORY (CŨ – GIỮ NGUYÊN) =====
    public MutableLiveData<ProductModel> getProducts(int idcate) {
        MutableLiveData<ProductModel> data = new MutableLiveData<>();

        api.getProducts(idcate).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    // ===== SEARCH PRODUCT (MỚI – SỬA LỖI) =====
    public MutableLiveData<ProductModel> searchProduct(String keyword) {
        MutableLiveData<ProductModel> data = new MutableLiveData<>();

        api.searchProduct(keyword).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
