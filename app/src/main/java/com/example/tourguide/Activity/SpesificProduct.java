package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.example.tourguide.R;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.GenerateQR;
import com.example.tourguide.service.GetBitmapInterface;
import com.example.tourguide.service.GlideApp;
import com.example.tourguide.service.GlideRequests;
import com.example.tourguide.service.Screenshot;
import com.example.tourguide.service.SvgSoftwareLayerSetter;
import com.google.android.material.button.MaterialButton;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpesificProduct extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;
    Bitmap bitmapQr;
    String uriImage;
    private RequestBuilder<PictureDrawable> requestBuilder;
    private static final String IMAGE_DIRECTORY = "/tourGuide";
    GetBitmapInterface getBitmapInterfaceClick;
    private static final String TAG = "SpecificProduct";
    private String token;
    int path;
    int merchant_id;
    int value;
    private String urlPhoto,description,address,urlQR,endDate,maxCut;
    MaterialButton mSaveButton;
    Button mBackButton;
    Date date;
    String mMode;
    SimpleDateFormat formatter,output;
    private ImageView mImage,mQrCodeImage;
    Intent intent ;
    private TextView mEndDate,mDisc,mDescriptionText,mAddressText,mMaxCut;
    ImageView openMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesific_product);

        formatter= new SimpleDateFormat("yyyy-MM-dd");
        output = new SimpleDateFormat("MMMM, dd yyyy");
        mImage = findViewById(R.id.imSpesificProduck);
        mSaveButton = findViewById(R.id.saveButton);
        mBackButton = findViewById(R.id.backButton);
        mQrCodeImage = findViewById(R.id.qrCodeImage);
        mEndDate = findViewById(R.id.endDate);
        mDisc = findViewById(R.id.valueDisc);
        mDescriptionText = findViewById(R.id.descriptionText);
        mAddressText = findViewById(R.id.addressText);
        mMaxCut = findViewById(R.id.maxCut);
        openMerchant = findViewById(R.id.seeToko);



        intent = getIntent();
        merchant_id = intent.getExtras().getInt("merchantID",0);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpesificProduct.super.onBackPressed();
            }
        });
        openMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpesificProduct.this, Merchant.class);

                Bundle bundle = new Bundle();
                bundle.putInt("idMerchant" , merchant_id);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        mMode = intent.getExtras().getString("MODE",null);

        setAllTextview();





    }

    private void setAllTextview() {
        urlPhoto = intent.getStringExtra("StringURLImage");
        description = intent.getExtras().getString("StringDescription");
        address = intent.getStringExtra("StringAddress");
        maxCut = String.valueOf(intent.getIntExtra("Max_cut",0));

        endDate = intent.getStringExtra("StringEndDate");
            try {
                date = formatter.parse(endDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            path = intent.getIntExtra("pathItem",0);
            value = intent.getIntExtra("Value",0);
            Glide.with(this)
                    .asBitmap()
                    .load(urlPhoto)
                    .into(mImage);

            mSaveButton.setVisibility(View.GONE);

            requestBuilder = GlideApp.with(this)
                    .as(PictureDrawable.class)
                    .placeholder(R.drawable.chaced_foreground)
                    .listener(new SvgSoftwareLayerSetter());
            callAPI();




//        getBitmapInterfaceClick.setBitmap();

            mEndDate.setText("Untill " + output.format(date));
            mDisc.setText("" + value + " %");
            mAddressText.setText(address);
            mMaxCut.setText("Max Cutting : Rp." + maxCut);
            if(maxCut.equals("0")){
                mMaxCut.setVisibility(View.GONE);
            }
            mDescriptionText.setText(description);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void loadNet(Uri uri) {
        requestBuilder.load(uri).into(mQrCodeImage);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d(TAG, "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return "";
    }

    private void callAPI() {
        Call<GenerateQR> call = Api.getClient().generateQR("Bearer "+ token,path);
        call.enqueue(new Callback<GenerateQR>() {
            @Override
            public void onResponse(Call<GenerateQR> call, Response<GenerateQR> response) {
                if(response.isSuccessful()){

                    urlQR = response.body().getResult();
                    if(urlQR != null){
                        Log.d(TAG, "onResponse: " + urlQR);
                        Uri uri = Uri.parse(urlQR);
//                    loadNet(uri);
                        GlideApp.with(SpesificProduct.this)
                                .load(uri)
//                            .apply(RequestOptions.centerCropTransform())
                                .into(mQrCodeImage);

                        mSaveButton.setVisibility(View.VISIBLE);
                        mSaveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_DENIED){
                                    //permission not granted, request permission
                                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                                    //show pop up
                                    requestPermissions(permission,PERMISSION_CODE);
                                }
                                Log.d(TAG, "onClick: Clicked Button Save");
                                Bitmap save = Screenshot.takesScreenshotOfRootView(mQrCodeImage);
                                uriImage = saveImage(save);
                                Log.d(TAG, "onClick: bitmapQr " + uriImage);
                                Toast.makeText(SpesificProduct.this, "Qr Code Success have been saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        mQrCodeImage.setImageDrawable(getResources().getDrawable(R.drawable.qr2));
                    }
                }
            }

            @Override
            public void onFailure(Call<GenerateQR> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
