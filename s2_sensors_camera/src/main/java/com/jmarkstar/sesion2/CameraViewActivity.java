package com.jmarkstar.sesion2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.cameraview.CameraView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CameraViewActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_PERMISSIONS = 1000;

    private static final String TAG = "CameraViewActivity";
    private CameraView mCameraView;

    private Handler mBackgroundHandler;
    private MediaPlayer mediaPlayerCamera;
    private Button btnTomarFoto;
    private Button btnFlash;
    private Button btnCambiarCamara;

    public static void start(Context context) {
        Intent starter = new Intent(context, CameraViewActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        mCameraView = (CameraView) findViewById(R.id.camera);
        btnTomarFoto = (Button)findViewById(R.id.btn_tomar_foto);
        btnFlash = (Button)findViewById(R.id.btn_flash);
        btnCambiarCamara = (Button)findViewById(R.id.btn_camara);

        btnTomarFoto.setOnClickListener(this);
        btnCambiarCamara.setOnClickListener(this);
        btnFlash.setOnClickListener(this);

        if(mCameraView != null) {
            mCameraView.addCallback(mCallback);
            mCameraView.getAdjustViewBounds();
        }

        settingCameraAudio();
    }

    @Override public void onClick(View view) {
        if(view.getId() == R.id.btn_tomar_foto){
            mCameraView.takePicture();
            mediaPlayerCamera.start();
        }else if(view.getId() == R.id.btn_flash){
            cambiarFlash();
        }else if(view.getId() == R.id.btn_camara){
            cambiarCamara();
        }
    }

    private void cambiarFlash(){
        if (mCameraView != null) {
            int flash = mCameraView.getFlash();
            mCameraView.setFlash(flash == CameraView.FLASH_OFF? CameraView.FLASH_ON: CameraView.FLASH_OFF);
        }
    }

    private void cambiarCamara(){
        if (mCameraView != null) {
            int facing = mCameraView.getFacing();
            mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                    CameraView.FACING_BACK : CameraView.FACING_FRONT);
        }
    }

    private void settingCameraAudio(){
        try {
            AssetFileDescriptor afd = getAssets().openFd("camera-shutter.mp3");
            mediaPlayerCamera = new MediaPlayer();
            mediaPlayerCamera.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayerCamera.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            mCameraView.start();
        }
    }

    private void checkPermissions(){
        List<String> permissionsToAsk = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToAsk.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToAsk.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissionsToAsk.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionsToAsk.toArray(new String[permissionsToAsk.size()]), REQUEST_PERMISSIONS);
        }else{
            mCameraView.start();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSIONS){
            if(grantResults.length==2){
                boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean accessStorePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(accessStorePermission && cameraPermission)
                    mCameraView.start();
                else
                    checkPermissions();
            }else{
                boolean permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(permission)
                    mCameraView.start();
                else
                    checkPermissions();
            }
        }
    }

    @Override protected void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {
        @Override public void onCameraOpened(CameraView cameraView) {
            Log.v(TAG, "onCameraOpened");
        }

        @Override public void onCameraClosed(CameraView cameraView) {
            Log.v(TAG, "onCameraClosed");
        }

        @Override public void onPictureTaken(CameraView cameraView, final byte[] data) {
            getBackgroundHandler().post(new Runnable() {
                @Override public void run() {
                    InputStream is = new ByteArrayInputStream(data);
                    Bitmap bmp = BitmapFactory.decodeStream(is);

                    Log.v(TAG, "foto tomada");

                }
            });
        }
    };
}
