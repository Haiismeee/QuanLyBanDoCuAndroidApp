package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        // ===== TÍNH & HIỂN THỊ TỔNG TIỀN =====
        double total = 0;
        for (Cart cart : Utils.cartList) {
            total += cart.getAmount()
                    * cart.getProductDetail().getPrice();
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTotalPrice.setText("Tổng tiền: " + decimalFormat.format(total) + " đ");
        // ===================================

        findViewById(R.id.btnConfirmPayment).setOnClickListener(v -> {

            String method = getPaymentMethod();

            if (method.equals("UNKNOWN")) {
                Toast.makeText(this,
                        "Vui lòng chọn phương thức thanh toán",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (method.equals("PAYOS_QR")) {
                showPayOSDialog();
            } else {
                createOrder(method); // COD
            }
        });
    }

    // ================= CREATE ORDER =================

    private void createOrder(String paymentMethod) {

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

        // ✅ LẤY THÔNG TIN THẬT
        String address = Utils.user_current.getAddress();
        String phone   = Utils.user_current.getPhone();

        if (address == null || address.isEmpty()
                || phone == null || phone.isEmpty()) {

            Toast.makeText(this,
                    "Vui lòng cập nhật địa chỉ và số điện thoại trước khi thanh toán",
                    Toast.LENGTH_LONG).show();
            return;
        }

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
                                "Thanh toán thành công (" + paymentMethod + ")",
                                Toast.LENGTH_LONG).show();

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
        if (checkedId == R.id.radioPayOS) return "PAYOS_QR";
        return "UNKNOWN";
    }

    // ================= PAYOS MOCK =================

    private void showPayOSDialog() {

        View view = getLayoutInflater()
                .inflate(R.layout.dialog_payos_qr, null);

        TextView tvAmount = view.findViewById(R.id.tvAmount);
        tvAmount.setText("Số tiền: " + tvTotalPrice.getText());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        view.findViewById(R.id.btnPaid).setOnClickListener(v -> {
            dialog.dismiss();
            createOrder("PAYOS_QR");
        });

        dialog.show();
    }
}
