package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.RecyclerViewVoucherAdapter;
import com.example.tourguide.Adapter.Voucher;
import com.example.tourguide.R;
import com.example.tourguide.model.PromoSelf;
import com.example.tourguide.service.Api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemActivity extends AppCompatActivity {

    private static final String TAG = "RedeemActivity";
    TextView points;
    String point;
    String token;
    List<Voucher> mList;
    List<Voucher> vouchers = new ArrayList<>();
    RecyclerView recyclerView;
    Button back;

    @Override
    public void onBackPressed() {
        //nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);


        back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RedeemActivity.this,LandingMainActivity.class));
            }
        });
        points = findViewById(R.id.hisPoint);
        recyclerView = findViewById(R.id.RecycleList);


        mList= new ArrayList<>();


        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        point = preferences.getString("points", null);

        getAllPromo();
        if(point == null){
            points.setText("0");

        }else{
            points.setText(" " + point);

        }




    }

    private void getAllPromo() {
        Call<PromoSelf> getAllPromo = Api.getClient().getAllPromo("Bearer " + token);
        getAllPromo.enqueue(new Callback<PromoSelf>() {
            @Override
            public void onResponse(Call<PromoSelf> call, Response<PromoSelf> response) {
                if(response.isSuccessful()){
                    vouchers = response.body().getData();
                    for(Voucher voucher : vouchers){
                        Log.d(TAG, "onResponse: "+ voucher.getId());
                        Log.d(TAG, "onResponse: "+ voucher.getPoint());
                        mList.add(new Voucher(voucher.getDescription(),voucher.getValue(),true,voucher.getEnd_time(),voucher.getPoint(),voucher.getId(),voucher.getMerchant_id()));
//                        Log.d(TAG, "onResponse: " + " " + voucher.getValue());
                    }
                    recyclerView.setHasFixedSize(true);
                    RecyclerViewVoucherAdapter recyclerViewVoucherAdapter = new RecyclerViewVoucherAdapter(RedeemActivity.this,mList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RedeemActivity.this));
                    recyclerView.setAdapter(recyclerViewVoucherAdapter);
                    Log.d("redeemActivity", String.valueOf(mList.size()));

                }
                Log.d(TAG, "onResponse: " + response.message());

            }

            @Override
            public void onFailure(Call<PromoSelf> call, Throwable t) {

            }
        });
    }
}
