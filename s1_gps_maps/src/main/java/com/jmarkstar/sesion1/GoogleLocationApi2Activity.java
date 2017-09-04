package com.jmarkstar.sesion1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/** Sesion 1
 * Created by jmarkstar on 01/09/2017.
 */
public class GoogleLocationApi2Activity extends AppCompatActivity {

    private static final int CODIGO_PETICION_LOCALIZACION  = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private GoogleApiClient mGoogleApiClient;
    private Location mUltimaLocalizacion;
    private LocationRequest mSolicitadorLocalizacion;

    private LocationSettingsRequest.Builder settingsApiBuilder;

    private LinearLayout llDescripcionApp;
    private LinearLayout llErrorMensaje;
    private Button btnActivarGPS;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_location_api_2);
        llDescripcionApp = findViewById(R.id.ll_descripcion);
        llErrorMensaje = findViewById(R.id.ll_mensaje_error);
        btnActivarGPS = findViewById(R.id.btn_activar);
        btnActivarGPS.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                verificarConfiguracionLocalizacion();
            }
        });

        validarPermisoLocalizacion();
    }

    private void validarPermisoLocalizacion(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            int locationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(locationPermissionCheck == PackageManager.PERMISSION_GRANTED){
                configurarGoogleLocationAPI();
            }else{
                //el permiso esta denegado.
                //muestro un popup al usuario para que pueda dar el permiso a la aplicación.
                perdirPermisoLocalizacion();
            }
        }else{//es android Lollipop o menor.
            configurarGoogleLocationAPI();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //primero verifico el codigo de peticion
        if(requestCode == CODIGO_PETICION_LOCALIZACION){
            //luego, verifico si el usuario nos dio el permiso o no.
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                configurarGoogleLocationAPI();
            }else{
                //si no dio el permiso. vuelvo a mostrar el popup.
                perdirPermisoLocalizacion();
            }
        }
    }

    /** Abre un Popup que solicita al usuario dar el permiso de localización.
     * */
    private void perdirPermisoLocalizacion(){
        String permissions [] = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, CODIGO_PETICION_LOCALIZACION );
    }

    private void configurarGoogleLocationAPI(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //Configurar ConnectionCallbacks
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override public void onConnected(@Nullable Bundle bundle) throws SecurityException{
                        mUltimaLocalizacion = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mostrarLocalizacion();

                        crearLocationRequest();
                    }

                    @Override public void onConnectionSuspended(int i) {
                        //este metodo se ejecuta si la conexion es suspendida por algun motivo.
                        //motivos:
                        //1: el servicio fue matado.
                        //2: el dispositivo perdio la conexión.
                    }
                })
                //Configurar OnConnectionFailedListener
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(GoogleLocationApi2Activity.this, R.string.google_error_conexion, Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(LocationServices.API)
                .build();

        //Conectar
        mGoogleApiClient.connect();
    }

    private LocationCallback myLocationCallback = new LocationCallback(){
        @Override public void onLocationResult(LocationResult result) {
            mUltimaLocalizacion = result.getLastLocation();
            mostrarLocalizacion();
        }

        @Override public void onLocationAvailability(LocationAvailability locationAvailability) {
            if(!locationAvailability.isLocationAvailable()){
                Log.e("location", "is not Location Available");
                pararActualizacionesPosicion();
                verificarConfiguracionLocalizacion();
            }
        }
    };

    private void crearLocationRequest() throws SecurityException{
        mSolicitadorLocalizacion = new LocationRequest();
        mSolicitadorLocalizacion.setInterval(10000);
        mSolicitadorLocalizacion.setFastestInterval(5000);
        mSolicitadorLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mSolicitadorLocalizacion.setMaxWaitTime(100);

        settingsApiBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mSolicitadorLocalizacion);

        verificarConfiguracionLocalizacion();
    }

    /** Verifica si el servicio de GPS esta habilitado o no en las configuraciones del S.O.
     * */
    private void verificarConfiguracionLocalizacion(){
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(settingsApiBuilder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override public void onSuccess(LocationSettingsResponse locationSettingsResponse) throws SecurityException {
                //Si el GPS esta habilitado, entonces comenzamos a solicitar las coordenadas.
                iniciarActualizacionesPosicion();
            }
        });

        //Si ocurre un error, es porque el gps no esta disponible, po ello mostramos un popup para
        // solicitar la activacion de este.
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(GoogleLocationApi2Activity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            sendEx.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //En esta seccion podemos mostrar nuestro propio dialog con un intent a las configuraciones.
                        mostrarPopupSolicitarActivacionGPS();
                        break;
                }
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //INICIAR ACTUALIZACIONES DE LOCALIZACION
                        iniciarActualizacionesPosicion();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("location", "User chose not to make required location settings changes.");
                        llErrorMensaje.setVisibility(View.VISIBLE);
                        llDescripcionApp.setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

    private void iniciarActualizacionesPosicion() throws SecurityException{
        llErrorMensaje.setVisibility(View.GONE);
        llDescripcionApp.setVisibility(View.VISIBLE);
        if (mGoogleApiClient != null)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mSolicitadorLocalizacion, myLocationCallback, null);
    }

    private void pararActualizacionesPosicion() throws SecurityException{
        if (mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, myLocationCallback);
    }

    @Override protected void onDestroy() {
        pararActualizacionesPosicion();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    private void mostrarLocalizacion(){
        if (mUltimaLocalizacion != null) {
            Toast.makeText(this , "Ultima localización: Latitude: "+ mUltimaLocalizacion.getLatitude()+", longitude: "+mUltimaLocalizacion.getLongitude()
                    ,Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarPopupSolicitarActivacionGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.gps_dialog_descripcion)
                .setTitle(R.string.gps_dialog_titulo);
        builder.setPositiveButton(R.string.gps_dialog_boton, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}