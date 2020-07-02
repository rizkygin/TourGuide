package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tourguide.R;
import com.example.tourguide.model.Login;
import com.example.tourguide.model.User;
import com.example.tourguide.service.UserClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextView login;
    MaterialButton regis;

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.btnlogin);
        regis = findViewById(R.id.btnregis);

        SharedPreferences preferences = LoginActivity.this.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        if(token != null){
            Intent intent = new Intent(LoginActivity.this, LandingMainActivity.class);
            intent.putExtra("SearchedCity","Search Here");
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

}
