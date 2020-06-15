package com.example.tourguide.service;

import com.example.tourguide.JsonPlaceHolderApi;
import com.example.tourguide.model.MerchantIndex;
import com.example.tourguide.model.MerchantItemStore;
import com.example.tourguide.model.MerchantItemUpdate;
import com.example.tourguide.model.MerchantShow;
import com.example.tourguide.model.PromoSelf;
import com.example.tourguide.model.RegisterdUser;
import com.example.tourguide.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserClient {

//    @POST("login")
//    Call<User> login(@Body Login login);

    @FormUrlEncoded
    @POST("login")
    Call<User> login(@Field("email") String email, @Field("password") String Password);



    @FormUrlEncoded
    @POST("register")
    Call<RegisterdUser> registration(@Field("name") String name,
                                     @Field("email") String email,
                                     @Field("password") String password,
                                     @Field("password_confirmation") String password_confirmation);


    @GET("merchant")
    Call<MerchantIndex> recomendedplace(@Header("Authorization")String token);

    @GET("merchant/{id}")
    Call<MerchantShow> merchantShow(@Path("id") int id,@Header("Authorization")String token);

    @Multipart
    @POST("items")
    Call<MerchantItemStore> uploadItem(@Header("Authorization") String token,
                                       @Part MultipartBody.Part photo ,
                                       @Part("merchant_id") RequestBody merchant_id,
                                       @Part("name") RequestBody name,
                                       @Part("description") RequestBody description,
                                       @Part("price") RequestBody price);

    @Multipart
    @POST("items/{iditem}")
    Call<MerchantItemUpdate> updateItem(@Header("Authorization") String token,
                                        @Part MultipartBody.Part photo ,
                                        @Part("merchant_id") RequestBody merchant_id,
                                        @Part("name") RequestBody name,
                                        @Part("description") RequestBody description,
                                        @Part("price") RequestBody price,
                                        @Path("iditem") int iditem,
                                        @Part("_method") RequestBody method);
    @FormUrlEncoded
    @POST("promo")
    Call<JsonResponse> storePromoApi(@Header("Authorization") String token,
                                 @Field("item_id") int item_id,
                                 @Field("value") int value,
                                 @Field("description")String description,
                                 @Field("category")String category,
                                 @Field("max_cut") String max_cut,
                                 @Field("start_time")String start_time,
                                 @Field("end_time")String end_time);
    @DELETE("items/{id}")
    Call<JsonResponse> deleteItemMerhcant(@Header("Authorization") String token,
                                          @Path("id") int id);

    @GET("promo")
    Call<PromoSelf> getAllPromo(@Header("Authorization")String token);

    @GET("qr/generate/{id}")
    Call<GenerateQR> generateQR(@Header("Authorization")String token,
                                @Path("id") int id);

    @GET("/qr/scan/{token}")
    Call<JsonResponse> scanQrCode(@Header("Authorization")String tokenLogin,
                                  @Path("token") String token);
}
