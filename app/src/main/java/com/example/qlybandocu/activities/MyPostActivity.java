package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.adapters.MyPostAdapter;
import com.example.qlybandocu.models.MyPostModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostActivity extends AppCompatActivity {

    // ✅ KHAI BÁO Ở ĐÂY
    RecyclerView rcv;
    BanDoCuApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        // ✅ ÁNH XẠ ĐÚNG ID
        rcv = findViewById(R.id.rcvMyPosts);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(new MyPostAdapter(this, new ArrayList<>()));


        api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        loadMyPosts();
    }

    private void loadMyPosts() {

        if (Utils.user_current == null) {
            Toast.makeText(this,
                    "Vui lòng đăng nhập lại",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int iduser = Utils.user_current.getId();

        // ✅ CHO PHÉP ID = 0 (USER CHƯA MAP MYSQL)
        api.getMyPosts(iduser).enqueue(new Callback<MyPostModel>() {
            @Override
            public void onResponse(Call<MyPostModel> call,
                                   Response<MyPostModel> res) {

                if (res.body() != null
                        && res.body().isSuccess()
                        && res.body().getResult() != null
                        && res.body().getResult().size() > 0) {

                    rcv.setAdapter(new MyPostAdapter(
                            MyPostActivity.this,
                            res.body().getResult()
                    ));
                } else {
                    Toast.makeText(MyPostActivity.this,
                            "Bạn chưa đăng tin nào",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyPostModel> call, Throwable t) {
                Toast.makeText(MyPostActivity.this,
                        "Lỗi kết nối server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



}
