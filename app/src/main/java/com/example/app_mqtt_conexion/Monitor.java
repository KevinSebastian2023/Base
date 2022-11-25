package com.example.app_mqtt_conexion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Objects;

public class Monitor extends AppCompatActivity {
    Button btn8;
    Button boton_mapa;
    MqttAndroidClient client;
    TextView oxigeno;
    TextView pulsaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);


        oxigeno = (TextView) findViewById(R.id.texto_oxigeno);
        pulsaciones = (TextView) findViewById(R.id.texto_pulsaciones);
        boton_mapa = findViewById(R.id.Cambiar_ubicacion);

        boton_mapa.setOnClickListener(view -> {

            Intent intent = new Intent(Monitor.this, Ubicacion_maps.class);
            startActivity(intent);
            overridePendingTransition(R.anim.to_left, R.anim.from_rigth);
            finish();

        });
        btn8 = findViewById(R.id.Boton_atras);

        btn8.setOnClickListener(view -> {

            Intent intent = new Intent(Monitor.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left, R.anim.to_right);
            finish();

        });


        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://68.183.119.177", clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Monitor.this, "Conectado ", Toast.LENGTH_LONG).show();
                    setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Monitor.this, "CONEXION FALLIDA!!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                if (Objects.equals(topic, "st/r")) {

                    pulsaciones.setText(new String(message.getPayload()));  // Recibimos el mensaje desde el MQTT en un recurso de texto tipo Texview para ser mostrado por pantalla al usuario

                    //String Message = message.toString();
                    //if(Message.equals("1")){

                    // pulsacion.setText("orden recibida");

                    //}
                    // else {

                    // pulsacion.setText(Message);
                    // }

                }


                if (Objects.equals(topic, "st/ir")) {


                    String Message = message.toString();      // tenemos el mensaje en una variable de tipo string


                    if (Message.length() == 4) {


                        oxigeno.setText("0%");
                    }

                    if (Message.length() == 5) {
                        String a = String.valueOf(Message.charAt(0));
                        String b = String.valueOf(Message.charAt(1));

                        oxigeno.setText(a + b + "%");

                    }

                    if (Message.length() == 6) {
                        String a = String.valueOf(Message.charAt(0));
                        String b = String.valueOf(Message.charAt(1));

                        String c = String.valueOf(Message.charAt(2));
                        oxigeno.setText(a + b + c + "%");
                    }


                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    private void setSubscription() {

        try {

            client.subscribe("st/ir", 0);
            client.subscribe("st/r", 0);


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}