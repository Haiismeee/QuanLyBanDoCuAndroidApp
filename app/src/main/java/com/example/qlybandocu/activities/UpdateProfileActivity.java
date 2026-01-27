package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress;
    Button btnSave, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);

        btnSave = findViewById(R.id.btnSaveProfile);
        btnSkip = findViewById(R.id.btnSkipProfile);

        // ===== HIỂN THỊ THÔNG TIN HIỆN TẠI (NẾU CÓ) =====
        if (Utils.user_current != null) {
            edtName.setText(Utils.user_current.getName());
            edtPhone.setText(Utils.user_current.getPhone());
            edtAddress.setText(Utils.user_current.getAddress());
        }

        // ===== LƯU THÔNG TIN =====
        btnSave.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (Utils.user_current == null) {
                Toast.makeText(this,
                        "Vui lòng đăng nhập",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfileToServer(
                    Utils.user_current.getId(),
                    name,
                    phone,
                    address
            );
        });

        // ===== NHẬP SAU (BỎ QUA) =====
        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(
                    UpdateProfileActivity.this,
                    HomeActivity.class
            ));
            finish();
        });
    }

    // ================= UPDATE MYSQL =================

    private void updateProfileToServer(int idUser,
                                       String name,
                                       String phone,
                                       String address) {

        BanDoCuApi api = RetrofitInstance
                .getRetrofit()
                .create(BanDoCuApi.class);

        api.updateProfile(
                idUser,
                name,
                phone,
                address
        ).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call,
                                   Response<MessageModel> response) {

                if (response.body() != null
                        && response.body().isSuccess()) {

                    // ✅ CẬP NHẬT LẠI USER TRONG APP (QUAN TRỌNG)
                    Utils.user_current.setName(name);
                    Utils.user_current.setPhone(phone);
                    Utils.user_current.setAddress(address);

                    Toast.makeText(UpdateProfileActivity.this,
                            "Cập nhật thông tin thành công",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(
                            UpdateProfileActivity.this,
                            HomeActivity.class
                    ));
                    finish();

                } else {
                    Toast.makeText(UpdateProfileActivity.this,
                            "Cập nhật thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this,
                        "Lỗi kết nối server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
