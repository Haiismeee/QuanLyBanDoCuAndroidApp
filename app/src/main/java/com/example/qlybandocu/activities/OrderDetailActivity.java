package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.adapters.OrderDetailAdapter;
import com.example.qlybandocu.models.OrderDetail;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;
import com.example.qlybandocu.viewModel.OrderDetailModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    RecyclerView rcv;
    Button btnReview;
    TextView tvOrderId, tvTotalPrice;
    TextView tvBuyerName, tvBuyerPhone, tvBuyerAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        int idorder = getIntent().getIntExtra("idorder", 0);
        int status  = getIntent().getIntExtra("status", 0);

        rcv = findViewById(R.id.rcvOrderDetail);
        btnReview = findViewById(R.id.btnReview);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvBuyerName = findViewById(R.id.tvBuyerName);
        tvBuyerPhone = findViewById(R.id.tvBuyerPhone);
        tvBuyerAddress = findViewById(R.id.tvBuyerAddress);


        rcv.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailAdapter adapter = new OrderDetailAdapter(new ArrayList<>());
        rcv.setAdapter(adapter);

        tvOrderId.setText("Mã đơn: #" + idorder);

        // ===== HIỂN THỊ NÚT ĐÁNH GIÁ =====
        btnReview.setVisibility(status == 3 ? View.VISIBLE : View.GONE);

        btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("idorder", idorder);
            startActivity(intent);
        });

        BanDoCuApi api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        api.getOrderDetail(idorder).enqueue(new Callback<OrderDetailModel>() {
            @Override
            public void onResponse(Call<OrderDetailModel> call,
                                   Response<OrderDetailModel> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()) {

                    List<OrderDetail> list = response.body().getResult();

                    if (list != null && !list.isEmpty()) {

                        // ===== SET ADAPTER =====
                        OrderDetailAdapter adapter =
                                new OrderDetailAdapter(list);
                        rcv.setAdapter(adapter);

                        // ===== TỔNG TIỀN =====
                        DecimalFormat df = new DecimalFormat("###,###,###");
                        tvTotalPrice.setText(df.format(adapter.getTotalPrice()) + " đ");

                        // ===== THÔNG TIN NGƯỜI MUA =====
                        OrderDetail first = list.get(0);

                        tvBuyerName.setText("Tên: " + first.getBuyerName());
                        tvBuyerPhone.setText("SĐT: " + first.getBuyerPhone());
                        tvBuyerAddress.setText("Địa chỉ: " + first.getBuyerAddress());

                    }
                    else {
                        tvTotalPrice.setText("0 đ");
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this,
                            "Không có dữ liệu đơn hàng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this,
                        "Lỗi API: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

}
