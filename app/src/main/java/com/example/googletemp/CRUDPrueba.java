package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CRUDPrueba extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r_u_d_prueba);
    }

    public void Crear(View r){
        Intent i = new Intent(this, CrearPrueba.class);
        startActivity(i);
    }

    public void Actualizar(View r){
        Intent i = new Intent(this, ActualizarPrueba.class);
        startActivity(i);
    }

    public void Eliminar(View r){
        Intent i = new Intent(this, EliminarPrueba.class);
        startActivity(i);
    }
    public void Ver(View r){
        Intent i = new Intent(this, VerPrueba.class);
        startActivity(i);
    }
}