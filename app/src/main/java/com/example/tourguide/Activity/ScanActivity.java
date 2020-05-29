package com.example.tourguide.Activity;

import com.example.tourguide.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

public class ScanActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{
    private Size imageDimension;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder  captureRequestBuilder ;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;


    private EditText editText;
    private ImageView imageView;

    private String cameraId;
    private Camera mCamera;
    private TextureView mTextureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);

        mTextureView = findViewById(R.id.textureView);

        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);

        setContentView(mTextureView);
    }

    public void QRCodeButton(View view){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            openCamera();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera()throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            assert manager != null;
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics =  manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            //check realtime permission if run higest API 23
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return;
            }

            manager.openCamera(cameraId,statecallback,null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private CameraDevice.StateCallback statecallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onError(@NonNull CameraDevice camera, int i) {
            camera.close();
            cameraDevice= null;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreview() {
        try{
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(ScanActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updatePreview() {
        if(cameraDevice == null){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        }try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You Cant use Camera Without Permission on it", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if(mTextureView.isAvailable()){
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
    private void startBackgroundThread() {
        mBackgroundThread =  new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler =  new Handler(mBackgroundThread.getLooper());
    }
}
