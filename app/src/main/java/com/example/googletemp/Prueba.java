package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Prueba extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
    }

    public void Mat (View r){
        Intent i = new Intent(this, Mmat.class);
        startActivity(i);
    }

    public void Lectura (View r){
        Intent i = new Intent(this, Mlec.class);
        startActivity(i);
    }

    public void SC (View r){
        Intent i = new Intent(this, Msc.class);
        startActivity(i);
    }

    public void CN (View r){
        Intent i = new Intent(this, Mcn.class);
        startActivity(i);
    }

    public void Ingles (View r){
        Intent i = new Intent(this, Ming.class);
        startActivity(i);
    }
}