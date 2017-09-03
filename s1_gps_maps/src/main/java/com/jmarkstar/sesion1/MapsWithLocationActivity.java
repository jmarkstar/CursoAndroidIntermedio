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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/** Sesion 1
 * Created by jmarkstar on 01/09/2017.
 */
public class MapsWithLocationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static final int CODIGO_PETICION_LOCALIZACION  = 1000;

    private GoogleApiClient mGoogleApiClient;
    private Location mUltimaLocalizacion;

    private GoogleMap mMap;
    private Marker mCurrentLocationMarker;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_with_location);
        validarPermisoLocalizacion();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void validarPermisoLocalizacion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int locationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(locationPermissionCheck == PackageManager.PERMISSION_GRANTED){
                validarGpsActivo();
            }else{
                perdirPermisoLocalizacion();
            }
        }else{
            validarGpsActivo();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODIGO_PETICION_LOCALIZACION){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                validarGpsActivo();
            }else{
                perdirPermisoLocalizacion();
            }
        }
    }

    private void perdirPermisoLocalizacion(){
        String permissions [] = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, CODIGO_PETICION_LOCALIZACION );
    }

    private void validarGpsActivo(){
        if(AppUtils.gpsEstaActivo(this)){
            configurarGoogleLocationAPI();
        }else{
            Toast.makeText(this, R.string.mensaje_gps_no_activo, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void configurarGoogleLocationAPI(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override public void onConnected(@Nullable Bundle bundle) throws SecurityException{
                        mUltimaLocalizacion = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mostrarLocalizacion();
                        iniciarActualizacionesPosicion();
                    }
                    @Override public void onConnectionSuspended(int i) {}
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MapsWithLocationActivity.this, R.string.google_error_conexion, Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(LocationServices.API)
                .build();
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

    private void iniciarActualizacionesPosicion() throws SecurityException{
        LocationRequest mSolicitadorLocalizacion = new LocationRequest();
        mSolicitadorLocalizacion.setInterval(10000);
        mSolicitadorLocalizacion.setFastestInterval(5000);
        mSolicitadorLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mSolicitadorLocalizacion, this);
    }

    private void pararActualizacionesPosicion() throws SecurityException{
        if (mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override public void onLocationChanged(Location location) {
        mUltimaLocalizacion = location;
        mostrarLocalizacion();
    }

    private void mostrarLocalizacion(){
        if (mUltimaLocalizacion != null) {
            LatLng latLng = new LatLng(mUltimaLocalizacion.getLatitude(), mUltimaLocalizacion.getLongitude());
            if(mCurrentLocationMarker==null){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getString(R.string.current_position_text));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrentLocationMarker = mMap.addMarker(markerOptions);
            }else{
                mCurrentLocationMarker.setPosition(latLng);
            }

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        pararActualizacionesPosicion();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}
