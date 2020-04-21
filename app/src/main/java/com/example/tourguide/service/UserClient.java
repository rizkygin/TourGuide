package com.example.tourguide.service;

import com.example.tourguide.model.MerchantIndex;
import com.example.tourguide.model.RegisterdUser;
import com.example.tourguide.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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

}
