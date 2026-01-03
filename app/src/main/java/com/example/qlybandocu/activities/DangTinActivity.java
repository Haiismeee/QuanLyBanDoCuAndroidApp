package com.example.qlybandocu.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.databinding.ActivityDangTinBinding;
import com.example.qlybandocu.models.Category;
import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance; // Dùng đúng file bạn đang có
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangTinActivity extends AppCompatActivity {
    ActivityDangTinBinding binding;
    BanDoCuApi banDoCuApi;
    String mediaPath;
    int idCategorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangTinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // GỌI ĐÚNG file RetrofitInstance của bạn
        banDoCuApi = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        initView();
        initData();
        initControl();
    }

    private void initView() {
        binding.imgBack.setOnClickListener(v -> finish());
    }

    private void initData() {
        banDoCuApi.getCategory().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Category> list = response.body().getResult();
                    List<String> listName = new ArrayList<>();
                    for (Category category : list) {
                        listName.add(category.getCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DangTinActivity.this, android.R.layout.simple_spinner_dropdown_item, listName);
                    binding.spinnerLoai.setAdapter(adapter);
                    binding.spinnerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            idCategorySelected = list.get(i).getId();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initControl() {
        binding.imgCamera.setOnClickListener(view -> {
            ImagePicker.with(DangTinActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        binding.btnDangTin.setOnClickListener(view -> postProduct());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            // --- SỬA Ở ĐÂY ---
            mediaPath = getPathFromUri(uri);
            // ----------------

            binding.imgProducts.setImageURI(uri); // Hiển thị lên giao diện
            // binding.imgCamera.setVisibility(View.GONE); // Ẩn icon camera nếu cần
        }
    }

    private void postProduct() {
        String str_ten = binding.edtTenSp.getText().toString().trim();
        String str_gia = binding.edtGia.getText().toString().trim();
        String str_mota = binding.edtMoTa.getText().toString().trim();

        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mediaPath)) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(mediaPath);
        if (!file.exists()) {
            Toast.makeText(this, "Lỗi ảnh: Không tìm thấy file trong bộ nhớ đệm", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ID User
        String str_iduser = (Utils.user_current != null && Utils.user_current.getId() > 0)
                ? String.valueOf(Utils.user_current.getId()) : "0";

        // --- PHẦN SỬA ĐỔI QUAN TRỌNG ---

        // 1. Ảnh thì giữ nguyên multipart/form-data
        RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBodyImg = MultipartBody.Part.createFormData("hinhanh", file.getName(), requestBodyImg);

        // 2. Chữ thì đổi sang text/plain để PHP dễ đọc hơn
        RequestBody requestBodyTen = RequestBody.create(MediaType.parse("text/plain"), str_ten);
        RequestBody requestBodyGia = RequestBody.create(MediaType.parse("text/plain"), str_gia);
        RequestBody requestBodyMoTa = RequestBody.create(MediaType.parse("text/plain"), str_mota);
        RequestBody requestBodyLoai = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idCategorySelected));
        RequestBody requestBodyIdUser = RequestBody.create(MediaType.parse("text/plain"), str_iduser);

        // Gọi API
        banDoCuApi.insertSp(requestBodyTen, requestBodyGia, requestBodyMoTa, requestBodyLoai, requestBodyIdUser, multipartBodyImg)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Đăng tin thành công!", Toast.LENGTH_SHORT).show();
                                finish(); // Đóng màn hình quay về Home
                            } else {
                                String msg = (response.body() != null) ? response.body().getMessage() : "Lỗi không xác định";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // In lỗi ra logcat để xem nếu server trả về 500
                            Log.e("UploadError", "Code: " + response.code() + ", Mess: " + response.message());
                            Toast.makeText(getApplicationContext(), "Lỗi Server: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Log.e("UploadError", "Lỗi kết nối: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getPathFromUri(android.net.Uri uri) {
        try {
            android.content.ContentResolver contentResolver = getContentResolver();
            // Tạo tên file tạm
            String fileName = "temp_image_" + System.currentTimeMillis() + ".png";
            java.io.File file = new java.io.File(getCacheDir(), fileName);

            // Copy dữ liệu từ Uri vào file tạm
            java.io.InputStream inputStream = contentResolver.openInputStream(uri);
            java.io.OutputStream outputStream = new java.io.FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath(); // Trả về đường dẫn file thật
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}