package com.example.app_mqtt_conexion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Bienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // agregando animaciones

        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        TextView texto = findViewById(R.id.Texto_Logo);
        ImageView imagenlogo = findViewById(R.id.imageView2);
        texto.setAnimation(animacion2);
        imagenlogo.setAnimation(animacion1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Bienvenida.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        } , 4000);
    }
}