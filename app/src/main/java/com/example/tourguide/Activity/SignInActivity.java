package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourguide.R;
import com.example.tourguide.model.Login;
import com.example.tourguide.model.User;
import com.example.tourguide.service.UserClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Handler;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private TextView test;
    private MaterialButton login;
    private TextInputLayout email,password;


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

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    UserClient userClient = retrofit.create(UserClient.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        test =  findViewById(R.id.signintext);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        email = (TextInputLayout) findViewById(R.id.emailajanihcuy);
        password = findViewById(R.id.password);




        login = findViewById(R.id.btnloginaplikasi);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                validate();
                login();
            }
        });
    }

    private void validate() {

        emailtype = email.getEditText().getText().toString().trim();
        passwordtype = password.getEditText().getText().toString();

        
        if(emailtype.isEmpty()){
            email.setError("Fill The Field");
        }
        if(passwordtype.isEmpty()){
            password.setError("Fill The Field");
        }
    }


    private void login() {


        Login login = new Login(emailtype,passwordtype);
        Call<User> call =  userClient.login(emailtype,passwordtype);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SignInActivity.this,"Succes" , Toast.LENGTH_LONG).show();

                    if(response.body() != null){
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        token = response.body().getToken();
                        editor.putString("token",token);
                        editor.putString("email",response.body().getUser().getEmail());
                        editor.putString("name",response.body().getUser().getName());
                        editor.putString("points",response.body().getUser().getPoints());
                        editor.putString("role",response.body().getRole());

                        editor.commit();


                        progressBar.setVisibility(View.GONE);

                        Intent i = new Intent(SignInActivity.this, LandingMainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(SignInActivity.this,"No Data Found",Toast.LENGTH_LONG);
                    }

                }
                else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(SignInActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(SignInActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SignInActivity.this, "unknown error " + response.code(), Toast.LENGTH_SHORT).show();
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
                if (t instanceof SocketTimeoutException)
                {
                    Toast.makeText(SignInActivity.this,"Connection time out  " + Message,Toast.LENGTH_LONG ).show();
                }
                else if (t instanceof IOException)
                {
                    Toast.makeText(SignInActivity.this,"Time Out  " + Message,Toast.LENGTH_LONG ).show();
                }
                else
                {
                    //Call was cancelled by user
                    if(call.isCanceled())
                    {
                        System.out.println("Call was cancelled forcefully");
                    }
                    else
                    {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }

        });
    }



}
