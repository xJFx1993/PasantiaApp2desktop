package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        if (ETNombreDeleteV.length()!=0  || ETCodigoDeleteV.length()!=0 ){//ambos campos

            if (ETNombreDeleteV.length()!=0 && ETCodigoDeleteV.length()!=0){
                //Toast.makeText(this,"Estamos en el if que tocaria eliminar por nombre ambos campos digitados", Toast.LENGTH_SHORT).show();
                String URLn="http://192.168.0.100/android/prueba/delete-N.php";

                //creamos la prueba en la base de datos
                StringRequest SR = new StringRequest(
                        Request.Method.POST,
                        URLn,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(Registrarse.this, "Correct", Toast.LENGTH_SHORT).show();
                                //updateUI(identificador_F);

                                //metodo consultar id prueba y mostrarlo en un toast y edittext
                                //consultar_id_prueba();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params =  new HashMap<>();
                        params.put ("Nombre", ETNombreDeleteV.getText().toString());
                        return params;
                    }
                };

                RQ.add(SR);
                ETNombreDeleteV.setText("");
            }else if (ETCodigoDeleteV.length()!=0){//por codigo
                //Toast.makeText(this,"Por codigo", Toast.LENGTH_SHORT).show();
                String URLc="http://192.168.0.100/android/prueba/delete-C.php";

                //creamos la prueba en la base de datos
                StringRequest SR = new StringRequest(
                        Request.Method.POST,
                        URLc,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(Registrarse.this, "Correct", Toast.LENGTH_SHORT).show();
                                //updateUI(identificador_F);

                                //metodo consultar id prueba y mostrarlo en un toast y edittext
                              //  consultar_id_prueba();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params =  new HashMap<>();
                        params.put ("id", String.valueOf(ETCodigoDeleteV.getText()));

                        return params;
                    }
                };

                RQ.add(SR);
                ETCodigoDeleteV.setText("");

            }else if (ETNombreDeleteV.length()!=0){//por nombre
              //  Toast.makeText(this,"Por Nombre", Toast.LENGTH_SHORT).show();
                String URLn="http://192.168.0.100/android/prueba/delete-N.php";

                //creamos la prueba en la base de datos
                StringRequest SR = new StringRequest(
                        Request.Method.POST,
                        URLn,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(Registrarse.this, "Correct", Toast.LENGTH_SHORT).show();
                                //updateUI(identificador_F);

                                //metodo consultar id prueba y mostrarlo en un toast y edittext
                                //consultar_id_prueba();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params =  new HashMap<>();
                        params.put ("Nombre", ETNombreDeleteV.getText().toString());
                        return params;
                    }
                };

                RQ.add(SR);
                ETNombreDeleteV.setText("");

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
            //Toast.makeText(this,"Cerrando sesion",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            mAuth.signOut();


            Intent intent = new Intent(this, MainActivity2.class);
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