package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.models.Cart;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.models.ProductDetail;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    TextView tvTotalPrice;
    RadioGroup radioGroupPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);

        // ===== TÍNH & HIỂN THỊ TỔNG TIỀN (SỬA LỖI 3.45E7) =====
        double total = 0;
        for (Cart cart : Utils.cartList) {
            total += cart.getAmount()
                    * cart.getProductDetail().getPrice();
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTotalPrice.setText("Tổng tiền: " + decimalFormat.format(total) + " đ");
        // =====================================================

        findViewById(R.id.btnConfirmPayment)
                .setOnClickListener(v -> createOrder());
    }

    // ================= CREATE ORDER =================

    private void createOrder() {

        if (Utils.cartList == null || Utils.cartList.size() == 0) {
            Toast.makeText(this,
                    "Giỏ hàng trống",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utils.user_current == null) {
            Toast.makeText(this,
                    "Vui lòng đăng nhập",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int iduser = Utils.user_current.getId();

        // MOCK THÔNG TIN
        String address = "123 Nguyễn Văn A";
        String phone = "0901234567";
        String paymentMethod = getPaymentMethod();

        double total = 0;
        int quantity = 0;

        try {
            JSONArray jsonArray = new JSONArray();

            for (Cart cart : Utils.cartList) {

                ProductDetail p = cart.getProductDetail();

                JSONObject obj = new JSONObject();
                obj.put("idproduct", p.getId());
                obj.put("quantity", cart.getAmount());
                obj.put("price", p.getPrice());

                quantity += cart.getAmount();
                total += cart.getAmount() * p.getPrice();

                jsonArray.put(obj);
            }

            String itemsJson = jsonArray.toString();

            BanDoCuApi api = RetrofitInstance
                    .getRetrofit()
                    .create(BanDoCuApi.class);

            api.createOrder(
                    iduser,
                    address,
                    phone,
                    total,
                    quantity,
                    itemsJson,
                    paymentMethod
            ).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call,
                                       Response<MessageModel> response) {

                    if (response.body() != null
                            && response.body().isSuccess()) {

                        Toast.makeText(PaymentActivity.this,
                                "Thanh toán thành công (" +
                                        getPaymentMethod() + ")",
                                Toast.LENGTH_LONG).show();

                        // Clear giỏ hàng
                        Utils.cartList.clear();

                        startActivity(new Intent(
                                PaymentActivity.this,
                                OrderSuccessActivity.class
                        ));
                        finish();

                    } else {
                        Toast.makeText(PaymentActivity.this,
                                "Tạo đơn thất bại",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Toast.makeText(PaymentActivity.this,
                            t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Lỗi xử lý đơn hàng",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // ================= PAYMENT METHOD =================

    private String getPaymentMethod() {
        int checkedId = radioGroupPayment.getCheckedRadioButtonId();
        if (checkedId == R.id.radioCOD) return "COD";
        if (checkedId == R.id.radioEwallet) return "Ví điện tử";
        return "Chuyển khoản";
    }
}
