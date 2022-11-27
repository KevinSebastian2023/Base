package com.example.app_mqtt_conexion;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.graphics.Color;
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
    MqttAndroidClient client;
    TextView oxigeno;
    TextView pulsaciones;

    //variables a utilizar para las notificaciones

    private static final String CHANNEL_ID = "NOTIFICACION";
    public static final int NOTIFICACION_ID = 0;
    PendingIntent pendingIntent;
    PendingIntent siPending, noPending;

    private static final int PENDING_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);



        oxigeno = (TextView) findViewById(R.id.texto_oxigeno);
        pulsaciones = (TextView) findViewById(R.id.texto_pulsaciones);


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
                        String a = String.valueOf(Message.charAt(0)); // si deseamos immprimir un valor mayor a 100 elegimos este metodo
                        String b = String.valueOf(Message.charAt(1)); // pero en nuestro caso no puedde ser mayor a 100 % el nivel de oxigenacion en la sangre

                        String c = String.valueOf(Message.charAt(2)); // por lo tanto imprimimos 100
                        oxigeno.setText("100%");
                    }


                }
                if (Objects.equals(topic, "st/n")){     //topico y subtopico de la notificacion


                    String Message = message.toString();
                    if(Message.equals("1")){

                        setNoPendingIntent();
                        setSiPendingIntent();
                        setPendingIntent();
                        createNotificacionChannel();
                        crearNotificaciones();
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

            client.subscribe("st/ir", 0); //sensor de nivel de oxigeno
            client.subscribe("st/r", 0);  //sensor de pulsaciones por minuto
            client.subscribe("st/n", 0);  // alerta


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    private void createNotificacionChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Características del Canal
            CharSequence name="Notificacion Normal";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //notificationChannel.setAllowBubbles(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    //Inferiores a Oreo API 26 Android 8.0
    private void crearNotificaciones(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.corazon);
        builder.setContentTitle("Anomalia Detectada");
        builder.setContentText("Ver ubicacion GPS del usuario ");
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Anomalia detectada del usuario , presiona para ver su ubicacion actual "));
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA,1000,1000);
        builder.setVibrate(new long[]{1000,1000,1000, 1000});
        //builder.setSound()
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        builder.setNumber(7);
        //builder.addAction(R.drawable.ic_launcher_background,"Sí", siPending);   // METODO PARA PREGUNTAR SI DESEA HACER ALGO DESDE LA NOTIFICACION
        //builder.addAction(R.drawable.ic_launcher_background,"No", noPending);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
    }

    private void setNoPendingIntent() {   // METODO QUE VA CON LA ELECCION SI DESEAS ELEGIR EJECUTAR ALGUNA ACCION DESDE LA NOTIFICACION
        Intent intent = new Intent(this, Ubicacion_maps.class);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Ubicacion_maps.class);
        stackBuilder.addNextIntent(intent);


        noPending = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setSiPendingIntent() {  // METODO QUE VA CON LA ELECCION SI DESEAS ELEGIR EJECUTAR ALGUNA ACCION DESDE LA NOTIFICACION
        Intent intent = new Intent(this, Ubicacion_maps.class);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Ubicacion_maps.class);
        stackBuilder.addNextIntent(intent);


        siPending = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void setPendingIntent() {
        Intent intent = new Intent(this, Ubicacion_maps.class);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Ubicacion_maps.class);
        stackBuilder.addNextIntent(intent);


        pendingIntent = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);


    }
}