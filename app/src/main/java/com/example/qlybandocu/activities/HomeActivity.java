package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.adapters.CategoryAdapter;
import com.example.qlybandocu.adapters.PopularAdapter;
import com.example.qlybandocu.databinding.ActivityHomeBinding;
import com.example.qlybandocu.listener.CategoryListener;
import com.example.qlybandocu.listener.EventClickListener;
import com.example.qlybandocu.models.Category;
import com.example.qlybandocu.models.Products;
import com.example.qlybandocu.viewModel.HomeViewModel;

public class HomeActivity extends AppCompatActivity implements CategoryListener, EventClickListener {
    HomeViewModel homeViewModel;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initView();
        initData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initView() {
        binding.rcCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rcCategory.setLayoutManager(layoutManager);

        binding.rcPopular.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,3);
        binding.rcPopular.setLayoutManager(layoutManager1);

        // 1. Nút Giỏ hàng (ở giữa thanh menu dưới)
        binding.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        // 2. Mở trang quản lý tài khoản khi click avatar
        binding.imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        // --- BỔ SUNG: Nút Đăng Tin Mới (Nút màu cam ở góc) ---
        // Lưu ý: Đảm bảo bạn đã thêm id="@+id/btnThemSp" vào activity_home.xml như hướng dẫn trước
        if (binding.btnThemSp != null) { // Kiểm tra null để tránh crash nếu lỡ chưa sửa XML
            binding.btnThemSp.setOnClickListener(view -> {
                Intent intent = new Intent(HomeActivity.this, DangTinActivity.class);
                startActivity(intent);
            });
        }
        // -----------------------------------------------------
    }

    private void initData() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.categoryModelMutableLiveData().observe(this, categoryModel -> {

            if (categoryModel == null) {
                Log.e("API_ERROR", "Response null từ server");
                return;
            }

            if (categoryModel.isSuccess()) {
                CategoryAdapter adapter = new CategoryAdapter(categoryModel.getResult(), this);
                binding.rcCategory.setAdapter(adapter);
            } else {
                Log.e("API_ERROR", "API success=false: " + categoryModel.getMessage());
            }
        });
        homeViewModel.productModelMutableLiveData(1).observe(this,productModel -> {
            if(productModel.isSuccess()){
                PopularAdapter adapter = new PopularAdapter(productModel.getResult(), this);
                binding.rcPopular.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        intent.putExtra("idcate", category.getId());
        intent.putExtra("namecate", category.getCategory());
        startActivity(intent);
    }

    @Override
    public void onPopularClick(Products products) {
        Intent intent = new Intent(getApplicationContext(), ShowDetailActivity.class);
        intent.putExtra("id", products.getIdProduct());
        startActivity(intent);
    }
}