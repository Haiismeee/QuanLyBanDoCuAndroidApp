package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.models.UserModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtToRegister, txtForgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtToRegister = findViewById(R.id.txtToRegister);
        txtForgotPassword = findViewById(R.id.tvForgotPassword);
        TextView txtGuest = findViewById(R.id.btnGuest);

        txtGuest.setOnClickListener(v -> loginAsGuest());

        txtToRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(email, pass);
        });

        if (txtForgotPassword != null) {
            txtForgotPassword.setOnClickListener(v -> {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(this, "Nhập email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this,
                                        "Email đặt lại mật khẩu đã được gửi!",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            });
        }
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Toast.makeText(this,
                                "Sai email hoặc mật khẩu!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser == null || !firebaseUser.isEmailVerified()) {
                        Toast.makeText(this,
                                "Vui lòng xác thực email!",
                                Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        return;
                    }

                    String firebaseUid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName() != null
                            ? firebaseUser.getDisplayName()
                            : "Người dùng";
                    String userEmail = firebaseUser.getEmail();

                    BanDoCuApi api = RetrofitInstance
                            .getRetrofit()
                            .create(BanDoCuApi.class);

                    // 🔹 1. THỬ LẤY USER MYSQL
                    api.getUserByFirebase(firebaseUid)
                            .enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Call<UserModel> call,
                                                       Response<UserModel> response) {

                                    if (response.body() != null
                                            && response.body().isSuccess()) {

                                        Utils.user_current = response.body().getResult();
                                        goHome();
                                    } else {
                                        // 🔹 2. CHƯA CÓ → SYNC USER
                                        syncUser(api, firebaseUid, userEmail, name);
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserModel> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this,
                                            "Lỗi server: " + t.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                });
    }
    private void loginAsGuest() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this,
                                "Không thể đăng nhập khách",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();

                    // ⚠️ QUAN TRỌNG
                    // Guest KHÔNG có user trong MySQL
                    Utils.user_current = null;
                    Utils.isGuest = true; // <-- bạn thêm biến này

                    Toast.makeText(this,
                            "Đang dùng với tư cách khách",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                });
    }


    private void syncUser(BanDoCuApi api,
                          String firebaseUid,
                          String email,
                          String name) {

        api.syncUser(firebaseUid, email, name)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (!response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "HTTP error: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (response.body() == null) {
                            Toast.makeText(LoginActivity.this,
                                    "Response body null",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (response.body().isSuccess()) {
                            Utils.user_current = response.body().getResult();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    response.body().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,
                                "Lỗi sync user: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goHome() {
        Toast.makeText(this,
                "Đăng nhập thành công!",
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


}