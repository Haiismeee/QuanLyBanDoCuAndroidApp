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
    private Button btnMyPosts;


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
        btnMyPosts = findViewById(R.id.btnMyPosts);
        btnMyPosts.setOnClickListener(v -> {
            startActivity(new Intent(AccountActivity.this, MyPostActivity.class));
        });
        findViewById(R.id.btnOrderHistory).setOnClickListener(v -> {
            startActivity(new Intent(
                    AccountActivity.this, OrderHistoryActivity.class));
        });
        findViewById(R.id.btnDelivery).setOnClickListener(v -> {
            startActivity(new Intent(this, DeliveryActivity.class));
        });


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
        new AlertDialog.Builder(AccountActivity.this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setCancelable(false)
                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(AccountActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}