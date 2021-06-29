package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class SesionEstudiante extends AppCompatActivity {

    private Button BInstrucciones;

    private TextView Globalname;

    private FirebaseAuth mAuth;
    RequestQueue RQ;

    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_estudiante);

        mAuth = FirebaseAuth.getInstance();

        RQ= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        vistas();

        ReadUser(mAuth.getCurrentUser().getUid());
    }

    private void vistas() {
        BInstrucciones= (Button)findViewById(R.id.BInstrucciones);
        Globalname = (TextView) findViewById(R.id.Welcome);
    }

    public void Instrucciones (View r){
        Intent i = new Intent(this, InfoCRUDPrueba.class);
        startActivity(i);
    }

    public void Modificar (View r){
        Intent i = new Intent(this, Modificar.class);
        startActivity(i);
    }

    public void CrudPrueba (View r){
        Intent i = new Intent(this, CRUDPrueba.class);
        startActivity(i);
    }

    public void RealizarPrueba (View r){
        Intent i = new Intent(this, Prueba.class);
        startActivity(i);
    }

    public void VerResultados (View r){
        Intent i = new Intent(this, VerSuPuntaje.class);
        startActivity(i);
    }

    public void CompetenciaEstudiantes (View r){
        Intent i = new Intent(this, Competencia.class);
        startActivity(i);
    }

    public void Sugerencias (View r){
        Intent i = new Intent(this, Sugerencias.class);
        startActivity(i);
    }

    public void ReadUser(String identificador_F){
        String URL ="http://192.168.0.100/android/persona/fetch.php?id="+identificador_F;
        JsonObjectRequest jsOR = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String name, aux;
                        try {
                            name = response.getString("Nombre");
                            aux= Globalname.getText().toString() ;
                            name = aux + " " +name;

                            Globalname.setText(name);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        RQ.add(jsOR);

    }


    //menu over flow
    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //metodo para asignar las funciones correspondientes a las opciones
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.item1){
            Toast.makeText(this,"Cerrando sesion",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            mAuth.signOut();


            Intent intent = new Intent(this, BigG.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);


        }/*else if (id == R.id.item2){
            Toast.makeText(this,"OP2",0).show();
        }else if (id == R.id.item3){
            Toast.makeText(this,"OP3",0).show();
        }*/
        //palabra revervada super de java
        return super.onOptionsItemSelected(item);
    }


}