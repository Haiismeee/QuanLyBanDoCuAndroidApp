package com.example.qlybandocu.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;

    // Giữ nguyên đường dẫn theo yêu cầu của bạn
    private static final String BASE_URL = "http://10.0.2.2/qldocu/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // 1. Tạo Gson với chế độ "dễ tính" (Lenient) để sửa lỗi JSON
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // 2. Truyền gson vào đây
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}