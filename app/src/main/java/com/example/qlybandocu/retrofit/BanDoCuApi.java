package com.example.qlybandocu.retrofit;


import com.example.qlybandocu.models.CategoryModel;
import com.example.qlybandocu.models.MessageModel;
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

    // Lấy danh mục
    @GET("category.php")
    Call<CategoryModel> getCategory();

    @POST("get_product.php")
    @FormUrlEncoded
    Call<ProductModel> getProducts(
            @Field("idcate") int idcate
    );
    @POST("productdetail.php")
    @FormUrlEncoded
    Call<ProductDetailModel> getProductsDetail(
            @Field("id") int id
    );

    // Đăng ký người dùng
    @FormUrlEncoded
    @POST("register.php")
    Call<UserModel> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );

    // Đăng nhập người dùng
    @FormUrlEncoded
    @POST("login.php")
    Call<UserModel> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @Multipart
    @POST("insertsp.php")
    Observable<MessageModel> insertSp(
                                       @Part("tensp") RequestBody tensp,
                                       @Part("gia") RequestBody gia,
                                       @Part("mota") RequestBody mota,
                                       @Part("loai") RequestBody loai,
                                       @Part("iduser") RequestBody iduser,
                                       @Part MultipartBody.Part hinhanh
    );
}
