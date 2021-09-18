package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import static java.lang.Boolean.TRUE;

public class CrearPrueba extends AppCompatActivity {

    EditText ETP1V=null;
    EditText EDTMV=null;
    EditText NombreP=null;
    Button CrearP;

    ProgressBar PB=null;

    Button BTP;

    //valor random
    int valorEnteroR=0;

    private FirebaseAuth mAuth;
    RequestQueue RQ;

    private RequestQueue rq;

    int ID_BD=0;

    //Maximos de las preguntas
    int Mm, Mlc, Msc , Mcn,Mi;

    //numero maximos de las preguntas por modulos
    int [] MaxPM = new int[5] ;

    //los numeros aleatorios de las preguntas para elegir
    int [] NR = new int[10] ;

    //necesito un array matriz que almacene todos los id's de las preguntas por modulo [5][N max]
    int [] ArregloPreguntasIds;

    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_prueba);

        mAuth = FirebaseAuth.getInstance();

        RQ= Volley.newRequestQueue(this);
        rq= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //tenemos el id del usuario de la base de datos
        ID_BD=ReadUser(mAuth.getCurrentUser().getUid());

        vistas();

        //PB.setVisibility(View.VISIBLE);
        PB.setVisibility(View.INVISIBLE);

    }



    // donde hacemos el insert a la tabla prueba
    //un request
    public void Creando_prueba(View r){

        String URL1 = "http://192.168.0.100/android/prueba/save.php";
        if (NombreP.length() !=0){
            PB.setVisibility(View.VISIBLE);
            for (int i = 0 ; i< MaxPM.length; i++){
                MaxPM[i]=-1;
            }
            //aqui va el request que hace el insert


            //creamos la prueba en la base de datos
            StringRequest SR = new StringRequest(
                    Request.Method.POST,
                    URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(Registrarse.this, "Correct", Toast.LENGTH_SHORT).show();
                            //updateUI(identificador_F);

                            //metodo consultar id prueba y mostrarlo en un toast y edittext
                            consultar_id_prueba();

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
                    params.put ("id_persona", String.valueOf(ID_BD));
                    params.put ("Nombre", NombreP.getText().toString());
                   return params;
                }
            };

            RQ.add(SR);

            PB.setVisibility(View.GONE);
            PB.setVisibility(View.INVISIBLE);

            Toast.makeText(this, NombreP.getText() +" Creado" ,Toast.LENGTH_SHORT).show();
            NombreP.setText("");



           // Toast.makeText(this,"ejecucion CreandoPrueba" + MaxPM[0],Toast.LENGTH_SHORT).show();
            //Traer en un arreglo todas las preguntas de la BD, solamente los ID (id_pregunta)
        }else{
            Toast.makeText(this,"Nombre prueba",Toast.LENGTH_SHORT).show();
            PB.setVisibility(View.GONE);
            PB.setVisibility(View.INVISIBLE);
        }

    }


    // donde consultamos el id de la prueeba insertada, una consulta
    //un request
    private void consultar_id_prueba() {

        String URL ="http://192.168.0.100/android/prueba/fetch-ID.php?id="+ID_BD;
        JsonObjectRequest jsOR = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String name, aux;
                        try {
                            //name = response.getString("Nombre");
                            /*aux= Globalname.getText().toString() ;
                            name = aux + " " +name;

                            Globalname.setText(name);*/
                            //ID_BD = response.getInt("Id_persona");
                            Creando_Modulos(response.getInt("id_prueba"));

                            //response.getInt("id_prueba")


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

    private void Creando_Modulos(int id_prueba) {
        //Toast.makeText(this,"Creando_Modulos id prueba " + id_prueba,Toast.LENGTH_SHORT).show();
       // ETP1V.setText(String.valueOf(id_prueba));

        //Tenemos el id de la prueba
        //creacion de modulos

        int i =1;
        int j =0;
        boolean bandera = true;

        //es el request el metodo que contiene el request
        //ValoresMaximo3(1,id_prueba);


            while ((i<=5) && TRUE){
            //es el request el metodo que contiene el request
                ValoresMaximo3(1,id_prueba);
                i=i+1;

            }
    }


    private void ValoresMaximo3(int i,int id_prueba) {
        //ejecutar y dejar termimnar una consulta con modulo 1 y 2

        //https
        //http

        //ArregloPsMaxRandModu
        String JSON_URL = "http://192.168.0.100/android/Prueba/ArregloPsMaxRandModu.php?i="+i;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


               // String tama= String.valueOf(response.length());
              //  EDTMV.setText("Arrary d emostrar que al menos esta entrando");
                //EDTMV.setText(tama);
                MaxPM[i-1]=response.length();
                ArregloPreguntasIds = new int[MaxPM[i-1]];

               creandoVariables(response,i,id_prueba);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        //Toast.makeText(this,"ejecucion" + MaxPM[0],Toast.LENGTH_SHORT).show();
        int asgasg=-1;
      /*  for (int a1=0 ;a1<ArregloPreguntasIds.length;a1++){
Toast.makeText(this,"asasf VE  "+ ArregloPreguntasIds[a1],Toast.LENGTH_SHORT).show();
        }*/

    }

    private void creandoVariables(JSONArray response, int i,int id_prueba) {
        // tengo el array de preguntas por modulo
        //al finalizar el ciclo For
        MaxPM[i-1] = response.length();
        ArregloPreguntasIds= new  int[MaxPM[i-1]];

        for (int JF=0 ; JF<response.length();JF++){

            try {
                JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(JF)));
                JSONObject objeto2 = new JSONObject(response.get(JF).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");
                String numetoS = objeto2.getString("id_pregunta");


                ArregloPreguntasIds[JF]= objeto2.getInt("id_pregunta");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        //Toast.makeText(this,"Lleega hasta creando variables los random faltan" ,Toast.LENGTH_SHORT).show();

        //vamos a generar los valores randon
        valores2(i,ArregloPreguntasIds,id_prueba);


       // Toast.makeText(this,"Estoy en el response" + MaxPM[0],Toast.LENGTH_SHORT).show();

       // ETP1V.setText(String.valueOf(ArregloPreguntasIds[0]));

    }

    //los valores random para elegir las preguntas
    private void valores2(int i, int[] ArregloPreguntasIds,int id_prueba) {
        //valor maximo MaxPM[i-1]
        NR= new int[10];
        //"bandera" para controlar el ciclo la variable x
        int x=0;

        //Toast.makeText(this,"estamos en el metodo random  ",Toast.LENGTH_SHORT).show();

        Boolean B2 = true;
        do {
            valorEnteroR = (int) Math.floor(Math.random()*(0-(ArregloPreguntasIds.length)+1)+(ArregloPreguntasIds.length));

            if (!(buscarRepetido(NR,valorEnteroR))){
                NR[x]=valorEnteroR;
                x= x +1;
            }
        }while (x<10);
        //Tenemos los diez numeros aleatorios para elegir el id
        //Toast.makeText(this,"estamos en el metodo random  " + NR[2],Toast.LENGTH_SHORT).show();

        //asociamos en insertamos
        int x2=0;
        String URL2 = "http://192.168.0.100/android/relacionpruebapregunta/save.php";
       while (x2 <10 ){

           //ArregloPreguntasIds[NR[x2]];


           //aqui va el request que hace el insert de loa relacion prueba pregunta


           //creamos la relacion en la base de datos
           int finalX = x2;
           StringRequest SR2 = new StringRequest(
                   Request.Method.POST,
                   URL2,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           //Toast.makeText(Registrarse.this, "Correct", Toast.LENGTH_SHORT).show();
                           //updateUI(identificador_F);

                          // message (response);
//                           consultar_id_prueba();

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
                   params.put ("id_prueba", String.valueOf(id_prueba));
                   params.put ("id_pregunta", String.valueOf(ArregloPreguntasIds[NR[finalX]]));
                   return params;
               }
           };

           RQ.add(SR2);



           x2=x2+1;

        }

    }


    public int ReadUser(String identificador_F){
        ID_BD = -1;
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
                            //name = response.getString("Nombre");
                            /*aux= Globalname.getText().toString() ;
                            name = aux + " " +name;

                            Globalname.setText(name);*/
                            ID_BD = response.getInt("Id_persona");

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
        return ID_BD;

    }

    private void vistas() {

       // CrearP= (Button)findViewById(R.id.btnCrearP);
        NombreP = (EditText) findViewById(R.id.editTextTextPersonName2);


               // ETP1V = (EditText) findViewById(R.id.ETP1);

       // EDTMV = (EditText) findViewById(R.id.EDTM);

        BTP= (Button)findViewById(R.id.BTP);

        PB= (ProgressBar)findViewById(R.id.PB);

        //un textview para que tenga scroll
        // TV1.append ("texto para agregar")
        //TV!.setMovementMethod (new ScrollingMovementMethod());

    }

    private boolean buscarRepetido(int[] nr, int valorEnteroR2) {
        for (int a1=0 ;a1<nr.length;a1++){
            if (nr[a1] == valorEnteroR2){
                return true;
            }
        }
        return false;
    }

}