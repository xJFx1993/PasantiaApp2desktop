package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EliminarPrueba extends AppCompatActivity {

    TextView Pruebas=null;
    private FirebaseAuth mAuth;
    RequestQueue RQ;

    EditText ETNombreDeleteV =null;
    EditText   ETCodigoDeleteV =null;

    int ID_BD=0;

    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_prueba);

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        vistas();
        Pruebas.setText("");


        //tenemos el id del usuario de la base de datos
        ID_BD=ReadUser(mAuth.getCurrentUser().getUid());

        //Toast.makeText(this,"Valor BD "+ ID_BD, Toast.LENGTH_SHORT).show();
    }
    public void CallDelete(View r) {

        //||
        if (ETNombreDeleteV.length()!=0  || ETCodigoDeleteV.length()!=0 ){

            if (ETNombreDeleteV.length()!=0 && ETCodigoDeleteV.length()!=0){
                Toast.makeText(this,"Estamos en el if que tocaria eliminar por nombre ambos campos digitados", Toast.LENGTH_SHORT).show();
            }else if (ETCodigoDeleteV.length()!=0){
                Toast.makeText(this,"Por codigo", Toast.LENGTH_SHORT).show();
                String URLc="";
            }else if (ETNombreDeleteV.length()!=0){
                Toast.makeText(this,"Por Nombre", Toast.LENGTH_SHORT).show();
                String URLn="";
            }

        }else{
            Toast.makeText(this,"Faltan datos", Toast.LENGTH_SHORT).show();
        }


    }

    public int ReadUser(String identificador_F){
        ID_BD = -1;
        String URL ="http://192.168.0.100/android/persona/fetch.php?id="+ identificador_F;
        JsonObjectRequest jsOR = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            ID_BD = response.getInt("Id_persona");
                            //return ID_BD;
                            //entrnadoenelresponse(response);
                            CargarPruebas(ID_BD);


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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsOR);
        // RQ.add(jsOR);
        return ID_BD;

    }

    private void vistas() {

        Pruebas = (TextView) findViewById(R.id.TVpruebasDelete);
        Pruebas.setMovementMethod (new ScrollingMovementMethod());

        EditText ETNombreDelete =null;
        EditText   ETCodigoDelete =null;

        ETNombreDeleteV = (EditText) findViewById(R.id.ETNombreDelete);
        ETCodigoDeleteV = (EditText) findViewById(R.id.ETCodigoDelete);
    }

    private void CargarPruebas(int id_bd) {

        String URL = "http://192.168.0.100/android/prueba/fetch-P.php?id="+id_bd;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                insertando_pruebas(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(jsonArrayRequest);
        //RQ.add(jsonArrayRequest);
    }

    private void insertando_pruebas(JSONArray response) {
        for (int i =0 ; i<response.length();i++){

            try {
                //JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(i)));
                JSONObject objeto2 = new JSONObject(response.get(i).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");

                Pruebas.append(i+1 + " :");
                Pruebas.append("    Nombre: "+objeto2.getString("Nombre"));
                Pruebas.append("    Codigo: "+objeto2.getString("id_prueba"));
                Pruebas.append("\n");



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void Call(View r) {
        Pruebas.setText("");

        CargarPruebas(ID_BD);

    }
}