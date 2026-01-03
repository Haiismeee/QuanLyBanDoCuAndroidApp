package com.example.qlybandocu.retrofit;


import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.models.MyPostModel;
import com.example.qlybandocu.models.ProductDetail;
import com.example.qlybandocu.models.ProductDetailModel;
import com.example.qlybandocu.models.ProductModel;
import com.example.qlybandocu.models.UserModel;


import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BanDoCuApi {

    // ===== DANH MỤC =====
    @GET("category.php")
    Call<CategoryModel> getCategory();

    // ===== SẢN PHẨM =====
    @FormUrlEncoded
    @POST("get_product.php")
    Call<ProductModel> getProducts(
            @Field("idcate") int idcate
    );

    @FormUrlEncoded
    @POST("productdetail.php")
    Call<ProductDetailModel> getProductsDetail(
            @Field("id") int id
    );

    // ===== USER =====
    @FormUrlEncoded
    @POST("register.php")
    Call<UserModel> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<UserModel> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    // ===== UPLOAD ẢNH =====
    @Multipart
    @POST("upload_image.php")
    Call<MessageModel> uploadImage(
            @Part MultipartBody.Part file
    );

    // ===== ĐĂNG TIN (INSERT VÀO products) =====
    @FormUrlEncoded
    @POST("insertsp2.php")
    Call<MessageModel> insertProduct(
            @Field("tensp") String tensp,
            @Field("hinhanh") String hinhanh,
            @Field("idcategory") int idcategory,
            @Field("iduser") int iduser
    );

    @FormUrlEncoded
    @POST("sync_user.php")
    Call<UserModel> syncUser(
            @Field("firebase_uid") String firebaseUid,
            @Field("email") String email,
            @Field("name") String name
    );
    @POST("get_my_posts.php")
    @FormUrlEncoded
    Call<MyPostModel> getMyPosts(
            @Field("iduser") int iduser
    );
    @FormUrlEncoded
    @POST("search_product.php")
    Call<ProductModel> searchProduct(
            @Field("keyword") String keyword
    );


}
