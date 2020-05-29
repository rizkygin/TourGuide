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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);



        points = findViewById(R.id.hisPoint);
        recyclerView = findViewById(R.id.RecycleListVoucher);


        mList= new ArrayList<>();
//        mList.add(new Voucher("Auto Leader",15000,true));
//        mList.add(new Voucher("Rumah Makan Kita",35000,true));
//        mList.add(new Voucher("Warung Santai ",23000,true));
//        mList.add(new Voucher("Pak Tarno",25000,true));
//        mList.add(new Voucher("Swadihap",15000,true));
//        mList.add(new Voucher("Auto Leader 2",10000,true));
//        mList.add(new Voucher("Bantal Ban ",15000,true));
//        mList.add(new Voucher("Tidur Nyenyak Hotel",15000,true));
//        mList.add(new Voucher("Best Cover Hotel",15000,true));
//        mList.add(new Voucher("Safety Hotel",25000,true));
//        mList.add(new Voucher("Kikuk",35000,true));


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
                        mList.add(new Voucher(voucher.getDescription(),voucher.getValue(),true,voucher.getEnd_time()));
                        Log.d(TAG, "onResponse: " + " " + voucher.getValue());
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
