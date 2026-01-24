package com.example.qlybandocu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword;
    Button btnRegister;
    TextView txtToLogin;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtToLogin = findViewById(R.id.txtToLogin);

        // Quay lại Login
        txtToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this,
                        "Điền đầy đủ thông tin!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(name, email, pass);
        });
    }

    private void registerUser(String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Toast.makeText(this,
                                "Đăng ký thất bại: " +
                                        task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) return;

                    String uid = user.getUid();

                    // ===== LƯU THÔNG TIN CƠ BẢN =====
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("email", email);
                    userMap.put("profileCompleted", false); // ⭐ QUAN TRỌNG

                    db.collection("users")
                            .document(uid)
                            .set(userMap);

                    // ===== GỬI EMAIL XÁC THỰC =====
                    user.sendEmailVerification()
                            .addOnCompleteListener(verifyTask -> {

                                if (verifyTask.isSuccessful()) {

                                    Toast.makeText(this,
                                            "Đăng ký thành công! Vui lòng xác thực email.",
                                            Toast.LENGTH_LONG).show();

                                    // 👉 CHUYỂN SANG NHẬP THÔNG TIN
                                    startActivity(new Intent(
                                            RegisterActivity.this,
                                            UpdateProfileActivity.class
                                    ));
                                    finish();

                                } else {
                                    Toast.makeText(this,
                                            "Lỗi gửi email xác thực: " +
                                                    verifyTask.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                });
    }
}
