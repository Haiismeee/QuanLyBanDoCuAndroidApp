package com.example.qlybandocu.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlybandocu.R;
import com.example.qlybandocu.Utils.Utils;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.retrofit.BanDoCuApi;
import com.example.qlybandocu.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText edtComment;
    Button btnSubmit;

    int idorder, idproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ratingBar = findViewById(R.id.ratingBar);
        edtComment = findViewById(R.id.edtComment);
        btnSubmit = findViewById(R.id.btnSubmitReview);

        idorder = getIntent().getIntExtra("idorder", 0);
        idproduct = getIntent().getIntExtra("idproduct", 0);

        btnSubmit.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {

        int rating = (int) ratingBar.getRating();
        String comment = edtComment.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this,
                    "Vui lòng chọn số sao",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        BanDoCuApi api = RetrofitInstance.getRetrofit()
                .create(BanDoCuApi.class);

        api.addReview(
                Utils.user_current.getId(),
                idproduct,
                idorder,
                rating,
                comment
        ).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call,
                                   Response<MessageModel> response) {

                if (response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(ReviewActivity.this,
                            "Cảm ơn bạn đã đánh giá ❤️",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ReviewActivity.this,
                            "Gửi đánh giá thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(ReviewActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}