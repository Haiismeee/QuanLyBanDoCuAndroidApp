package com.example.qlybandocu.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.adapters.ProductAdapter;
import com.example.qlybandocu.databinding.ActivityCategoryBinding;
import com.example.qlybandocu.listener.EventClickListener;
import com.example.qlybandocu.models.Products;
import com.example.qlybandocu.viewModel.CategoryViewModel;

public class CategoryActivity extends AppCompatActivity implements EventClickListener {
    ActivityCategoryBinding binding;
    CategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        initView();
        initData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initData() {
        int idcate = getIntent().getIntExtra("idcate",1);
        String namecate = getIntent().getStringExtra("namecate");
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        viewModel.productModelMutableLiveData(idcate).observe(this,productModel -> {
            if (productModel.isSuccess()){
                ProductAdapter adapter = new ProductAdapter(productModel.getResult(), this);
                binding.rcCategory.setAdapter(adapter);
                binding.tvname.setText(namecate + ":"+ productModel.getResult().size());
            }
        });
    }

    private void initView() {
        binding.rcCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.rcCategory.setLayoutManager(layoutManager);
    }

    @Override
    public void onPopularClick(Products products) {

    }
}