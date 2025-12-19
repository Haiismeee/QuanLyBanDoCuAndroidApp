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

            // Sửa chỗ này: Gọi hàm mới
            mediaPath = getPathFromUri(uri);

            binding.imgProducts.setImageURI(uri);
            binding.imgCamera.setVisibility(View.GONE);
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

        // Kiểm tra file có tồn tại không trước khi gửi
        File file = new File(mediaPath);
        if (!file.exists()) {
            Toast.makeText(this, "Không tìm thấy file ảnh!", Toast.LENGTH_SHORT).show();
            Log.e("UploadError", "File not found: " + mediaPath);
            return;
        }

        String str_iduser = (Utils.user_current != null && Utils.user_current.getId() > 0)
                ? String.valueOf(Utils.user_current.getId()) : "1";

        // --- QUAY LẠI CẤU HÌNH CHUẨN (MULTIPART/FORM-DATA) ---
        // PHP thường ưa chuộng kiểu này nhất
        RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBodyImg = MultipartBody.Part.createFormData("hinhanh", file.getName(), requestBodyImg);

        RequestBody requestBodyTen = RequestBody.create(MediaType.parse("multipart/form-data"), str_ten);
        RequestBody requestBodyGia = RequestBody.create(MediaType.parse("multipart/form-data"), str_gia);
        RequestBody requestBodyMoTa = RequestBody.create(MediaType.parse("multipart/form-data"), str_mota);
        RequestBody requestBodyLoai = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idCategorySelected));
        RequestBody requestBodyIdUser = RequestBody.create(MediaType.parse("multipart/form-data"), str_iduser);

        banDoCuApi.insertSp(requestBodyTen, requestBodyGia, requestBodyMoTa, requestBodyLoai, requestBodyIdUser, multipartBodyImg)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Đăng tin thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // Server trả về success: false
                                String msg = (response.body() != null) ? response.body().getMessage() : "Lỗi không xác định";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Lỗi Server (Code: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UploadError", "Lỗi: " + t.getMessage());
                    }
                });
    }

    private String getPathFromUri(Uri uri) {
        try {
            // Tạo file tạm trong thư mục cache của app
            File file = new File(getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".png");

            // Mở luồng đọc từ Uri và luồng ghi vào file tạm
            java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
            java.io.OutputStream outputStream = new java.io.FileOutputStream(file);

            // Copy dữ liệu
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            // Trả về đường dẫn tuyệt đối của file tạm
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}