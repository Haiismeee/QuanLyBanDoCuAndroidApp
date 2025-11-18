package com.example.qlybandocu.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlybandocu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private TextView tvAccountTitle;
    private Button btnEditProfile, btnChangePassword, btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ view
        tvAccountTitle = findViewById(R.id.tvAccountTitle);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        loadUserInfo();

        btnEditProfile.setOnClickListener(v -> editProfile());
        btnChangePassword.setOnClickListener(v -> changePassword());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserInfo() {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            // Lấy tên từ Firestore
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String name = documentSnapshot.contains("name") ? documentSnapshot.getString("name") : "Người dùng";
                        tvAccountTitle.setText("Xin chào, " + name + " (" + email + ")");
                    })
                    .addOnFailureListener(e -> {
                        tvAccountTitle.setText("Xin chào, " + email);
                    });
        }
    }

    private void editProfile() {
        // Hiển thị dialog chỉnh sửa tên
        EditText edtName = new EditText(this);
        edtName.setHint("Nhập tên mới");
        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String currentName = documentSnapshot.contains("name") ? documentSnapshot.getString("name") : "";
                    edtName.setText(currentName);
                });

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa thông tin")
                .setView(edtName)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = edtName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        Map<String, Object> updateMap = new HashMap<>();
                        updateMap.put("name", newName);
                        db.collection("users").document(currentUser.getUid())
                                .update(updateMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    loadUserInfo();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void changePassword() {
        EditText edtNewPass = new EditText(this);
        edtNewPass.setHint("Nhập mật khẩu mới");

        new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(edtNewPass)
                .setPositiveButton("Đổi", (dialog, which) -> {
                    String newPass = edtNewPass.getText().toString().trim();
                    if (!newPass.isEmpty()) {
                        currentUser.updatePassword(newPass)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Đổi mật khẩu thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        Toast.makeText(this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        finish();
    }
}
