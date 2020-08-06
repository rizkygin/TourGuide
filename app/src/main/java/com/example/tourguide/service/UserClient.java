package com.example.tourguide.service;

import com.example.tourguide.JsonPlaceHolderApi;
import com.example.tourguide.model.GalleryResponse;
import com.example.tourguide.model.MerchantIndex;
import com.example.tourguide.model.MerchantItemStore;
import com.example.tourguide.model.MerchantItemUpdate;
import com.example.tourguide.model.MerchantShow;
import com.example.tourguide.model.PromoGetSelf;
import com.example.tourguide.model.PromoSelf;
import com.example.tourguide.model.RedeemResponses;
import com.example.tourguide.model.RegisterdUser;
import com.example.tourguide.model.ScanResponses;
import com.example.tourguide.model.TourismIndex;
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
                                     @Field("phone") String phone,
                                     @Field("password_confirmation") String password_confirmation);


    @GET("merchant")
    Call<MerchantIndex> recomendedplace(@Header("Authorization")String token);

    @GET("merchant/{id}")
    Call<MerchantShow> merchantShow(@Path("id") int id,
                                    @Header("Authorization")String token);

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
    @Multipart
    @POST("items/{iditem}")
    Call<MerchantItemUpdate> updateItemNoPic(@Header("Authorization") String token,
                                        @Part("merchant_id") RequestBody merchant_id,
                                        @Part("name") RequestBody name,
                                        @Part("description") RequestBody description,
                                        @Part("price") RequestBody price,
                                        @Path("iditem") int iditem,
                                        @Part("_method") RequestBody method);
    @FormUrlEncoded
    @POST("promo")
    Call<JsonResponse> storePromoApiM(@Header("Authorization") String token,
                                 @Field("item_id") int item_id,
                                 @Field("value") int value,
                                 @Field("description")String description,
                                 @Field("category")String category,
                                 @Field("max_cut") String max_cut,
                                 @Field("start_time")String start_time,
                                 @Field("end_time")String end_time);
    @FormUrlEncoded
    @POST("promo")
    Call<JsonResponse> storePromoApi(@Header("Authorization") String token,
                                     @Field("item_id") int item_id,
                                     @Field("value") int value,
                                     @Field("description")String description,
                                     @Field("category")String category,
                                     @Field("start_time")String start_time,
                                     @Field("end_time")String end_time);
    @DELETE("items/{id}")
    Call<JsonResponse> deleteItemMerhcant(@Header("Authorization") String token,
                                          @Path("id") int id);

    @GET("promo")
    Call<PromoSelf> getAllPromo(@Header("Authorization")String token);

    @GET("reward")
    Call<RewardShowAll> getReward(@Header("Authorization")String token);

    @GET("qr/generate/{id}")
    Call<GenerateQR> generateQR(@Header("Authorization")String token,
                                @Path("id") int id);

    @GET("qr/scan/{token}")
    Call<ScanResponses> scanQrCode(@Header("Authorization")String tokenLogin,
                                   @Path("token") String token);

    @GET("getpromo")
    Call<PromoGetSelf> promoCheck(@Header("Authorization")String token);

    @FormUrlEncoded
    @POST("promo/{id}")
    Call<JsonResponse> promoUpdate(@Header("Authorization")String token,
                                   @Field("value") String value,
                                   @Field("description") String description,
                                   @Field("category") String category,
                                   @Field("max_cut") String max_cut,
                                   @Field("start_time") String start_time,
                                   @Field("end_time") String end_time,
                                   @Path("id") String id,
                                   @Field("_method") String _method);
    @DELETE("promo/{id}")
    Call<JsonResponse> destroyPromo(@Header("Authorization")String token,
                                    @Path("id") int id);
    @Multipart
    @POST("merchant/update")
    Call<JsonResponse> updateMerchant(@Header("Authorization") String token,
                                      @Part("address")RequestBody address,
                                      @Part("name")RequestBody name,
                                      @Part("latitude")RequestBody latitude,
                                      @Part("longitude")RequestBody longitude,
                                      @Part("description")RequestBody description,
                                      @Part MultipartBody.Part photo,
                                      @Part("_method") RequestBody method);
    @Multipart
    @POST("merchant/update")
    Call<JsonResponse> updateMerchant(@Header("Authorization") String token,
                                      @Part("address")RequestBody address,
                                      @Part("name")RequestBody name,
                                      @Part("latitude")RequestBody latitude,
                                      @Part("longitude")RequestBody longitude,
                                      @Part("description")RequestBody description,
                                      @Part("_method") RequestBody method);
    @GET("tourism")
    Call<TourismIndex> tourismIndex(@Header("Authorization") String token);

    @GET("tourism-city/{id}")
    Call<TourismIndex> tourismIndexCity(@Header("Authorization") String token,
                                       @Path("id") int id);

    @GET("tourism-gallery/{id}")
    Call<GalleryResponse> tourismIndexing(@Header("Authorization") String token,
                                          @Path("id") int id);

    @GET("reward-redeem/{id}")
    Call<RedeemResponses> redeem(@Header("Authorization")String token,
                                 @Path("id") int id);
}
