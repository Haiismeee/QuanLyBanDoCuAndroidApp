package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.adapters.OrderAdapter;
import com.example.qlybandocu.models.OrderModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BanDoCuApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.rcvOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        loadOrders();
    }

    private void loadOrders() {

        if (Utils.user_current == null) {
            Toast.makeText(this,
                    "Vui lòng đăng nhập",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int iduser = Utils.user_current.getId();

        api.getOrders(iduser).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call,
                                   Response<OrderModel> response) {

                if (response.body() != null && response.body().isSuccess()) {
                    recyclerView.setAdapter(
                            new OrderAdapter(response.body().getResult())
                    );
                } else {
                    Toast.makeText(OrderHistoryActivity.this,
                            "Chưa có đơn hàng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}