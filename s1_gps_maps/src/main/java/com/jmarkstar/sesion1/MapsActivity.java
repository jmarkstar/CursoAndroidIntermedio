package com.jmarkstar.sesion1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                        GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        LatLng LIMA = new LatLng(-12.0539185, -77.042364);
        mMap.addMarker(new MarkerOptions().position(LIMA).title("Lima"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LIMA));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LIMA, 15));
    }

    @Override public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, String.format(getString(R.string.selected_marker_text), marker.getTitle()), Toast.LENGTH_SHORT).show();
        return false;
    }
}
