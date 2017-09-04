package com.jmarkstar.sesion1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/** Sesion 1
 * Created by jmarkstar on 01/09/2017.
 */
public class GoogleLocationApiActivity extends AppCompatActivity implements LocationListener {

    private static final int CODIGO_PETICION_LOCALIZACION  = 1000;

    private GoogleApiClient mGoogleApiClient;
    private Location mUltimaLocalizacion;
    private LocationRequest mSolicitadorLocalizacion;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_location_api);
        validarPermisoLocalizacion();
    }

    /** PASO 3: VALIDAR PERMISOS PARA ANDROID M A MAS.
     * */
    private void validarPermisoLocalizacion(){

        //PASO 3.1 Primero verifico si la version de android del dispositivo es mayor o igual a Android M.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            //PASO 3.2 verificar si el permiso ya fue otorgado.
            int locationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(locationPermissionCheck == PackageManager.PERMISSION_GRANTED){
                validarGpsActivo();
            }else{
                //el permiso esta denegado.
                //muestro un popup al usuario para que pueda dar el permiso a la aplicación.
                perdirPermisoLocalizacion();
            }
        }else{//es android Lollipop o menor.
            validarGpsActivo();
        }
    }

    /** PASO 3.3 AQUI LLEGA LA RESPUESTA QUE EL USUARIO LE DIO AL POPUP.
     * */
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //primero verifico el codigo de peticion
        if(requestCode == CODIGO_PETICION_LOCALIZACION){
            //luego, verifico si el usuario nos dio el permiso o no.
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                validarGpsActivo();
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

    /** PASO 4 Validar que el usuario tenga activado el gps, sino mostrar un mensaje y cerrar la aplicación.
     * */
    private void validarGpsActivo(){
        if(AppUtils.gpsEstaActivo(this)){
            configurarGoogleLocationAPI();
        }else{
            Toast.makeText(this, R.string.mensaje_gps_no_activo, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /** PASO 5: CONFIGURAR GOOGLE API CLIENT.
     */
    private void configurarGoogleLocationAPI(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //5.1: Configurar ConnectionCallbacks
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override public void onConnected(@Nullable Bundle bundle) throws SecurityException{
                        mUltimaLocalizacion = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mostrarLocalizacion();

                        //INICIAR ACTUALIZACIONES DE LOCALIZACION
                        iniciarActualizacionesPosicion();
                    }

                    @Override public void onConnectionSuspended(int i) {
                        //este metodo se ejecuta si la conexion es suspendida por algun motivo.
                        //motivos:
                        //1: el servicio fue matado.
                        //2: el dispositivo perdio la conexión.
                    }
                })
                //5.2: Configurar OnConnectionFailedListener
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(GoogleLocationApiActivity.this, R.string.google_error_conexion, Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(LocationServices.API)
                .build();

        //5.3: Conectar
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

    /** PASO 6: OBTENER LAS COORDENADAS
     * */
    private void iniciarActualizacionesPosicion() throws SecurityException{
        //paso 6.1 configurar LocationRequest
        mSolicitadorLocalizacion = new LocationRequest();
        mSolicitadorLocalizacion.setInterval(10000);
        mSolicitadorLocalizacion.setFastestInterval(5000);
        mSolicitadorLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //paso 6.3 iniciar las actualizaciones
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mSolicitadorLocalizacion, this);
    }

    /** PASO 7.1: Para las actualizaciones de las coordenadas
     * */
    private void pararActualizacionesPosicion() throws SecurityException{
        if (mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /** PASO 6.2 CONFIGURAR LocationListener
     * */
    @Override public void onLocationChanged(Location location) {
        mUltimaLocalizacion = location;
        mostrarLocalizacion();
    }

    private void mostrarLocalizacion(){
        if (mUltimaLocalizacion != null) {
            Toast.makeText(GoogleLocationApiActivity.this , "Ultima locaionzacion: Latitude:" +
                    mUltimaLocalizacion.getLatitude()+", Longitude:"+mUltimaLocalizacion.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }

    /** PASO 7 Cuando el activity se cierra, se debe de para las actualizaciones de posición y desconectar google api client.
     */
    @Override protected void onDestroy() {
        super.onDestroy();

        //7.1
        pararActualizacionesPosicion();

        //7.2
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}