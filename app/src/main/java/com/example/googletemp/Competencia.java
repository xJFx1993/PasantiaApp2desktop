package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Competencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competencia);
    }



    public void UsarPruebasExistentes (View r){
        Intent i = new Intent(this, Lobby.class);
        startActivity(i);
    }


    public void CrearNuevaPrueba (View r){
        Intent i = new Intent(this, Lobby.class);
        startActivity(i);
    }
}