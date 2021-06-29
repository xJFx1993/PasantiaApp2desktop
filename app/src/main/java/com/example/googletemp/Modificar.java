package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Modificar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
    }


    public void Nickname (View r){
        Intent i = new Intent(this, Nickname.class);
        startActivity(i);
    }


    public void Password (View r){
        Intent i = new Intent(this, ModificarPassword.class);
        startActivity(i);
    }

}