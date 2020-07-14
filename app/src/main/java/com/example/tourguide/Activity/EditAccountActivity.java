package com.example.tourguide.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourguide.R;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.location.places.ui.PlacePicker;
//import com.google.android.gms.maps.model.LatLng;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.ImageFilePath;
import com.example.tourguide.service.JsonResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "EditAccountACT";
    boolean selectedImage = false;
    static final int REQUEST_CODE = 989;
    static final int INTENT_REQUEST_CODE = 123;
//    private static final int PLACE_PICKER_REQUEST = 56789;
    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;

    int path;
    String token;
    String nameStore,addressStore,descriptionStore;
    double latitude,longitude;
    Uri mImageUrl;
    InputStream is;
    File originalFile;
    RequestBody requestFile;

    TextView mTitle;
    ImageView mImageView;
    TextInputLayout mName,mDescription,mAddress;
    String addressTrue;
    LatLng mAddressGet;
    MaterialButton mBack,mSelectImage,mSave;
    ProgressBar mProgress;
    Button mBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_edit_account);

        mBackButton  = findViewById(R.id.backButton);
        mTitle = findViewById(R.id.title_edit);
        mName = findViewById(R.id.nameStoreEdit);
        mDescription = findViewById(R.id.descriptionStoreEdit);
        mProgress = findViewById(R.id.mProgressBar);

        mImageView = findViewById(R.id.picOfProfileEA);



        mBack = findViewById(R.id.btnBackEA);
        mSave = findViewById(R.id.btnSaveEA);
        mSelectImage = findViewById(R.id.btnSelectImageEdit);
        mAddress = findViewById(R.id.addressStoreEdit);
        mProgress.setVisibility(View.GONE);

        token  = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("token"," ");

        mAddress.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent (EditAccountActivity.this, com.example.tourguide.Activity.PlacePicker.class));
                goToPickerActivity();

            }
        });
        path = getSharedPreferences("UserData",Context.MODE_PRIVATE).getInt("merchant_id",0);
        setPreviousInfo();



        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAccountActivity.this,Merchant.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idMerchant" , path);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mSelectImage.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);

    }

    private void setPreviousInfo() {
        Intent prev = getIntent();
        mName.getEditText().setText(
        prev.getExtras().getString("name"," "));
        mDescription.getEditText().setText(
                prev.getExtras().getString("description"," "));
        mAddress.getEditText().setText(
                prev.getExtras().getString("address"," "));
        Glide.with(this)
                .asBitmap()
                .load(prev.getExtras().getString("image"))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mImageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }


    private void goToPickerActivity() {
        SharedPreferences preferences = getSharedPreferences("UserData",MODE_PRIVATE);
        String lat,lon;
        lat =         preferences.getString("latitude","-7.764262");
        lon =         preferences.getString("longitude","110.3691065");

        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.access_token))
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).zoom(16).build())
                                .build())
                        .build(this), REQUEST_CODE);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker.getPlace(data);

            // Set the TextView text to the entire CarmenFeature. The CarmenFeature
            // also be parsed through to grab and display certain information such as
            // its placeName, text, or coordinates.
            if (carmenFeature != null) {
//                selectedLocationTextView.setText(String.format(
//                        getString(R.string.selected_place_info), carmenFeature.toJson()));
                mAddress.getEditText().setText(carmenFeature.placeName());
                latitude = carmenFeature.center().latitude();
                longitude = carmenFeature.center().longitude();
            }
        }
        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                try {

                    mImageUrl = data.getData();
//                        Log.d(TAG, "onActivityResult: " + mImageUrl);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUrl);
//                    saveImage(bitmap);
//                        selectedImagePath  = ImageFilePath.getPath(
//                                EditAccountActivity.this, mImageUrl);
                    is = getContentResolver().openInputStream(data.getData());
                    mSelectImage.setText("Change Image");
                    selectedImage = true;
                    mImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean validate() {
        nameStore = mName.getEditText().getText().toString();
        addressStore = mAddress.getEditText().getText().toString();
        descriptionStore = mDescription.getEditText().getText().toString();

//        if(!selectedImage){
//            mSelectImage.setTextColor(getResources().getColor(R.color.design_default_color_error));
//            Drawable cant = getDrawable(R.drawable.cant_go);
//            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            mImageView.setImageDrawable(cant);
//            mProgress.setVisibility(View.GONE);
//            return false;
//        }
        if(nameStore.isEmpty()){
            mName.setError("Fill the field above");
            return false;
        }if(addressStore.isEmpty()){
            mAddress.setError("Select the address");
            return false;

        }if(descriptionStore.isEmpty()){
            mDescription.setError("Fill the field above");
            return false;

        }

        return true;
    }

    private void updateStore(Uri uri){
        Call<JsonResponse> call;
        if(uri != null){
            try {
                if(uri != null){
                    //FileUtil for get from gallery
                    originalFile =  FileUtil.from(EditAccountActivity.this,uri);
                    requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), originalFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        String method = "PUT";
        RequestBody reqAddress =
                RequestBody.create(MediaType.parse("multipart/form-data"),addressStore );
        RequestBody reqName =
                RequestBody.create(MediaType.parse("multipart/form-data"),nameStore );
        RequestBody reqDesc =
                RequestBody.create(MediaType.parse("multipart/form-data"),descriptionStore );
        RequestBody reqLatitude =
                RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(latitude) );
        RequestBody reqLongitude =
                RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(longitude) );
        RequestBody reqmMethod =
                RequestBody.create(MediaType.parse("multipart/form-data"), method);

        if(requestFile != null){
            MultipartBody.Part photo =
                    MultipartBody.Part.createFormData("photo", originalFile.getName(),requestFile);
            call= Api.getClient().updateMerchant("Bearer " + token,reqAddress,reqName,reqLatitude,reqLongitude,reqDesc,photo,reqmMethod);

        }else{
            call= Api.getClient().updateMerchant("Bearer " + token,reqAddress,reqName,reqLatitude,reqLongitude,reqDesc,reqmMethod);
        }

        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(EditAccountActivity.this, Merchant.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt("idMerchant" , path);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(EditAccountActivity.this, "Success to Update", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onResponse: " + response.message());
                    Toast.makeText(EditAccountActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBackEA:
                Intent intent = new Intent(EditAccountActivity.this,Merchant.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idMerchant" , path);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnSaveEA:
                makeitLoad();
                if(validate()){
                    updateStore(mImageUrl);
                }
                break;
            case R.id.btnSelectImageEdit:
                selectedImage = true;
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/jpeg");
                try {
                    startActivityForResult(i, INTENT_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void makeitLoad() {
        mProgress.setVisibility(View.VISIBLE);
        mName.getEditText().getFreezesText();
        mAddress.getEditText().getFreezesText();
        mDescription.getEditText().getFreezesText();
    }
}
