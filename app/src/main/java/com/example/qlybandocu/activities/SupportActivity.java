package com.example.qlybandocu.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.R;

public class SupportActivity extends AppCompatActivity {

    Button btnCall, btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        // Ánh xạ view
        btnCall = findViewById(R.id.btnCall);
        btnEmail = findViewById(R.id.btnEmail);

        // 📞 Gọi hotline
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0901234567"));
            startActivity(intent);
        });

        // ✉️ Gửi email hỗ trợ
        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@qlybandocu.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hỗ trợ ứng dụng QlyBanDoCu");
            startActivity(intent);
        });
    }
}
