package com.example.tourguide.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourguide.R;
import com.example.tourguide.model.MerchantItemStore;
import com.example.tourguide.model.MerchantItemUpdate;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.ImageFilePath;
import com.example.tourguide.service.JsonResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDiscount extends AppCompatActivity implements View.OnClickListener {

    int path;
    Boolean selectedImage = false;
    String urlImage;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private static final String TAG = "AddDiscount";
    private static final int PERMISSION_CODE = 1001;
    private static final int INTENT_REQUEST_CODE = 100;

    TextView mJudul;
    ImageView imageView;
    TextInputLayout mName,mNormalPrice,mDescription;
    TextView mStartDate,mEndDate,helperStartDate,helperEndDate;
    RelativeLayout mStartDateRelative,mEndDateRelative;
    Button mButton;
    MaterialButton mButton_save;
    ProgressBar mProggres;
    private int merchant_id;
    private String mPrice;
    String selectedImagePath;
    String mNameType,mDescriptionType;
    InputStream is;
    String MODE = "";
    File originalFile;
    Uri imageUrl;
    String token = "";

    Bitmap image;
    RequestBody requestFile;
    String editTextDesscription;
    String descriptionStorePromo;
    String mStartDatePromo;
    String mEndDatePromo;
    Button  mBackButton;
    int item_id;
    int value;
    String max_cut;
    private boolean changeClicked = false;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Calendar now = Calendar.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount);

        mJudul = findViewById(R.id.judul);

        mBackButton = findViewById(R.id.backButton);

        mName = findViewById(R.id.name);
        mNormalPrice = findViewById(R.id.price);
        mDescription = findViewById(R.id.description);
        mStartDate = findViewById(R.id.mStartDateFix);
        mEndDate = findViewById(R.id.endDateFix);
        helperStartDate = findViewById(R.id.helperTextSetting);
        helperEndDate = findViewById(R.id.helperTextSettingEnd);
        mStartDateRelative = findViewById(R.id.startDate);
        mEndDateRelative = findViewById(R.id.endDate);

        mProggres = findViewById(R.id.progress);
        mButton = findViewById(R.id.btnSelectImage);
        mButton_save = findViewById(R.id.btnSaveEA);

        imageView = findViewById(R.id.picOfProfile);

        path = getSharedPreferences("UserData",Context.MODE_PRIVATE).getInt("merchant_id",0);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDiscount.this,Merchant.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idMerchant" , path);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Intent intent =  getIntent();
        MODE = intent.getStringExtra("Mode");
        merchant_id = intent.getIntExtra("Merhant_id",0);
        editTextDesscription = intent.getExtras().getString("StringDescription","No Description");
        item_id = intent.getExtras().getInt("Item_id",0);
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        if(merchant_id == 0){
            Log.d(TAG, "onCreate: Payah masa merhcant_id = 0");
        }
        if(MODE.equals("Add")){
            mName.setHint("Name");
            mName.setHelperText("Enter the name of an item");
            mJudul.setText("Add an Item");
            helperEndDate.setVisibility(View.GONE);
            helperStartDate.setVisibility(View.GONE);
            mStartDateRelative.setVisibility(View.GONE);
            mEndDateRelative.setVisibility(View.GONE);
            mStartDate.setVisibility(View.GONE);
            mEndDate.setVisibility(View.GONE);
            mButton_save.setText("SAVE");
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permission not granted, request permission
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show pop up
                requestPermissions(permission,PERMISSION_CODE);
            }
            mButton.setOnClickListener(this);
            mButton_save.setOnClickListener(this);
        }else if (MODE.equals("AddPromo")){
            imageView.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            mJudul.setText("Add Promo");
            mName.setHint("Discount"); //Value
            mName.setHelperText("Enter in Percent");
            mName.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER); //Sets input type to Number Only
            mNormalPrice.setHint("Max Cutting Price"); //Max Cutting Price
            mDescription.getEditText().setText(editTextDesscription);
//            mStartDate.setHint("Enter start Promo");
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            System.out.println(date.format(formatter));
//            Calendar calendar = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            mStartDate.setText(formatter.format(calendar.getTime()));
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            mStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            AddDiscount.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String startDate;
                            month = month+1;
                            String date = year+"-"+month+"-"+dayOfMonth;
                            startDate = date;
//                            mStartDate.getEditText().setText(startDate);
                            mStartDate.setText(startDate);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
//            mEndDate.setHint("Enter end of Promo");
//            mEndDate.setText(String.valueOf(Calendar.getInstance().getTime()));
            mStartDate.setText(formatter.format(calendar.getTime()));
            final int yearEnd = calendar.get(Calendar.YEAR);
            final int monthEnd = calendar.get(Calendar.MONTH);
            final int dayEnd = calendar.get(Calendar.DAY_OF_MONTH);
            mEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            AddDiscount.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String endDate;
                            month = month+1;
                            String date = year+"-"+month+"-"+dayOfMonth;
                            endDate = date;
//                            mEndDate.getEditText().setText(endDate);
                            mEndDate.setText(endDate);
                        }
                    }, yearEnd, monthEnd, dayEnd);
                    datePickerDialog.show();
                }
            });
            mButton_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(storePromoValidate()){
                        storePromo();
                        mButton_save.setClickable(false);

                    }
                }
            });

        }else if (MODE.equals("UpdateItem")){
            mJudul.setText("Update Item");
            URL url = null;
                String patch = intent.getExtras().getString("StringURLImage");
//                url = new URL(patch);

            Glide.with(this)
                    .asBitmap()
                    .load(patch)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            imageUrl = Uri.parse(saveImage(resource));
                            Log.d(TAG, "onResourceReady: "+ imageUrl);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            mButton.setOnClickListener(this);
            helperEndDate.setVisibility(View.GONE);
            helperStartDate.setVisibility(View.GONE);
            mStartDateRelative.setVisibility(View.GONE);
            mEndDateRelative.setVisibility(View.GONE);
            mStartDate.setVisibility(View.GONE);
            mEndDate.setVisibility(View.GONE);
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permission not granted, request permission
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show pop up
                requestPermissions(permission,PERMISSION_CODE);
            }
            mButton_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validate()){
                        mButton_save.setClickable(false);
                        Log.d(TAG, "onClick: imageUrl "+ imageUrl);
                        updateItem(imageUrl);
                    }
                }
            });

        }
        Log.d(TAG, "onCreate: MODE " + MODE);
        Log.d(TAG, "onCreate: merhcant_id = " + merchant_id);



    }


    private boolean storePromoValidate() {
        mProggres.setVisibility(View.VISIBLE);

        this.max_cut = mNormalPrice.getEditText().getText().toString();
        if(mName.getEditText().toString()!=null){
            this.value = Integer.parseInt(mName.getEditText().getText().toString());
        }
        this.descriptionStorePromo = mDescription.getEditText().getText().toString();
        this.mStartDatePromo = mStartDate.getText().toString();
        this.mEndDatePromo = mEndDate.getText().toString();
        if(max_cut.isEmpty()){
            mNormalPrice.setError("Fill this field please!");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(mName.getEditText().getText().toString().isEmpty()){
            mName.setError("Fill this field please!");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(value > 100){
            mName.setError("Enter under 100 % value");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(descriptionStorePromo.isEmpty()){//max cut
            mDescription.setError("Fill this field please!");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(mStartDatePromo.isEmpty()){
//            mStartDate.setError("Fill this field please!");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(mEndDatePromo.equals(String.valueOf(formatter.format(Calendar.getInstance().getTime())))){//max cut
//            mEndDate.setError("Fill this field please!");
            helperEndDate.setText("Are you sure to setting on this date ?");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        return true;
    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void storePromo() {
        Call<JsonResponse> call = Api.getClient().storePromoApi("Bearer "+ token,item_id,value,descriptionStorePromo,descriptionStorePromo,max_cut,mStartDatePromo,mEndDatePromo);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                mProggres.setVisibility(View.GONE);
                Toast.makeText(AddDiscount.this, "Success to add Promo", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(AddDiscount.this,Merchant.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idMerchant" , path);
                back.putExtras(bundle);
                startActivity(back);

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }


    private boolean validate() {
        this.mNameType = mName.getEditText().getText().toString();
        this.mDescriptionType = mDescription.getEditText().getText().toString();
        this.mPrice = mNormalPrice.getEditText().getText().toString();


        if(!selectedImage){
            mButton.setTextColor(getResources().getColor(R.color.design_default_color_error));
            Drawable cant = getDrawable(R.drawable.cant_go);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageDrawable(cant);
            mProggres.setVisibility(View.GONE);
            return false;
        }
        if(mNameType.isEmpty()){
            mName.setError("Is Not Valid Email");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(mDescriptionType.isEmpty()){
            mDescription.setError("Please Fill The Field");
            mProggres.setVisibility(View.GONE);

            return false;
        }
        if(mPrice.isEmpty()){
            mNormalPrice.setError("You cant set item to free");
            mProggres.setVisibility(View.GONE);

            return false;
        }

        mProggres.setVisibility(View.GONE);

        return true;

    }
    private void updateItem(Uri uri) {


//            Log.d(TAG, "uploadImage: Uri " + uriUpdateItem);
            try {
                requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(imageUrl));
                if(uri != null){
                    //FileUtil for get from gallery
                    originalFile =  FileUtil.from(AddDiscount.this,uri);
                     requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), originalFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

//        RequestBody filePart = RequestBody.create(
//                MediaType.parse("multipart/form-data"),originalFile);

            String rToken = "Bearer "  + token;

            String method = "PUT";
            RequestBody requestMerchant_id =
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(merchant_id));
            RequestBody reqmNameType =
                    RequestBody.create(MediaType.parse("multipart/form-data"), mNameType);
            RequestBody reqmDescriptionType =
                    RequestBody.create(MediaType.parse("multipart/form-data"), mDescriptionType);
            RequestBody reqmPrice =
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(mPrice));
            RequestBody reqmMethod =
                    RequestBody.create(MediaType.parse("multipart/form-data"), method);


            Log.d(TAG, "uploadImage: selectedImage " + selectedImagePath);
            Log.d(TAG, "uploadImage: FileUtilsSelectedImage " + originalFile);
            MultipartBody.Part photo =
                    MultipartBody.Part.createFormData("photo", "NameImage",requestFile);
            int item_id = getIntent().getExtras().getInt("item_id",0);

            Call<MerchantItemUpdate> call = Api.getClient().updateItem("Bearer " + token,photo,requestMerchant_id,reqmNameType,reqmDescriptionType,reqmPrice,item_id,reqmMethod);
            mProggres.setVisibility(View.VISIBLE);
            mButton_save.setClickable(false);
            call.enqueue(new Callback<MerchantItemUpdate>() {

                @Override
                public void onResponse(Call<MerchantItemUpdate> call, Response<MerchantItemUpdate> response) {
                    if (response.isSuccessful()) {

                        mProggres.setVisibility(View.GONE);

                        MerchantItemUpdate merchantItemStore = response.body();
                        Log.d(TAG, "onResponse: " +  merchantItemStore.getData().getName());

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddDiscount.this);
                        builder1.setMessage("Success to Update.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddDiscount.this,Merchant.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("idMerchant" , path);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        Toast.makeText(AddDiscount.this,"Success to Update " + response.body().getData().getName(),Toast.LENGTH_LONG ).show();

                    } else {
                        mProggres.setVisibility(View.GONE);

//                    Log.d(TAG, "onResponse: is Not Succesfull");
                        assert response.body() != null;
//                    Toast.makeText(AddDiscount.this,"Success to Add " + response.body().getData().getName(),Toast.LENGTH_LONG ).show();
                        try {
                            Log.d(TAG, "onResponse: is Not Successfully " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: code " + response.code());
                        Log.d(TAG, "onResponse: Headers " + response.headers());
                        Log.d(TAG, "onResponse: Message " + response.message());

                    }
                }

                @Override
                public void onFailure(Call<MerchantItemUpdate> call, Throwable t) {

                }
            });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK && changeClicked) {

                try {

                    imageUrl = data.getData();
                    Log.d(TAG, "onActivityResult: " + imageUrl);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUrl);
//                    saveImage(bitmap);
                    selectedImagePath  = ImageFilePath.getPath(
                            AddDiscount.this, imageUrl);
                    is = getContentResolver().openInputStream(data.getData());
                    mButton.setText("Change Image");

                    selectedImage = true;
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            else{
//                selectedImagePath  = ImageFilePath.getPath(
//                        AddDiscount.this, imageUrl);
//            }
        }
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

            mButton.setText("Change Image");
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return "";
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }
    private void storeItem(Uri uri){
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

//        if(!selectedImagePath.equals("")){
//            File file = new File(selectedImagePath);
//        }
//        File file = new File("");
        Log.d(TAG, "uploadImage: Uri " + uri);
        try {
            originalFile =  FileUtil.from(AddDiscount.this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        RequestBody filePart = RequestBody.create(
//                MediaType.parse("multipart/form-data"),originalFile);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), originalFile);
        String rToken = "Bearer "  + token;

        RequestBody requestMerchant_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(merchant_id));
        RequestBody reqmNameType =
                RequestBody.create(MediaType.parse("multipart/form-data"), mNameType);
        RequestBody reqmDescriptionType =
                RequestBody.create(MediaType.parse("multipart/form-data"), mDescriptionType);
        RequestBody reqmPrice =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(mPrice));


        Log.d(TAG, "uploadImage: selectedImage " + selectedImagePath);
        Log.d(TAG, "uploadImage: FileUtilsSelectedImage " + originalFile);
        MultipartBody.Part photo =
                MultipartBody.Part.createFormData("photo", originalFile.getName(),requestFile);
//        MultipartBody.Part file1 =
//                MultipartBody.Part.createFormData("gambar",originalFile.getName(),filePart);
        Log.d(TAG, "uploadImage requestFile: " + requestFile);

        Call<MerchantItemStore> call = Api.getClient().uploadItem(rToken,photo,requestMerchant_id,reqmNameType,reqmDescriptionType,reqmPrice);
        mProggres.setVisibility(View.VISIBLE);
        mButton_save.setClickable(false);

        call.enqueue(new Callback<MerchantItemStore>() {
            @Override
            public void onResponse(Call<MerchantItemStore> call, Response<MerchantItemStore> response) {
                if (response.isSuccessful()) {

                    mProggres.setVisibility(View.GONE);

                    MerchantItemStore merchantItemStore = response.body();
                    Log.d(TAG, "onResponse: " +  merchantItemStore.getData().getName());

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddDiscount.this);
                    builder1.setMessage("Success to Add.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(AddDiscount.this,Merchant.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("idMerchant" , path);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    Toast.makeText(AddDiscount.this,"Success to Add " + response.body().getData().getName(),Toast.LENGTH_LONG ).show();

                } else {
                    mProggres.setVisibility(View.GONE);

//                    Log.d(TAG, "onResponse: is Not Succesfull");
                    assert response.body() != null;
//                    Toast.makeText(AddDiscount.this,"Success to Add " + response.body().getData().getName(),Toast.LENGTH_LONG ).show();
                    try {
                        Log.d(TAG, "onResponse: is Not Successfully " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponse: code " + response.code());
                    Log.d(TAG, "onResponse: Headers " + response.headers());
                    Log.d(TAG, "onResponse: Message " + response.message());

                }
            }

            @Override
            public void onFailure(Call<MerchantItemStore> call, Throwable t) {
                String Message = t.getMessage();
//                Toast.makeText(SignInActivity.this,"Sorry Try Again" + emailtype + "/ " + "pass " + Message,Toast.LENGTH_LONG ).show();
////                Log.d("response", t.getStackTrace().toString());
                if (t instanceof SocketTimeoutException)
                {
                    mProggres.setVisibility(View.GONE);

                    Toast.makeText(AddDiscount.this,"Connection time out  " + Message,Toast.LENGTH_LONG ).show();
                }
                else if (t instanceof IOException)
                {
                    mProggres.setVisibility(View.GONE);

                    Log.d(TAG+"f", "onFailure: "+ Message);
                    Toast.makeText(AddDiscount.this,"Time Out  " + Message,Toast.LENGTH_LONG ).show();
                }
                else
                {
                    //Call was cancelled by user
                    if(call.isCanceled())
                    {
                        mProggres.setVisibility(View.GONE);

                        Log.d(TAG, "onFailure: Cancel" + Message);
                        System.out.println("Call was cancelled forcefully");
                    }
                    else
                    {
                        mProggres.setVisibility(View.GONE);

                        Log.d(TAG, "onFailure: Network Error " + Message);

                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSelectImage:
                // code for button when user clicks buttonOne.
                Log.d(TAG, "onClick: Clicked");
                changeClicked = true;
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/jpeg");
                try {
                    startActivityForResult(i, INTENT_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
                break;

            case R.id.btnSaveEA:
                if(validate()){
                    mButton_save.setClickable(false);
                    storeItem(imageUrl);
                }
                
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //nothing
    }
}
