package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModificarPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_password);
    }

    public void Ingresar (View r){
        //Validar cual es la sesion a mostrar por ahora predeterminada estudiante
        Intent i = new Intent(this, SesionEstudiante.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();
    }

    public void IngresarEst (View r){
        Intent i = new Intent(this, SesionEstudiante.class);
        startActivity(i);
        finish();
    }

    public void IngresarAdm (View r){
        Intent i = new Intent(this, SesionAdministrador.class);
        startActivity(i);
        finish();
    }

    public void IngresarEnc (View r){

        Intent i = new Intent(this, SesionEncargado.class);
        startActivity(i);
        finish();


    }
}