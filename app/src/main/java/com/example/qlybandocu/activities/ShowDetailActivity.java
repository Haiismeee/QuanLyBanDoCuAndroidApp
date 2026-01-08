package com.example.qlybandocu.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;
import com.example.qlybandocu.viewModel.ShowDetailViewModel;
import com.example.qlybandocu.models.RatingModel;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailActivity extends AppCompatActivity {

    ShowDetailViewModel viewModel;
    ActivityShowDetailBinding binding;

    int amount = 1;
    ProductDetail productDetail;

    Button btnContactSeller;

    // ===== ĐÁNH GIÁ =====
    RatingBar ratingAvg;
    TextView tvTotalReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_show_detail
        );

        Paper.init(this);

        // ===== ÁNH XẠ ĐÁNH GIÁ =====
        ratingAvg = findViewById(R.id.ratingAvg);
        tvTotalReview = findViewById(R.id.tvTotalReview);

        int id = getIntent().getIntExtra("id", 0);

        getData(id);
        eventClick();
        showToData(id);

        btnContactSeller = findViewById(R.id.btnContactSeller);
        btnContactSeller.setOnClickListener(v -> showContactDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                });
    }

    // ================= HIỂN THỊ SỐ LƯỢNG =================

    private void showToData(int id) {

        if (Paper.book().read("cart") != null) {
            Utils.cartList = Paper.book().read("cart");
        }

        if (Utils.cartList.size() > 0) {
            for (int i = 0; i < Utils.cartList.size(); i++) {
                if (Utils.cartList.get(i)
                        .getProductDetail().getId() == id) {
                    binding.txtamount.setText(
                            Utils.cartList.get(i).getAmount() + ""
                    );
                }
            }
        } else {
            binding.txtamount.setText(amount + "");
        }
    }

    // ================= SỰ KIỆN CLICK =================

    private void eventClick() {

        binding.imageadd.setOnClickListener(view -> {
            amount = Integer.parseInt(
                    binding.txtamount.getText().toString()
            ) + 1;
            binding.txtamount.setText(String.valueOf(amount));
        });

        binding.imagesub.setOnClickListener(view -> {
            if (Integer.parseInt(
                    binding.txtamount.getText().toString()
            ) > 1) {
                amount = Integer.parseInt(
                        binding.txtamount.getText().toString()
                ) - 1;
                binding.txtamount.setText(String.valueOf(amount));
            }
        });

        binding.btnadd.setOnClickListener(view -> addToCart(amount));
    }

    // ================= ADD TO CART =================

    private void addToCart(int amount) {

        boolean checkExit = false;
        int n = 0;

        if (Utils.cartList.size() > 0) {
            for (int i = 0; i < Utils.cartList.size(); i++) {
                if (Utils.cartList.get(i)
                        .getProductDetail().getId()
                        == productDetail.getId()) {
                    checkExit = true;
                    n = i;
                    break;
                }
            }
        }

        if (checkExit) {
            Utils.cartList.get(n).setAmount(amount);
        } else {
            Cart cart = new Cart();
            cart.setProductDetail(productDetail);
            cart.setAmount(amount);
            Utils.cartList.add(cart);
        }

        Toast.makeText(
                getApplicationContext(),
                "Added to your cart",
                Toast.LENGTH_LONG
        ).show();

        Paper.book().write("cart", Utils.cartList);
    }

    // ================= LOAD DATA SP =================

    private void getData(int id) {

        viewModel = new ViewModelProvider(this)
                .get(ShowDetailViewModel.class);

        viewModel.productDetailModelMutableLiveData(id)
                .observe(this, productDetailModel -> {

                    if (productDetailModel.isSuccess()) {

                        productDetail =
                                productDetailModel.getResult().get(0);

                        binding.txtnameproduct.setText(
                                productDetail.getProductname()
                        );

                        DecimalFormat df =
                                new DecimalFormat("###,###,###");
                        binding.txtprice.setText(
                                "Đơn Giá: " +
                                        df.format(productDetail.getPrice()) +
                                        "₫"
                        );

                        binding.txtdesc.setText(
                                productDetail.getDescription()
                        );

                        Glide.with(this)
                                .load(productDetail.getStrproductthumb())
                                .into(binding.image);

                        // ===== LOAD ĐÁNH GIÁ TRUNG BÌNH =====
                        loadRating(productDetail.getId());
                    }
                });
    }

    // ================= LOAD RATING =================

    private void loadRating(int idproduct) {

        BanDoCuApi api =
                RetrofitInstance.getRetrofit()
                        .create(BanDoCuApi.class);

        api.getAvgRating(idproduct)
                .enqueue(new Callback<RatingModel>() {
                    @Override
                    public void onResponse(
                            Call<RatingModel> call,
                            Response<RatingModel> res
                    ) {
                        if (res.body() != null && res.body().isSuccess()) {
                            ratingAvg.setRating(
                                    res.body().getAvg_rating()
                            );
                            tvTotalReview.setText(
                                    "(" + res.body().getTotal()
                                            + " đánh giá)"
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<RatingModel> call,
                            Throwable t
                    ) {
                        // demo
                    }
                });
    }

    // ================= LIÊN HỆ NGƯỜI BÁN =================

    private void showContactDialog() {

        String[] options = {"Gọi điện", "Gửi email"};

        new AlertDialog.Builder(this)
                .setTitle("Liên hệ người bán")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) callSeller();
                    else emailSeller();
                })
                .show();
    }

    private void callSeller() {
        String phone = "0901234567";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void emailSeller() {
        String email = "seller@gmail.com";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Liên hệ về sản phẩm"
        );
        intent.putExtra(
                Intent.EXTRA_TEXT,
                "Tôi muốn hỏi thêm thông tin về sản phẩm."
        );
        startActivity(
                Intent.createChooser(intent, "Chọn ứng dụng email")
        );
    }
}
