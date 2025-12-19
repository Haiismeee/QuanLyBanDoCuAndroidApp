package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.adapters.CartAdapter;
import com.example.qlybandocu.databinding.ActivityCartBinding;
import com.example.qlybandocu.listener.ChangeNumListener;
import com.example.qlybandocu.models.Cart;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        Paper.init(this);
        initView();
        initData();
        totalPrice();
        initControl();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initControl() {
        binding.btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cart = new Gson().toJson(Utils.cartList);
                Log.d("loggg", cart);
            }
        });
    }

    private void initData() {
        List<Cart> carts = Paper.book().read("cart");
        Utils.cartList = carts;
        CartAdapter adapter = new CartAdapter(this, Utils.cartList, new ChangeNumListener() {
            @Override
            public void change() {
                totalPrice();
            }
        });
        binding.recyclecart.setAdapter(adapter);
    }

    private void totalPrice() {
        int item = 0;
        for (int i=0; i<Utils.cartList.size(); i++){
            item = item + Utils.cartList.get(i).getAmount();
        }
        binding.txtitem.setText(String.valueOf(item));

        Double price = 0.0;
        for (int i=0; i<Utils.cartList.size(); i++){
            price = price + (Utils.cartList.get(i).getAmount() * Utils.cartList.get(i).getProductDetail().getPrice());
        }

        // --- SỬA ĐOẠN HIỂN THỊ GIÁ ---
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.txtprice.setText(decimalFormat.format(price) + " đ");
        // ------------------------------
    }

    private void initView() {
        binding.recyclecart.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclecart.setLayoutManager(layoutManager);
    }
}