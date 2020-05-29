package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.tourguide.R;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.GenerateQR;
import com.google.android.material.button.MaterialButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpesificProduct extends AppCompatActivity {

    private String token;
    int path;
    int merchant_id;
    int value;
    private String urlPhoto,description,address,urlQR,endDate;
    MaterialButton mSaveButton;
    Button mBackButton;
    private ImageView mImage,mQrCodeImage;
    private TextView mEndDate,mDisc,mDescriptionText,mAddressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesific_product);

        mImage = findViewById(R.id.imSpesificProduck);
        mSaveButton = findViewById(R.id.saveButton);
        mBackButton = findViewById(R.id.backButton);
        mQrCodeImage = findViewById(R.id.qrCodeImage);
        mEndDate = findViewById(R.id.endDate);
        mDisc = findViewById(R.id.valueDisc);
        mDescriptionText = findViewById(R.id.descriptionText);
        mAddressText = findViewById(R.id.addressText);

        Intent intent = getIntent();

        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        urlPhoto = intent.getStringExtra("StringURLImage");
        description = intent.getStringExtra("StringDescription");
        address = intent.getStringExtra("StringAddress");
        endDate = intent.getStringExtra("StringEndDate");
        path = intent.getIntExtra("pathItem",0);
        value = intent.getIntExtra("Value",0);
        merchant_id = intent.getIntExtra("merchantID",0);
        Glide.with(this)
                .load(urlPhoto)
                .into(mImage);

        callAPI();

        mEndDate.setText("Untill " + endDate);
        mDisc.setText("" + value + " %");
        mAddressText.setText(address);
        mDescriptionText.setText(description);


    }

    private void callAPI() {
        Call<GenerateQR> call = Api.getClient().generateQR("Bearer "+ token,path);
        call.enqueue(new Callback<GenerateQR>() {
            @Override
            public void onResponse(Call<GenerateQR> call, Response<GenerateQR> response) {
                if(response.isSuccessful()){
                    urlQR = response.body().getResult();
                    Glide.with(SpesificProduct.this)
                            .load(urlQR)
                            .into(mQrCodeImage);
                }
            }

            @Override
            public void onFailure(Call<GenerateQR> call, Throwable t) {

            }
        });
    }
}
