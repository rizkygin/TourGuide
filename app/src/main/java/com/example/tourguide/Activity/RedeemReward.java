package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.RecyclerViewVoucherAdapter;
import com.example.tourguide.Adapter.RewardAdapter;
import com.example.tourguide.Adapter.Voucher;
import com.example.tourguide.R;
import com.example.tourguide.model.PromoSelf;
import com.example.tourguide.model.Reward;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.RewardShowAll;

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

public class RedeemReward extends AppCompatActivity {

    String token;
    List<Reward> mList;
    List<Reward> rewards = new ArrayList<>();

    String point;
    RecyclerView recyclerView;
    TextView points;

    Button back;


    @Override
    public void onBackPressed() {
        //nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_reward);

        back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RedeemReward.this,LandingMainActivity.class));
            }
        });
        points = findViewById(R.id.hisPointReward);
        recyclerView = findViewById(R.id.RecycleListReward);

        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        point = preferences.getString("points", null);

        showReward();
        if(point == null){

            points.setText("0");

        }else{
            points.setText(" " + point);

        }
    }

    private void showReward() {
        Call<RewardShowAll> showAllReward = Api.getClient().getReward("Bearer " + token);
        showAllReward.enqueue(new Callback<RewardShowAll>() {
            @Override
            public void onResponse(Call<RewardShowAll> call, Response<RewardShowAll> response) {
                if(response.isSuccessful()){
                    mList= new ArrayList<>();

                    rewards = response.body().getData();
                    for (Reward reward : rewards){
                        mList.add(new Reward(reward.getId(),reward.getPoint(),reward.getName(),reward.getPhoto(),reward.getDescription(),reward.getCreated_at(),reward.getUpdated_at()));

                    }
                    recyclerView.setHasFixedSize(true);
                    RewardAdapter rewardAdapter = new RewardAdapter(RedeemReward.this,mList,point,token);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RedeemReward.this));
                    recyclerView.setAdapter(rewardAdapter);


                }
            }

            @Override
            public void onFailure(Call<RewardShowAll> call, Throwable t) {

            }
        });
    }
}
