package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourguide.Adapter.ItemMerchant;
import com.example.tourguide.Adapter.RecyclerViewRecommendedAdapter;
import com.example.tourguide.R;
import com.example.tourguide.model.ItemOnMerchant;
import com.example.tourguide.model.MerchantShow;
import com.example.tourguide.model.Promo;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.SwipeToDeleteCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Merchant extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    int path;
    TextView mNameStore,mDesc,mClose,mOpen,mAddress,mDiscountText;
    ImageView mPic,mImgnoDiscount;
    List<ItemOnMerchant> mList = new ArrayList<>();
    String urlPhoto;
    int userMerchantId;
    private String TAG  = "Merchant.Activity";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        mNameStore = findViewById(R.id.nameMerchant);
        mDesc = findViewById(R.id.tvDescRelative);
        mDiscountText = findViewById(R.id.tvLabel);
        mClose = findViewById(R.id.tvClosedRelative);
        mOpen = findViewById(R.id.tvOpenAtRelative);
        mAddress = findViewById(R.id.tvAddressRelative);
        mPic = findViewById(R.id.imMerchant);
        mImgnoDiscount = findViewById(R.id.noDiscountImg);
        fab = findViewById(R.id.fabMerchant);

        recyclerView = findViewById(R.id.merchantItems);
        Intent intent = getIntent();

        path = intent.getIntExtra("idMerchant",0);
        if(path == 0){
            Intent i  = new Intent(Merchant.this,LandingMainActivity.class);
            startActivity(i);
        }
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        userMerchantId = preferences.getInt("merchant_id", 0);
        callAPI();
        Log.d(TAG, "path :" + path);
        Log.d(TAG, "merchant_id: " + userMerchantId);

        if(userMerchantId == path){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Mode","Add");
                    bundle.putInt("Merhant_id",path);
                    Intent intent = new Intent(Merchant.this,AddDiscount.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }else{
            fab.setVisibility(View.GONE);
        }


    }

    private void callAPI() {
        Call<MerchantShow> merchantShowCall = Api.getClient().merchantShow(path,"Bearer "+token);
        merchantShowCall.enqueue(new Callback<MerchantShow>() {
            @Override
            public void onResponse(Call<MerchantShow> call, Response<MerchantShow> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code :" + response.code());
                    return;
                }
                mNameStore.setText(response.body().getData().get(0).getName());
                Log.d(TAG, "onResponse: " +  response.body().getData().get(0).getName());
                mDesc.setText("" + response.body().getData().get(0).getDescription());
                mAddress.setText("" + response.body().getData().get(0).getAddress());
                mClose.setText("- 22.00" );
                mOpen.setText("10.00 " );
                List<ItemOnMerchant> items = response.body().getData().get(0).getItem();


                for(ItemOnMerchant item : items){
                    List<Promo> promos = item.getPromo();
                    mList.add(new ItemOnMerchant(item.getId(),item.getMerchant_id(),item.getName(),item.getDescription(),item.getPhoto(),item.getPrice(),promos));
                }
                ItemMerchant adapter = new ItemMerchant(Merchant.this, mList,path,response.body().getData().get(0).getAddress());

                mImgnoDiscount.setVisibility(View.GONE);
                if(mList.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    mImgnoDiscount.setVisibility(View.VISIBLE);
                    mDiscountText.setText("No Discount Today");
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Merchant.this));
                if(userMerchantId == path){
                    Log.d(TAG, "onResponse: userMerchant Path " + userMerchantId + " : path " + path);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }



                urlPhoto = response.body().getData().get(0).getPhoto();

                Glide.with(Merchant.this)
                        .load(urlPhoto)
                        .into(mPic);

            }

            @Override
            public void onFailure(Call<MerchantShow> call, Throwable t) {
                String Message = t.getMessage();
                Log.d(TAG + "f", Message);
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(Merchant.this, "Connection time out  " + Message, Toast.LENGTH_LONG).show();

                } else if (t instanceof IOException) {
                    Toast.makeText(Merchant.this, "Time Out  " + Message, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Message :" + Message);

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
}
