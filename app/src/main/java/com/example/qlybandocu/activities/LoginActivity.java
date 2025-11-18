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

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtToRegister, txtForgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtToRegister = findViewById(R.id.txtToRegister);
        txtForgotPassword = findViewById(R.id.tvForgotPassword); // nếu bạn có nút quên mk

        // Chuyển sang Register
        txtToRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // Login
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(email, pass);
        });

        // Quên mật khẩu (nếu có nút)
        if(txtForgotPassword != null) {
            txtForgotPassword.setOnClickListener(v -> {
                String email = edtEmail.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(this, "Nhập email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(this, "Email đặt lại mật khẩu đã được gửi!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            });
        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Vui lòng xác thực email trước khi đăng nhập!", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
