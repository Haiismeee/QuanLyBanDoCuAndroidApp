package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.adapters.DeliveryOrderAdapter;
import com.example.qlybandocu.models.OrderModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BanDoCuApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        recyclerView = findViewById(R.id.rcvDeliveryOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        loadOrders();
    }

    private void loadOrders() {
        api.getAllOrders().enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    recyclerView.setAdapter(
                            new DeliveryOrderAdapter(response.body().getResult(), api)
                    );
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(DeliveryActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
