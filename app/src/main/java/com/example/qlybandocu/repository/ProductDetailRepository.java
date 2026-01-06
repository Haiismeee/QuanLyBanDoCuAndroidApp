package com.example.qlybandocu.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.qlybandocu.viewModel.ProductDetailModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailRepository {
    private BanDoCuApi doCuApi;

    public ProductDetailRepository() {
        doCuApi = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);
    }
    public MutableLiveData<ProductDetailModel> getProductDetail(int id){
        MutableLiveData<ProductDetailModel> data = new MutableLiveData<>();
        doCuApi.getProductsDetail(id).enqueue(new Callback<ProductDetailModel>() {
            @Override
            public void onResponse(Call<ProductDetailModel> call, Response<ProductDetailModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductDetailModel> call, Throwable t) {
                data.setValue(null);
                Log.d("logg", t.getMessage());
            }
        });
        return data;
    }
}
