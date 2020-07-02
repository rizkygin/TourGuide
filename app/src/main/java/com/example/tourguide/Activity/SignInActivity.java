package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourguide.R;
import com.example.tourguide.model.Login;
import com.example.tourguide.model.User;
import com.example.tourguide.service.UserClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private TextView test;
    private MaterialButton login;
    private TextInputLayout email, password;


    static final String TAG = "SignInActivity";

    int merchant_id;
    Intent i;
    SharedPreferences sharedPreferences;
    private String emailtype;
    private String passwordtype;
    private String token;
    private ProgressBar progressBar;

//    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//    logging.setLevel(Level.BASIC);
//    OkHttpClient client = new OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build();
//OkHttpClient okHttpClient = new OkHttpClient.Builder()
//        .addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                okhttp3.Response response = chain.proceed(request);
//
//                // todo deal with the issues the way you need to
////                    if (response.code() == 500) {
////                        startActivity(
////                                new Intent(
////                                        ErrorHandlingActivity.this,
////                                        ServerIsBrokenActivity.class
////                                )
////                        );
//
//                return response;
//
//            }
//        })
//        .build();
    OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://tourgr.id/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        test = findViewById(R.id.signintext);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        email = (TextInputLayout) findViewById(R.id.emailajanihcuy);
        password = findViewById(R.id.password);


        login = findViewById(R.id.btnloginaplikasi);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(validate()){
                    login();
                }
            }
        });
    }

    private boolean validate() {
        emailtype = email.getEditText().getText().toString().trim();
        passwordtype = password.getEditText().getText().toString();


        if (emailtype.isEmpty()) {
            email.setError("Fill The Field");
            progressBar.setVisibility(View.GONE);
            return false;

        }
        if (passwordtype.isEmpty()) {
            password.setError("Fill The Field");
            progressBar.setVisibility(View.GONE);

            return false;
        }
        return true;
    }


    private void login() {


        final Login login = new Login(emailtype, passwordtype);
        Call<User> call = userClient.login(emailtype, passwordtype);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        token = response.body().getToken();
                        editor.putString("token", token);
                        editor.putString("points", response.body().getUser().getPoints());
                        editor.putString("email", response.body().getUser().getEmail());
//                        editor.putInt("merchant_id", 0);
                        editor.putString("name", response.body().getUser().getName());
                        editor.putString("role", response.body().getRole());
                        editor.putString("SearchedCity", null);
                        if (response.body().getUser().getMerchant_id() != null) {
                            editor.putInt("merchant_id", response.body().getUser().getMerchant_id());
                            merchant_id = response.body().getUser().getMerchant_id();
                            SharedPreferences sharedPreferences = getSharedPreferences("Tutor", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = sharedPreferences.edit();
                            editors.putBoolean("needTutor", true);
                            editors.commit();
                        }

                        editor.commit();


                        progressBar.setVisibility(View.GONE);

                        i = new Intent(SignInActivity.this, LandingMainActivity.class);
                        if (response.body().getUser().getMerchant_id() != null) {
                            Toast.makeText(SignInActivity.this, "Success Login As Merchant", Toast.LENGTH_LONG).show();
                            startActivity(i);

                        } else {
                            Toast.makeText(SignInActivity.this, "Success Login As Guide", Toast.LENGTH_LONG).show();
                            startActivity(i);
                        }

                    } else {
                        Toast.makeText(SignInActivity.this, "No Data Found", Toast.LENGTH_LONG);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(SignInActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(SignInActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SignInActivity.this, "Either one of email password doesn't match", Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
//                token = response.body().getToken();
//                SharedPreferences sharedPreferences = getSharedPreferences("TokenLogin", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                editor.putString("token",token);
//
//                editor.apply();


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String Message = t.getMessage();
//                Toast.makeText(SignInActivity.this,"Sorry Try Again" + emailtype + "/ " + "pass " + Message,Toast.LENGTH_LONG ).show();
////                Log.d("response", t.getStackTrace().toString());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SignInActivity.this, "Connection time out  " + Message, Toast.LENGTH_LONG).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SignInActivity.this, "Time Out  " + Message, Toast.LENGTH_LONG).show();
                } else {
                    //Call was cancelled by user
                    if (call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }

        });
    }

//    private void callApiimage() {
//            Call<MerchantShow> call = Api.getClient().merchantShow(merchant_id,"Bearer " + token);
//            call.enqueue(new Callback<MerchantShow>() {
//                @Override
//                public void onResponse(Call<MerchantShow> call, Response<MerchantShow> response) {
//                    if (!response.isSuccessful()) {
//                        Log.d(TAG, "Code :" + response.code());
//                        return;
//                    }
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("imageMerchant","");
//                    editor.commit();
//                    Toast.makeText(SignInActivity.this,"Success Login As Merchant" , Toast.LENGTH_LONG).show();
//
//                    startActivity(i);
//                }
//
//                @Override
//                public void onFailure(Call<MerchantShow> call, Throwable t) {
//
//                }
//            });
//    }


}
