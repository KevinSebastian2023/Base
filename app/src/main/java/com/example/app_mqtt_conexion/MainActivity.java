package com.example.app_mqtt_conexion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2;
    EditText user, mail, pass;

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.Id_Nombre); //Id_Nombre
        mail = findViewById(R.id.Id_correo); //Id_correo
        pass = findViewById(R.id.Id_password); //Id_password
        checkBox = findViewById(R.id.checkTerminos);//checkTerminos

        //Agregamos un checkBox simulando los terminos y condiciones
        checkBox.setOnCheckedChangeListener((compoundButton, check) -> {
            //si el estado del checkBox es un !true se mostrara el siguiente mensaje
            if (!check) {
                Toast.makeText(MainActivity.this, "Recuerde Aceptar los Términos para Iniciar", Toast.LENGTH_LONG).show();
            }
            //si el estado del checBox es true iniciará la  función correspondiente al btn1 login
            else {

                //Desde aquí iniciaremos sesion con el btn1 login
                btn1 = findViewById(R.id.button_Login);//button_Login
                //Se mantiene a la escucha del click del botón para desplegar la validacion de credenciales...
                btn1.setOnClickListener((view -> {
                    if (user.getText().toString().equals("1") && mail.getText().toString().equals("1") && pass.getText().toString().equals("1") && checkBox.isChecked()) {
                        // ya validado el usuario ingresara al Activity Monitor con un mensaje de Bienvenida
                        Intent p = new Intent(MainActivity.this, Monitor.class);
                        Toast.makeText(MainActivity.this, "Bienvenido " + user.getText().toString(), Toast.LENGTH_LONG).show();
                        startActivity(p);
                        overridePendingTransition(R.anim.to_left, R.anim.from_rigth);
                    } else {
                        // en caso de no ser validada la credencial, emergenra un mensaje de advertencia
                        Toast.makeText(MainActivity.this, "Credenciales Invalidas o Falta Aceptar los Términos para Iniciar", Toast.LENGTH_LONG).show();
                    }
                }));

            }

        });


        btn2 = findViewById(R.id.button_SignUp);//button_SignUp
        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity_SignUp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.to_left, R.anim.from_rigth);
        });
    }


}