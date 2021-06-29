package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerSuPuntaje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_su_puntaje);
    }

    public void GenerarReporte(View r){
       /* Intent i = new Intent(this, CrearPrueba.class);
        startActivity(i);*/
    }

    public void MasDetalles(View r){
        Intent i = new Intent(this, MasDetalles.class);
        startActivity(i);
    }
}