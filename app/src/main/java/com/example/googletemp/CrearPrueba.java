package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CrearPrueba extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_prueba);
    }

    public void CreacionModulos(View r){
        Intent i = new Intent(this, CrearPrueba.class);
        startActivity(i);
    }
}