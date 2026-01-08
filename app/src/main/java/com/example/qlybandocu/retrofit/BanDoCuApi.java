package com.example.qlybandocu.retrofit;


import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.models.MessageModel;
import com.example.qlybandocu.models.MyPostModel;
import com.example.qlybandocu.models.OrderModel;
import com.example.qlybandocu.models.RatingModel;
import com.example.qlybandocu.viewModel.OrderDetailModel;
import com.example.qlybandocu.viewModel.ProductDetailModel;
import com.example.qlybandocu.models.ProductModel;
import com.example.qlybandocu.models.UserModel;


import okhttp3.MultipartBody;
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
    @FormUrlEncoded
    @POST("create_order.php")
    Call<MessageModel> createOrder(
            @Field("iduser") int iduser,
            @Field("address") String address,
            @Field("phonenumber") String phone,
            @Field("total") double total,
            @Field("quantity") int quantity,
            @Field("items") String itemsJson,
            @Field("payment_method") String paymentMethod
    );

    @FormUrlEncoded
    @POST("update_payment.php")
    Call<MessageModel> updatePayment(
            @Field("idorder") int idorder,
            @Field("payment_method") String paymentMethod
    );
    @FormUrlEncoded
    @POST("get_orders.php")
    Call<OrderModel> getOrders(
            @Field("iduser") int iduser
    );
    @FormUrlEncoded
    @POST("get_order_detail.php")
    Call<OrderDetailModel> getOrderDetail(
            @Field("idorder") int idorder
    );

    @FormUrlEncoded
    @POST("update_order_status.php")
    Call<MessageModel> updateOrderStatus(
            @Field("idorder") int idorder,
            @Field("status") int status
    );
    @POST("get_all_orders.php")
    Call<OrderModel> getAllOrders();

    @FormUrlEncoded
    @POST("add_review.php")
    Call<MessageModel> addReview(
            @Field("iduser") int iduser,
            @Field("idproduct") int idproduct,
            @Field("idorder") int idorder,
            @Field("rating") int rating,
            @Field("comment") String comment
    );
    @FormUrlEncoded
    @POST("get_avg_rating.php")
    Call<RatingModel> getAvgRating(
            @Field("idproduct") int idproduct
    );

}
