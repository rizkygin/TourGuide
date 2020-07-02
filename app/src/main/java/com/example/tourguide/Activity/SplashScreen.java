package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.tourguide.MapsActivity;
import com.example.tourguide.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME = 3000; //This is 1 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);

                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
    }
}
