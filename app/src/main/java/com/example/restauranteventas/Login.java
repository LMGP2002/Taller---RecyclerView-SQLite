package com.example.restauranteventas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {
    //Declaración de variables
    EditText txtUser, txtPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Relación con los elementos del layout
        txtUser=findViewById(R.id.txtUser);
        txtPass=findViewById(R.id.txtPass);
        btnLogin=findViewById(R.id.btnLogin);

        //Asignación de funcionalidad al botón
        btnLogin.setOnClickListener(v->{
            login();
        });
    }

    //Método para validar el inicio de sesión
    private void login() {
        String user=txtUser.getText().toString().trim();
        String pass=txtPass.getText().toString();
        if(!user.isEmpty()&&!pass.isEmpty()){
            if(user.equals(ConstantesLogin.user)&&pass.equals(ConstantesLogin.pass)){
                Toast.makeText(this, "Inicio sesión", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Digite todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}