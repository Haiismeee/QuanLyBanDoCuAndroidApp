package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.adapters.OrderDetailAdapter;
import com.example.qlybandocu.viewModel.OrderDetailModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;
import com.example.qlybandocu.viewModel.OrderDetailModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    RecyclerView rcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        int idorder = getIntent().getIntExtra("idorder", 0);

        rcv = findViewById(R.id.rcvOrderDetail);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        BanDoCuApi api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        api.getOrderDetail(idorder)
                .enqueue(new Callback<OrderDetailModel>() {
                    @Override
                    public void onResponse(Call<OrderDetailModel> call,
                                           Response<OrderDetailModel> response) {

                        if (response.body() != null && response.body().isSuccess()) {
                            rcv.setAdapter(
                                    new OrderDetailAdapter(response.body().getResult())
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                        Toast.makeText(OrderDetailActivity.this,
                                t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
