package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.PromoSelfAdapter;
import com.example.tourguide.R;
import com.example.tourguide.model.PromoGetSelf;
import com.example.tourguide.model.PromoSelfCheck;
import com.example.tourguide.service.Api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.strictmode.Violation;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckPromoMerchant extends AppCompatActivity {

    Button back;
    List<PromoSelfCheck> promos = new ArrayList<>();
    List<PromoSelfCheck> mList  = new ArrayList<>();
    RecyclerView recyclerView;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_promo_merchant);


        back = findViewById(R.id.backButtonLanding);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckPromoMerchant.this,LandingMainActivity.class));
            }
        });
        token = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("token","");

        recyclerView = findViewById(R.id.RecycleListPromoSelf);
        getPromo();
    }

    private void getPromo() {
        Call<PromoGetSelf> call = Api.getClient().promoCheck("Bearer "+ token);
        call.enqueue(new Callback<PromoGetSelf>() {
            @Override
            public void onResponse(Call<PromoGetSelf> call, Response<PromoGetSelf> response) {
                if(response.isSuccessful()){
                    promos = response.body().getData();
                    for (PromoSelfCheck promo : promos){
                        mList.add(new PromoSelfCheck(promo.getId(),promo.getItem_id(),promo.getValue(),promo.getMax_cut(),promo.getDescription(),promo.getStart_time(),promo.getEnd_time()));
                    }
                    recyclerView.setHasFixedSize(true);
                    PromoSelfAdapter promoSelfAdapter = new PromoSelfAdapter(mList,CheckPromoMerchant.this,token);
                    recyclerView.setLayoutManager( new LinearLayoutManager(CheckPromoMerchant.this));
                    recyclerView.setAdapter(promoSelfAdapter);

                }
            }

            @Override
            public void onFailure(Call<PromoGetSelf> call, Throwable t) {

            }
        });

    }
}
