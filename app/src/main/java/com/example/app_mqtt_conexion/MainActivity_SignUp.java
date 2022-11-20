package com.example.app_mqtt_conexion;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_SignUp extends AppCompatActivity {

    Button btn3, btn4, btn5;
    EditText mail, pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up);

        mail=findViewById(R.id.id_correo); //id_correo
        pass1=findViewById(R.id.id_contraseña); // id_contraseña
        pass2=findViewById(R.id.id_confirmContraseña); // id_confirmContraseña

        //simularemos un ingreso nuevo de credenciales y regresa al Login del MainActivity
        btn3=findViewById(R.id.button_Sign_Up);//button_Sign_Up
        btn3.setOnClickListener(view -> {
            if (mail.getText().toString().equals("admin@gmail.com")&&pass1.getText().toString().equals("admin")
                    &&pass2.getText().toString().equals("admin")){
                Intent a = new Intent(MainActivity_SignUp.this, MainActivity.class);
                Toast.makeText(MainActivity_SignUp.this,"Bienvenido Usuario Registrado",Toast.LENGTH_LONG).show();
                startActivity(a);
                //se incorpora una transición para mejor experiencia de usuario hacia la derecha
                overridePendingTransition(R.anim.to_left,R.anim.from_rigth);
            }
            else{
                //de no ser valida la credencial, saldra un mensaje de advertencia y finalizará regresando al inicio
                finish();
                Toast.makeText(MainActivity_SignUp.this,"Intente Nuevamente",Toast.LENGTH_LONG).show();
            }

        });

        //Asignaremos este boton solo para regresar al login
        btn4=findViewById(R.id.button_Login_signup);//button_Login_signup
        btn4.setOnClickListener(view -> {
            Intent b = new Intent(MainActivity_SignUp.this,MainActivity.class);
            startActivity(b);
            //se incorpora una transicion hacia la derecha para mejor expericia de usuario
            overridePendingTransition(R.anim.from_left,R.anim.to_right);
            finish();
        });


        //asignaremos este boton solo para la recuperacion de contraseña al MaintActivity_Forgot_Password
        btn5=findViewById(R.id.button_Forgot_Pass);//button_Forgot_Pass
        btn5.setOnClickListener(view -> {
            Intent c = new Intent(MainActivity_SignUp.this,MainActivity_Forgot_Password.class);
            startActivity(c);
            //se incorpora una transicion hacia la derecha para mejor expericia de usuario
            overridePendingTransition(R.anim.to_left,R.anim.from_rigth);
        });
    }

}