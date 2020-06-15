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
import com.example.tourguide.service.Screenshot;
import com.google.android.material.button.MaterialButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpesificProduct extends AppCompatActivity {

    Bitmap bitmapQr;
    String uriImage;
    private RequestBuilder<PictureDrawable> requestBuilder;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    GetBitmapInterface getBitmapInterfaceClick;
    private static final String TAG = "SpecificProduct";
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
        Log.d(TAG, "onClick: StringURLImage 2"+ urlPhoto );
        description = intent.getStringExtra("StringDescription");
        address = intent.getStringExtra("StringAddress");
        endDate = intent.getStringExtra("StringEndDate");
        path = intent.getIntExtra("pathItem",0);
        value = intent.getIntExtra("Value",0);
        merchant_id = intent.getIntExtra("merchantID",0);
        Glide.with(this)
                .asBitmap()
                .load(urlPhoto)
                .into(mImage);

        mSaveButton.setVisibility(View.GONE);
        callAPI();


//        getBitmapInterfaceClick.setBitmap();

        mEndDate.setText("Untill " + endDate);
        mDisc.setText("" + value + " %");
        mAddressText.setText(address);
        mDescriptionText.setText(description);



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
                    Uri uri = Uri.parse(urlQR);
                    Log.d(TAG, "onResponse: " + urlQR);


                    GlideApp.with(SpesificProduct.this)
                            .load(uri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(mQrCodeImage);

                    mSaveButton.setVisibility(View.VISIBLE);
                    mSaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: Clicked Button Save");

                            Bitmap save = Screenshot.takesScreenshotOfRootView(mQrCodeImage);

                            uriImage = saveImage(save);
                            Log.d(TAG, "onClick: bitmapQr " + bitmapQr);

                            Toast.makeText(SpesificProduct.this, "Qr Code Success have been saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GenerateQR> call, Throwable t) {

            }
        });
    }
}
