package com.example.qlybandocu.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.qlybandocu.retrofit.RetrofitInstance;
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

    private ActivityDangTinBinding binding;
    private BanDoCuApi api;

    private Uri imageUri;
    private String imageUrl = "";
    private int idCategorySelected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangTinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        api = RetrofitInstance.getRetrofit().create(BanDoCuApi.class);

        initView();
        loadCategory();
        initAction();
    }

    private void initView() {
        binding.imgBack.setOnClickListener(v -> finish());
    }

    private void loadCategory() {
        api.getCategory().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    List<Category> list = response.body().getResult();
                    List<String> names = new ArrayList<>();
                    for (Category c : list) names.add(c.getCategory());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            DangTinActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            names
                    );
                    binding.spinnerLoai.setAdapter(adapter);

                    binding.spinnerLoai.setOnItemSelectedListener(
                            new android.widget.AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(android.widget.AdapterView<?> parent,
                                                           android.view.View view,
                                                           int position,
                                                           long id) {
                                    idCategorySelected = list.get(position).getId();
                                }

                                @Override
                                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                            });
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Toast.makeText(DangTinActivity.this,
                        "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAction() {

        binding.imgCamera.setOnClickListener(v ->
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .start()
        );

        binding.btnDangTin.setOnClickListener(v -> uploadImage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.imgProducts.setImageURI(imageUri);
        }
    }

    // ===== BƯỚC 1: UPLOAD ẢNH =====
    private void uploadImage() {

        if (imageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = createFileFromUri(imageUri);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            api.uploadImage(body).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.body() != null && response.body().isSuccess()) {
                        imageUrl = response.body().getMessage();
                        insertProduct();
                    } else {
                        Toast.makeText(DangTinActivity.this,
                                response.body() != null ? response.body().getMessage() : "Upload ảnh lỗi",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Toast.makeText(DangTinActivity.this,
                            t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    // ===== BƯỚC 2: INSERT VÀO products =====
    private void insertProduct() {
        if (Utils.user_current == null) {
            Toast.makeText(this,
                    "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        int iduser = 0;
        String tensp = binding.edtTenSp.getText().toString().trim();

        if (TextUtils.isEmpty(tensp)) {
            Toast.makeText(this, "Nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }


        api.insertProduct(tensp, imageUrl, idCategorySelected, iduser)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(DangTinActivity.this,
                                    "Đăng tin thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DangTinActivity.this,
                                    response.body() != null ? response.body().getMessage() : "Insert lỗi",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(DangTinActivity.this,
                                t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private File createFileFromUri(Uri uri) throws Exception {

        String fileName = "upload_" + System.currentTimeMillis() + ".jpg";
        File file = new File(getCacheDir(), fileName);

        try (java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
             java.io.OutputStream outputStream = new java.io.FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }

        return file;
    }
}
