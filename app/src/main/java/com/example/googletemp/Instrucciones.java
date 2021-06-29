package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Instrucciones extends AppCompatActivity {

    private Button BInfoCRUD ;
    private Button BInfoResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        Vistas();
    }

    private void Vistas() {
        BInfoCRUD= (Button)findViewById(R.id.BInfoCRUD);
        BInfoResultados= (Button)findViewById(R.id.BInfoResultados);
    }

    public void InfoCRUD (View r){
        Intent i = new Intent(this, InfoCRUDPrueba.class);
        startActivity(i);
    }

    public void InfoResultados (View r){
        Intent i = new Intent(this, InfoResultados.class);
        startActivity(i);
    }
}