package com.example.qlybandocu.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import com.example.qlybandocu.activities.LoginActivity;

public class AuthGuard {

    public static boolean requireLogin(Activity activity) {

        if (Utils.user_current != null) {
            return true;
        }

        new AlertDialog.Builder(activity)
                .setTitle("Yêu cầu đăng nhập")
                .setMessage("Bạn cần đăng nhập để sử dụng chức năng này.")
                .setCancelable(false)
                .setNegativeButton("Huỷ", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Đăng nhập", (dialog, which) -> {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                })
                .show();

        return false;
    }
}

