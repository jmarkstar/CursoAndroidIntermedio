package com.jmarkstar.sesion2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jmarkstar on 9/8/17.
 */

public class AppUtils {

    public static String createImageFile(Context context) {
        String imageFileName = "PNG_" + System.currentTimeMillis() + "_";
        try {
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
            return image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createVideoFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "V_"+ timeStamp;
        try {
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            File video = File.createTempFile(videoFileName, ".mp4", storageDir);
            return video.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** le baja el tamaño a la imagen.
     * */
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    /** si la imagen esta rotada, o mal posicionada, este metodo lo rota de forma portrair.
     * */
    public static Bitmap rotateBitmap(Bitmap bitmap, String mImageFileLocation){
        ExifInterface exifInterface = null;
        try{
            exifInterface = new ExifInterface(mImageFileLocation);
        }catch(IOException e){
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90: matrix.setRotate(90);break;
            case ExifInterface.ORIENTATION_ROTATE_180: matrix.setRotate(180);break;
            case ExifInterface.ORIENTATION_ROTATE_270: matrix.setRotate(270);break;
            default:
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /** Primero, disminuye la calidad un 50 % a la imagen y luego lo guarda.
     * */
    public static void storeBitmap(Bitmap bitmap, String filename){
        FileOutputStream out = null;
        try {
            File file = new File(filename);
            if(file.exists())
                file.delete();

            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
