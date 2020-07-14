package com.example.tourguide.Activity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.tourguide.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tourguide.model.ScanResponses;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.JsonResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity{

    private static final String TAG = "ScanActivity";

    private Size imageDimension;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder  captureRequestBuilder ;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    String token;

    CodeScannerView scannerView;
    CodeScanner codeScanner;

    Button mbuttonBack;

    private EditText editText;
    TextView mresultOfQR;
    private ImageView imageView;

    private String cameraId;
    private Camera mCamera;
    private TextureView mTextureView;

    @Override
    public void onBackPressed() {
        //nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mbuttonBack = findViewById(R.id.buttonBack);

        scannerView = findViewById(R.id.scanner_view);
        mresultOfQR = findViewById(R.id.resultOfQR);
        codeScanner = new CodeScanner(this,scannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mresultOfQR.setText(result.getText());
                        //CallAPI
                        callApi(result.getText());
                    }
                });
            }
        });

        mbuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this,LandingMainActivity.class);
                startActivity(intent);
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });

    }

    private void callApi(String results) {
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        Call<ScanResponses> call = Api.getClient().scanQrCode("Bearer "+ token,results);
        call.enqueue(new Callback<ScanResponses>() {
            @Override
            public void onResponse(Call<ScanResponses> call, Response<ScanResponses> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ScanActivity.this, "Scan QR Code Success", Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                    View dialog = LayoutInflater.from(ScanActivity.this).inflate(R.layout.custom_dialog_redeem,null,false);
                    TextView email = dialog.findViewById(R.id.mName);
                    TextView name = dialog.findViewById(R.id.judul);
                    TextView description = dialog.findViewById(R.id.info);
                    Button mOk = dialog.findViewById(R.id.buttonOk);

                    if(response.body().getUser()!= null){
                        email.setText(response.body().getUser().getEmail());
                        if (response.body().getUser().getName()!=null){

                            name.setText("Congratulation! " + response.body().getUser().getName());
                        }
                    }

                    description.setText(" User Guide will claim points sooner !" );

                    builder.setView(dialog);

                    AlertDialog alertDialog = builder.create();
                    mOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(ScanActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
                Toast.makeText(ScanActivity.this, "Failed to scan QR code", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: " +  results);

            }

            @Override
            public void onFailure(Call<ScanResponses> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCameraOpen();
    }

    private void requestCameraOpen() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(ScanActivity.this, "Camera Permission not Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}
