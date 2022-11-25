package com.example.app_mqtt_conexion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Ubicacion_maps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    EditText txtLatitud, txtLongitud;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_maps);



        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        double latitud = -33.449030;
        double longitud =  -70.662426;             // VARIABLES TEMPORALES

        txtLatitud.setText(String.valueOf(latitud));
        txtLongitud.setText(String.valueOf(longitud));

        LatLng GPS = new LatLng( latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(GPS).title("Ubicacion de la persona monitoreada "));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GPS));
    }



    @Override
    public void onMapClick(@NonNull LatLng latLng) {                 // metodo que coloca en las cajas de texto la ubicacion que presionamos en el mapa retorna latitud y longitud
        txtLatitud.setText(String.valueOf(latLng.latitude));
        txtLongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear();
        LatLng PosicionElegida = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(PosicionElegida).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PosicionElegida));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {                         // metodo que coloca en las cajas de texto la ubicacion que presionamos por un largo tiempo en el mapa retorna latitud y longitud
        txtLatitud.setText(String.valueOf(latLng.latitude));
        txtLongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear();
        LatLng GPS = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(GPS).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GPS));
    }
}