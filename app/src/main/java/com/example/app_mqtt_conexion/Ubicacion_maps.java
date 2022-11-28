package com.example.app_mqtt_conexion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;            // LIBRERIAS PARA CONECTAR ANDROID CON EL SERVIDOR MQTT
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Objects;


public class Ubicacion_maps extends Monitor implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {   // APLICAMOS  HERENCIA

    EditText txtLatitud, txtLongitud;
    GoogleMap mMap;
    MqttAndroidClient client;

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




         double latitud1 = latitud;
        double longitud2 =  longitud;            // VARIABLES TEMPORALES


        txtLatitud.setText(String.valueOf(latitud));
        txtLongitud.setText(String.valueOf(longitud));

        LatLng GPS = new LatLng( latitud1, longitud2);

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
    private void setSubscription() {

        try {

            client.subscribe("st/n", 0);  // alerta


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}