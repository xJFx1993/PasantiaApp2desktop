package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ActualizarPrueba extends AppCompatActivity {

    TextView Pruebas=null;
    private FirebaseAuth mAuth;
    RequestQueue RQ;

    EditText Codigo=null;
    EditText nombre=null;

    Button BnombreUpdateV=null;

    int IDAux = -1;


    int ID_BD=0;

    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_prueba);

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        vistas();

        //tenemos el id del usuario de la base de datos
        ID_BD=ReadUser(mAuth.getCurrentUser().getUid());
        //Toast.makeText(this,"Valor BD "+ ID_BD, Toast.LENGTH_SHORT).show();
    }

    //request actualizar
    public void Update(View r){
        if (nombre.length()!=0){
            //IDAux Aqui esta el Id que queremos modificarle el nombre

            //Update succesfully
            //campo actualizado
            //Hacer algun call

           //Toast.makeText(this,"ID auxiliar "+ IDAux, Toast.LENGTH_SHORT).show();
            updateUser(nombre.getText().toString(), String.valueOf(IDAux));

        }

    }

    private void updateUser(String name,  String ID) {

        String URL ="http://192.168.0.100/android/prueba/edit-N.php";
        StringRequest SR = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity2.this,"Update successfully", Toast.LENGTH_SHORT).show();
                        if (response.equals("Update succesfully")){
                            nombre.setEnabled(false);
                            nombre.setText("");
                            //nombre.setEnabled(true);
                            BnombreUpdateV.setEnabled(false);

                            Call(null);
                        }
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
                Map<String,String> params = new HashMap<>();
                params.put("id", ID);
                params.put("name", name);
                return params;
            }
        };
       //RQ.add(SR);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(SR);
    }

    //request buscar
    public void BuscarById(View r){
        if (Codigo.length()!=0){
            int IDtemp=-1;
            IDAux = Integer.parseInt(Codigo.getText().toString());
            String idBusqueda = Codigo.getText().toString();

            String URLUpD ="http://192.168.0.100/android/prueba/fetch.php?id="+ idBusqueda;
            JsonObjectRequest jsOR = new JsonObjectRequest(
                    Request.Method.GET,
                    URLUpD,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                nombre.setText(response.getString("Nombre"));
                                //IDtemp = response.getInt("id_prueba");

                                if (nombre.getText().length()!=0){
                                    nombre.setEnabled(true);
                                    BnombreUpdateV.setEnabled(true);
                                }else if (response.equals("Not found any rows")){
                                        nombre.setEnabled(false);
                                        nombre.setText("");
                                        //nombre.setEnabled(true);
                                        BnombreUpdateV.setEnabled(false);
                                    }





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            nombre.setEnabled(false);
                            nombre.setText("");
                            //nombre.setEnabled(true);
                            BnombreUpdateV.setEnabled(false);
                        }
                    }

            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsOR);
            // RQ.add(jsOR);


        }else{
            Toast.makeText(this,"Faltan datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void vistas() {

        Pruebas = (TextView) findViewById(R.id.TVUpdate);
        Pruebas.setMovementMethod (new ScrollingMovementMethod());

        Codigo = (EditText) findViewById(R.id.ETIDUpdate);
        nombre = (EditText) findViewById(R.id.ETNombreUpdate);

        BnombreUpdateV = (Button) findViewById(R.id.BNombreUpdate);

        nombre.setEnabled(false);
        //nombre.setEnabled(true);
        BnombreUpdateV.setEnabled(false);
        Pruebas.setText("");

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