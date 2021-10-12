package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.Collections;

public class CargarPrueba extends AppCompatActivity {

    EditText ETCodigoV=null;
    ListView LVCargarPruebasV=null;

    private FirebaseAuth mAuth;
    RequestQueue RQ;
    int ID_BD=0;
    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_prueba);

        vistas();

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //tenemos el id del usuario de la base de datos
        ID_BD=ReadUser(mAuth.getCurrentUser().getUid());



        //Logida del programa
        //Cada que se presione
        LVCargarPruebasV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //T1.setText("La edad de "+ L1.getItemAtPosition(position)) + " es " + edades[position]);
               // T1.setText("La edad de "+ L1.getItemAtPosition(position).toString() + " es " + edades[position] + " años");

                //Toast.makeText(this,"Valor BD ", Toast.LENGTH_SHORT).show();
                message(position);

            }
        });

        ETCodigoV.setEnabled(true);
       // ETCodigoV.onFinishTemporaryDetach();
    }

    private void message(int position) {

       // Toast.makeText(this,"Valor BD " + LVCargarPruebasV.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        //Algoritmo que averigue el codigo del texto presionado
       String codigo= TExtoPresionado(LVCargarPruebasV.getItemAtPosition(position).toString());
        //con ese codigo hacemos la consulta para el nombre
        consultaParaElnombre(codigo);

    }

    private void consultaParaElnombre(String codigo) {
        //Request
        //Aqui abrimos la nueva activity
        //EL estudiante quiere resolver la prueba con id codigo
            String URLBuscar ="http://192.168.0.100/android/prueba/fetch2.php?id="+ codigo;
            JsonObjectRequest jsOR = new JsonObjectRequest(
                    Request.Method.GET,
                    URLBuscar,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!(response.equals("Not found any rows"))){

                                //enviamos los valores a la nueva activity
                                //llamamos un metodo y enviamos los valores ala nueva activity
                                newACtityOpen(response);
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



    }

    //Abrimos la activity y le enviamos los valores
    private void newACtityOpen(JSONObject response) {

        String Nombre = null;
        String Codigo = null;

        try {
            Nombre=response.getString("Nombre") ;
            Codigo=response.getString("id_prueba");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Nombre de la otra activity  SegundoAct
        Intent i = new Intent(this,Prueba.class);
        //Enviar parametros
        i.putExtra("Nombre", Nombre);
        i.putExtra("id_prueba", Codigo);

        startActivity(i);
    }

    private String TExtoPresionado(String toString) {

        String Aux = toString;

        String [] variable = new String[toString.length()];


        //Aqui esta el split, split del contenido del ListView
        for (int i=0;i<variable.length;i++){
            variable[i]= String.valueOf(Aux.charAt(i));

        }

        //encontrar el codigo
        return encontrandoCOdigo(variable);
    }

    private String encontrandoCOdigo(String[] variable) {
// es un for
        String Codigo=null;
        Codigo="";

        int Aux=0;

        for (int i=variable.length-1;i>0;i--){

            if (variable[i].equals(" ") && variable[i-1].equals(":") && variable[i-2].equals("o")&& variable[i-3].equals("g")&& variable[i-4].equals("i")&& variable[i-5].equals("d")&& variable[i-6].equals("o")&& variable[i-7].equals("C")){
                Aux=i;
                i=0;
            }

        }
        for (int i=Aux+1;i<variable.length;i++){
            Codigo=Codigo+variable[i];
        }
        return Codigo;
    }

    //Cargamos en el list view el Id que esta buscando
    private void message2(JSONObject response) {

        String [] variable2 = new String[1];
        String variable = null;

        try {
            variable2[0] = "Nombre: "+response.getString("Nombre") + "     Codigo: "+response.getString("id_prueba");
            variable = "Nombre: "+response.getString("Nombre") + "     Codigo: "+response.getString("id_prueba");

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // Toast.makeText(this,"Valor  " + variable2[0], Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,R.layout.list_item_opciones, Collections.singletonList(variable));
        //ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,R.layout.list_item_opciones,variable2);
        LVCargarPruebasV.setAdapter(Adapter);
    }

    private void CargarPruebas2(int id_bd, int idLV) {

        String URL = "http://192.168.0.100/android/prueba/fetch-P.php?id="+id_bd;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                NewActivity(response,idLV);
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

    //Aqui enviamos las variables a la nueva activity
    //Tenemos los IDs y nombres de las pruebas de un usuario, del usuario ID_BD
    private void NewActivity(JSONArray response,int idLV) {

        String [][] variable = new String[2][response.length()];
        String [] variable2 = new String[response.length()];

        for (int i =0 ; i<response.length();i++){

            try {
                //JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(i)));
                JSONObject objeto2 = new JSONObject(response.get(i).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");

               /* Pruebas.append(i+1 + " :");
                Pruebas.append("    Nombre prueba: "+objeto2.getString("Nombre"));
                // Pruebas.append("    Codigo: "+objeto2.getString("id_prueba"));
                Pruebas.append("\n");*/

                variable [0][i]=objeto2.getString("Nombre");
                variable [1][i]=objeto2.getString("id_prueba");

                variable2 [i] = "Nombre: "+objeto2.getString("Nombre") + "     Codigo: "+objeto2.getString("id_prueba");

                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,R.layout.list_item_opciones,variable2);

                LVCargarPruebasV.setAdapter(Adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //request buscar
    public void BuscarById(View r){
        if (ETCodigoV.length()!=0){
            int IDtemp=-1;
            //IDAux = Integer.parseInt(ETCodigoV.getText().toString());
            String idBusqueda = ETCodigoV.getText().toString();

            String URLBuscar ="http://192.168.0.100/android/prueba/fetch2.php?id="+ idBusqueda;
            JsonObjectRequest jsOR = new JsonObjectRequest(
                    Request.Method.GET,
                    URLBuscar,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!(response.equals("Not found any rows"))){
                                message2(response);
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

    //Aqui cargamos las pruebas en el list view
    //Tenemos los IDs y nombres de las pruebas de un usuario, del usuario ID_BD
    private void insertando_pruebas(JSONArray response) {

        String [][] variable = new String[2][response.length()];
        String [] variable2 = new String[response.length()];

        for (int i =0 ; i<response.length();i++){

            try {
                //JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(i)));
                JSONObject objeto2 = new JSONObject(response.get(i).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");

                variable [0][i]=objeto2.getString("Nombre");
                variable [1][i]=objeto2.getString("id_prueba");

                variable2 [i] = "Nombre: "+objeto2.getString("Nombre") + "     Codigo: "+objeto2.getString("id_prueba");



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,R.layout.list_item_opciones,variable2);
        LVCargarPruebasV.setAdapter(Adapter);
    }

    public void Call(View r) {
        CargarPruebas(ID_BD);
    }

    private void vistas() {
        LVCargarPruebasV = (ListView) findViewById(R.id.LVCargarPruebas);
        ETCodigoV = (EditText) findViewById(R.id.ETCodigo);
        ETCodigoV.setText("");

       // ETCodigoV.setEnabled(false);
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