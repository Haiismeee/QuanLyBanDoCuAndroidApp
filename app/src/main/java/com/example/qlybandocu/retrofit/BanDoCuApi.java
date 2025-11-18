package com.example.qlybandocu.retrofit;

import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BanDoCuApi {

    // Lấy danh mục
    @GET("category.php")
    Call<CategoryModel> getCategory();

    // Đăng ký người dùng
    @FormUrlEncoded
    @POST("register.php")
    Call<UserModel> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );

    // Đăng nhập người dùng
    @FormUrlEncoded
    @POST("login.php")
    Call<UserModel> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );
}
