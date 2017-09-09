package com.jmarkstar.sesion2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.jmarkstar.sesion2.util.AppUtils;
import java.io.File;
import java.io.IOException;

/**
 * Created by jmarkstar on 9/8/17.
 */
public class TakePictureActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_WRITE_STORE_PERMISSION = 100;

    private ImageView ivPhoto;
    private String photoPath;

    public static void start(Context context) {
        Intent starter = new Intent(context, TakePictureActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        ivPhoto = findViewById(R.id.iv_photo);
    }

    /** 1. Llamar al Intent para tomar fotos
     * */
    public void takePicture(View view){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permissionWriteStore = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionWriteStore == PackageManager.PERMISSION_GRANTED){
                goToCamera();
            }else{
                requestPermissions(new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORE_PERMISSION);
            }
        }else{
            goToCamera();
        }
    }

    private void goToCamera(){
        //crear un Intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //verificar que exista algun app que pueda recibir ese intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoPath = AppUtils.createImageFile(this);
            if(photoPath!=null){
                File fileImage = new File(photoPath);
                Uri fotoURI;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    fotoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".fileprovider", fileImage);
                }else{
                    fotoURI = Uri.fromFile(fileImage);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                //ir a la camara
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_STORE_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                goToCamera();
            }else{
                Toast.makeText(this, "El permiso es necesario.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            // Guardar la imagen miniatura de la foto.
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Obtener la imagen grande de la foto.
            //Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath);
            //ivPhoto.setImageBitmap(imageBitmap);

            //Reducir de tamaño y calidad la foto grande.
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath);
            imageBitmap = AppUtils.scaleDown(imageBitmap, 1280, true);
            imageBitmap = AppUtils.rotateBitmap(imageBitmap, photoPath);

            ivPhoto.setImageBitmap(imageBitmap);
            AppUtils.storeBitmap(imageBitmap, photoPath);
        }
    }
}
