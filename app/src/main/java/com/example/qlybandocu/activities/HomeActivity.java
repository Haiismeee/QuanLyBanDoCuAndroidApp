package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlybandocu.R;
import com.example.qlybandocu.adapters.CategoryAdapter;
import com.example.qlybandocu.adapters.PopularAdapter;
import com.example.qlybandocu.databinding.ActivityHomeBinding;
import com.example.qlybandocu.listener.CategoryListener;
import com.example.qlybandocu.listener.EventClickListener;
import com.example.qlybandocu.models.Category;
import com.example.qlybandocu.models.Products;
import com.example.qlybandocu.viewModel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements CategoryListener, EventClickListener {

    ActivityHomeBinding binding;
    HomeViewModel homeViewModel;

    EditText editsearch;

    PopularAdapter popularAdapter;
    List<Products> popularList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initView();
        initData();
        initSearch();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    // ================= VIEW =================

    private void initView() {

        // CATEGORY
        binding.rcCategory.setHasFixedSize(true);
        binding.rcCategory.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // POPULAR
        binding.rcPopular.setHasFixedSize(true);
        binding.rcPopular.setLayoutManager(new GridLayoutManager(this, 3));

        popularAdapter = new PopularAdapter(popularList, this);
        binding.rcPopular.setAdapter(popularAdapter);

        // Cart
        binding.floatingbtn.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );

        // Account
        binding.imgProfile.setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class))
        );

        // Đăng tin
        if (binding.btnThemSp != null) {
            binding.btnThemSp.setOnClickListener(v ->
                    startActivity(new Intent(this, DangTinActivity.class))
            );
        }

        // ===== Bottom menu =====

        // INFO
        binding.btnInfo.setOnClickListener(v ->
                startActivity(new Intent(this, InfoActivity.class))
        );

        // SUPPORT
        binding.btnSupport.setOnClickListener(v ->
                startActivity(new Intent(this, SupportActivity.class))
        );

        // SETTINGS
        binding.btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class))
        );

    }

    // ================= DATA =================

    private void initData() {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // CATEGORY
        homeViewModel.categoryModelMutableLiveData()
                .observe(this, categoryModel -> {
                    if (categoryModel != null && categoryModel.isSuccess()) {
                        binding.rcCategory.setAdapter(
                                new CategoryAdapter(categoryModel.getResult(), this)
                        );
                    }
                });

        // POPULAR – LOAD 1 LẦN
        homeViewModel.productModelMutableLiveData(1)
                .observe(this, productModel -> {
                    if (productModel != null && productModel.isSuccess()) {
                        popularList.clear();
                        popularList.addAll(productModel.getResult());
                        popularAdapter.notifyDataSetChanged();
                    }
                });
    }

    // ================= SEARCH (LOCAL FILTER) =================

    private void initSearch() {
        editsearch = findViewById(R.id.editsearch);

        editsearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                String keyword = editsearch.getText().toString()
                        .trim().toLowerCase();

                List<Products> filtered = new ArrayList<>();

                if (keyword.isEmpty()) {
                    filtered.addAll(popularList);
                } else {
                    for (Products p : popularList) {
                        if (p.getStrProduct() != null &&
                                p.getStrProduct().toLowerCase().contains(keyword)) {
                            filtered.add(p);
                        }
                    }
                }

                binding.rcPopular.setAdapter(
                        new PopularAdapter(filtered, this)
                );
                return true;
            }
            return false;
        });
    }

    // ================= CLICK =================

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("idcate", category.getId());
        intent.putExtra("namecate", category.getCategory());
        startActivity(intent);
    }

    @Override
    public void onPopularClick(Products products) {
        Intent intent = new Intent(this, ShowDetailActivity.class);
        intent.putExtra("id", products.getIdProduct());
        startActivity(intent);
    }
}