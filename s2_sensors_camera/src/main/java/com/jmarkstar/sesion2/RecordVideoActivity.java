package com.jmarkstar.sesion2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.jmarkstar.sesion2.util.AppUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordVideoActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_RECORD = 1000;
    private static final int REQUEST_PERMISSIONS = 200;

    private VideoView mVvVideo;

    private String videoPath;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecordVideoActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        mVvVideo = findViewById(R.id.vv_video);
    }

    public void recordVideo(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermissions();
        }else{
            goToCamera();
        }
    }

    public void goToCamera(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            videoPath = AppUtils.createVideoFile(this);
            if(videoPath!=null){
                File fileImage = new File(videoPath);
                Uri fotoURI;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    fotoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".fileprovider", fileImage);
                }else{
                    fotoURI = Uri.fromFile(fileImage);
                }
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_RECORD);
            }
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
            goToCamera();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS){
            if(grantResults.length==2){
                boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean accessStorePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(accessStorePermission && cameraPermission)
                    goToCamera();
                else
                    Toast.makeText(this, "Los permisos son necesarios.", Toast.LENGTH_SHORT).show();
            }else{
                boolean permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(permission)
                    goToCamera();
                else
                    Toast.makeText(this, "Los permisos son necesarios.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_VIDEO_RECORD && resultCode == RESULT_OK){
            Uri uri = Uri.parse(videoPath);
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(mVvVideo);
            mVvVideo.setMediaController(mediacontroller);
            mVvVideo.setVideoURI(uri);
            mVvVideo.requestFocus();
            mVvVideo.start();
        }
    }
}
