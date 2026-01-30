package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.AuthGuard;
import com.example.qlybandocu.adapters.CategoryAdapter;
import com.example.qlybandocu.adapters.PopularAdapter;
import com.example.qlybandocu.databinding.ActivityHomeBinding;
import com.example.qlybandocu.listener.CategoryListener;
import com.example.qlybandocu.listener.EventClickListener;
import com.example.qlybandocu.models.Category;
import com.example.qlybandocu.models.Products;
import com.example.qlybandocu.viewModel.HomeViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlybandocu.Utils.GridSpacingItemDecoration;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements CategoryListener, EventClickListener {

    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;

    private PopularAdapter popularAdapter;
    private final List<Products> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initView();
        initAction();
        initData();
        initSearch();
    }

    // ================= VIEW =================

    private void initView() {

        binding.rcCategory.setLayoutManager(
                new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)
        );

        int spacing = getResources().getDimensionPixelSize(R.dimen._6sdp);
        binding.rcCategory.addItemDecoration(
                new GridSpacingItemDecoration(2, spacing, false)
        );

        binding.rcPopular.setLayoutManager(new GridLayoutManager(this, 2));
        popularAdapter = new PopularAdapter(productList, this);
        binding.rcPopular.setAdapter(popularAdapter);
    }


    // ================= ACTION =================

    private void initAction() {

        binding.floatingbtn.setOnClickListener(v ->{
            if (!AuthGuard.requireLogin(this)) return;

            startActivity(new Intent(this, CartActivity.class));
        });

        binding.imgProfile.setOnClickListener(v ->{
                if (!AuthGuard.requireLogin(this)) return;

                startActivity(new Intent(this, AccountActivity.class));
        });

        binding.btnThemSp.setOnClickListener(v ->
                startActivity(new Intent(this, DangTinActivity.class))
        );

        binding.btnInfo.setOnClickListener(v ->
                startActivity(new Intent(this, InfoActivity.class))
        );

        binding.btnSupport.setOnClickListener(v ->
                startActivity(new Intent(this, SupportActivity.class))
        );

        binding.btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class))
        );
    }

    // ================= DATA =================

    private void initData() {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.categoryModelMutableLiveData()
                .observe(this, model -> {
                    if (model != null && model.isSuccess()) {
                        binding.rcCategory.setAdapter(
                                new CategoryAdapter(model.getResult(), this)
                        );
                    }
                });

        loadLatestProducts();
    }

    private void loadLatestProducts() {
        homeViewModel.productModelMutableLiveData(1)
                .observe(this, model -> {
                    if (model != null && model.isSuccess()) {
                        productList.clear();
                        productList.addAll(model.getResult());
                        popularAdapter.notifyDataSetChanged();
                    }
                });
    }

    // ================= SEARCH (AN TOÀN, KHÔNG LỖI) =================

    private void initSearch() {
        binding.editsearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                String keyword = binding.editsearch.getText()
                        .toString()
                        .trim()
                        .replaceAll("\\s+", " ");


                if (keyword.isEmpty()) {
                    Toast.makeText(this,
                            "Vui lòng nhập từ khóa tìm kiếm",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                // 👉 TÁI SỬ DỤNG CategoryActivity Ở CHẾ ĐỘ SEARCH
                Intent intent = new Intent(this, CategoryActivity.class);
                intent.putExtra("keyword", keyword);
                intent.putExtra("isSearch", true);
                startActivity(intent);

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

    @Override
    protected void onResume() {
        super.onResume();
        loadLatestProducts();
    }
}
