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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView mNameStore,mDesc,mClose,mOpen,mAddress,mDiscountText,iWantTutorial;
    ImageView mPic,mImgnoDiscount,mTutorial;
    List<ItemOnMerchant> mList = new ArrayList<>();
    String urlPhoto;
    int userMerchantId;
    private String TAG  = "Merchant.Activity";
    private String token;
    Button editMerchant;
    Bundle bundle = new Bundle();
    Button mLandingBack;
    MaterialButton mTutorButton;
    SharedPreferences sharedPreferencesTu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        mNameStore = findViewById(R.id.nameMerchant);
        mLandingBack = findViewById(R.id.mBackButton);
        mDesc = findViewById(R.id.tvDescRelative);
        mDiscountText = findViewById(R.id.tvLabel);
//        mClose = findViewById(R.id.tvClosedRelative);
//        mOpen = findViewById(R.id.tvOpenAtRelative);
        mAddress = findViewById(R.id.tvAddressRelative);
        mPic = findViewById(R.id.imMerchant);
        mImgnoDiscount = findViewById(R.id.noDiscountImg);
        fab = findViewById(R.id.fabMerchant);
        editMerchant = findViewById(R.id.openMerchantEdit);
        mTutorial = findViewById(R.id.tutorial);
        mTutorButton = findViewById(R.id.tutorButton);
        iWantTutorial = findViewById(R.id.iWantSeeMyTutorial);


        sharedPreferencesTu = getSharedPreferences("Tutor",Context.MODE_PRIVATE);
        mTutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferencesTu.edit();
                editor.putBoolean("needTutor",false);
                editor.commit();
                mTutorButton.setVisibility(View.GONE);
                mTutorial.setVisibility(View.GONE);
            }
        });
        iWantTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTutorial.setVisibility(View.VISIBLE);
                mTutorButton.setVisibility(View.VISIBLE);
            }
        });
        editMerchant.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.merchantItems);
        Intent intent = getIntent();
        mTutorial.setVisibility(View.GONE);
        mTutorButton.setVisibility(View.GONE);
        mLandingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Merchant.this,LandingMainActivity.class));
            }
        });
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
            editMerchant.setVisibility(View.VISIBLE);
            iWantTutorial.setVisibility(View.VISIBLE);

            if(sharedPreferencesTu.getBoolean("needTutor",true)){
                mTutorial.setVisibility(View.VISIBLE);
                mTutorButton.setVisibility(View.VISIBLE);

            }

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
                Log.d(TAG, "onResponse: in kenapa 0 terus ya" + response.body().getData().size());
                mNameStore.setText(response.body().getData().get(0).getName());
                bundle.putString("name",response.body().getData().get(0).getName());
                mDesc.setText(response.body().getData().get(0).getDescription());
                bundle.putString("description",response.body().getData().get(0).getDescription());

                mAddress.setText(response.body().getData().get(0).getAddress());
                bundle.putString("address",response.body().getData().get(0).getAddress());
                bundle.putString("image",response.body().getData().get(0).getPhoto());

//                mClose.setText("- 22.00" );
////                mOpen.setText("10.00 " );
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
                    mDiscountText.setText("This Merchant is empty");
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Merchant.this));
                if(userMerchantId == path){
                    Log.d(TAG, "onResponse: userMerchant Path " + userMerchantId + " : path " + path);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
                    itemTouchHelper.attachToRecyclerView(recyclerView);

                    editMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent edit = new Intent(Merchant.this,EditAccountActivity.class);
                            edit.putExtras(bundle);
                            startActivity(edit);
                        }
                    });
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
