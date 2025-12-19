package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.databinding.ActivityShowDetailBinding;
import com.example.qlybandocu.models.Cart;
import com.example.qlybandocu.models.ProductDetail;
import com.example.qlybandocu.viewModel.ShowDetailViewModel;
import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.internal.Util;

public class ShowDetailActivity extends AppCompatActivity {
    ShowDetailViewModel viewModel;
    ActivityShowDetailBinding binding;
    int amount = 1;
    ProductDetail productDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_show_detail);
        Paper.init(this);
        int id = getIntent().getIntExtra("id", 0);
        getData(id);
        eventClick();
        showToData(id);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showToData(int id) {
        if (Paper.book().read("cart") != null){
            List<Cart> list = Paper.book().read("cart");
            Utils.cartList = list;
            Utils.cartList = Paper.book().read("cart");
        }


        if (Utils.cartList.size()>0){
            for (int i = 0; i<Utils.cartList.size(); i++){
                if (Utils.cartList.get(i).getProductDetail().getId() == id){
                    binding.txtamount.setText(Utils.cartList.get(i).getAmount() +"");
                }
            }

        } else {
            binding.txtamount.setText(amount + "");
        }
    }

    private void eventClick() {
        binding.imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(binding.txtamount.getText().toString()) + 1;
                binding.txtamount.setText(String.valueOf(amount));
            }
        });
        binding.imagesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(binding.txtamount.getText().toString()) > 1){
                    amount = Integer.parseInt(binding.txtamount.getText().toString()) - 1;
                    binding.txtamount.setText(String.valueOf(amount));
                }
            }
        });
        binding.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(amount);
            }
        });
    }

    private void addToCart(int amount) {
        boolean checkExit = false;
        int n = 0;
        if (Utils.cartList.size()>0){
            for (int i = 0; i<Utils.cartList.size(); i++){
                if (Utils.cartList.get(i).getProductDetail().getId() == productDetail.getId()){
                    checkExit = true;
                    n=i;
                    break;
                }
            }
        }
        if (checkExit){
            Utils.cartList.get(n).setAmount(amount);
        }else {
            Cart cart = new Cart();
            cart.setProductDetail(productDetail);
            cart.setAmount(amount);
            Utils.cartList.add(cart);
        }
        Toast.makeText(getApplicationContext(), "Adder to your cart", Toast.LENGTH_LONG).show();
        Paper.book().write("cart", Utils.cartList);
    }

    private void getData(int id) {

        viewModel = new ViewModelProvider(this).get(ShowDetailViewModel.class);
        viewModel.productDetailModelMutableLiveData(id).observe(this, productDetailModel -> {
            if(productDetailModel.isSuccess()){
                productDetail = productDetailModel.getResult().get(0);
                // --- XỬ LÝ HIỂN THỊ ---
                binding.txtnameproduct.setText(productDetail.getProductname());

                // CÁCH FORMAT GIÁ TIỀN VIỆT NAM:
                // Mẫu "###,###,###" sẽ tự động thêm dấu phân cách hàng nghìn
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                binding.txtprice.setText("Đơn Giá: " + decimalFormat.format(productDetail.getPrice()) + "₫");

                binding.txtdesc.setText(productDetail.getDescription());
                Glide.with(this).load(productDetail.getStrproductthumb()).into(binding.image);
            }
        });
    }
}