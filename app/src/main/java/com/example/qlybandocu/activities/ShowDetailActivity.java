package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.qlybandocu.R;
import com.example.qlybandocu.databinding.ActivityShowDetailBinding;
import com.example.qlybandocu.models.ProductDetail;
import com.example.qlybandocu.viewModel.ShowDetailViewModel;
import java.text.DecimalFormat;

public class ShowDetailActivity extends AppCompatActivity {
    ShowDetailViewModel viewModel;
    ActivityShowDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_show_detail);
        getData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getData() {
        int id = getIntent().getIntExtra("id", 0);
        viewModel = new ViewModelProvider(this).get(ShowDetailViewModel.class);
        viewModel.productDetailModelMutableLiveData(id).observe(this, productDetailModel -> {
            if(productDetailModel.isSuccess()){
                ProductDetail productDetail = productDetailModel.getResult().get(0);
                // --- XỬ LÝ HIỂN THỊ ---
                binding.txtnameproduct.setText(productDetail.getProductname());

                // CÁCH FORMAT GIÁ TIỀN VIỆT NAM:
                // Mẫu "###,###,###" sẽ tự động thêm dấu phân cách hàng nghìn
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                binding.txtprice.setText("Giá: " + decimalFormat.format(productDetail.getPrice()) + "₫");

                binding.txtdesc.setText(productDetail.getDescription());
                Glide.with(this).load(productDetail.getStrproductthumb()).into(binding.image);
            }
        });
    }
}