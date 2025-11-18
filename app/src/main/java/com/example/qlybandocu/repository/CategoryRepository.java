package com.example.qlybandocu.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private BanDoCuApi banDoCuApi;

    public CategoryRepository() {
        banDoCuApi = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);
    }

    public MutableLiveData<CategoryModel> getCategory() {
        MutableLiveData<CategoryModel> data = new MutableLiveData<>();

        banDoCuApi.getCategory().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    CategoryModel error = new CategoryModel();
                    error.setSuccess(false);
                    error.setMessage("API trả về rỗng hoặc lỗi parse JSON");
                    error.setResult(new ArrayList<>());
                    data.setValue(error);
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());

                CategoryModel error = new CategoryModel();
                error.setSuccess(false);
                error.setMessage("Lỗi kết nối: " + t.getMessage());
                error.setResult(new ArrayList<>());
                data.setValue(error);
            }
        });

        return data;
    }
}
