package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class Prueba extends AppCompatActivity {



    private FirebaseAuth mAuth;
    RequestQueue RQ;
    int ID_BD=0;
    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAnalytics mFirebaseAnalytics;

    String NombreP=null;
    String IDP=null;
    TextView TVV=null;

    ProgressBar PBpruebas=null;

    String [][] Var = null;

    int CountG=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        //Usando el dato enviado
         NombreP = getIntent().getStringExtra("Nombre");
         IDP = getIntent().getStringExtra("id_prueba");

        vistas();

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //tenemos el id del usuario de la base de datos
       // ID_BD=ReadUser(mAuth.getCurrentUser().getUid());
       Var = metodo(IDP);
        PBpruebas.setVisibility(View.INVISIBLE);

        Toast.makeText(this,"ID prueba "+ IDP, Toast.LENGTH_SHORT).show();

        //
        CountG= Metodocountconsulta();


    }

    public  void imprimir(View r) {

        String [][] copia = Var.clone();
        //Toast.makeText(this,"tamaño arreglo "+ Var.length, Toast.LENGTH_SHORT).show();
        int z = -1;

        int Count=0;
        Count = Metodocountconsulta();

        Toast.makeText(this,"Real tamaño arreglo "+ CountG, Toast.LENGTH_SHORT).show();

             /*  for (int i = 0 ; i<Var.length;i++){
                for (int j = 0 ; j<50;j++){
                        z=j;

                    //Var[i][j]
                    Toast.makeText(this,"arreglo sobrepasado, "+ z + " limite", Toast.LENGTH_SHORT).show();

                }

            }*/

        try{

            for (int j = 0 ; j<CountG;j++){
                //length 5
                for (int i = 0 ; i<Var.length;i++){
                    z=j;
                    //Var[i][j]
                  //  Toast.makeText(this,"arreglo sobrepasado, "+ Var[i][j] + " limite", Toast.LENGTH_SHORT).show();


                    if(i==0){
                        Toast.makeText(this,
                                "id_prueba, "+ Var[i][j] + " - "

                                , Toast.LENGTH_SHORT).show();
                    }else if (i==3){
                        Toast.makeText(this,
                                "modulo, "+ Var[i][j] + " - "

                                , Toast.LENGTH_SHORT).show();

                    }else if (i==4){
                        Toast.makeText(this,
                                "tipo_pregunta, "+ Var[i][j] + " - "

                                , Toast.LENGTH_SHORT).show();

                    }

                }

            }

        }catch (Exception e){
            Toast.makeText(this,"arreglo sobrepasado, "+ z + " limite", Toast.LENGTH_SHORT).show();
        }



    }

    private int Metodocountconsulta() {
        final int[] count = {0};
        final int[] count2 = {0};
        String URLCount ="http://192.168.0.100/android/relacionpruebapregunta/count.php?id="+IDP;


        JsonObjectRequest jsOR = new JsonObjectRequest(
                Request.Method.GET,
                URLCount,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            int aux=0;
                        try {

                           // ID_BD = response.getInt("Id_persona");
                            //return ID_BD;
                            //entrnadoenelresponse(response);
                            //CargarPruebas(ID_BD);
                            //count(*)
                            aux =response.getInt("count(*)");
                            CountG=aux;
                            count2[0] =aux;

                            //aqui es ddonde viene la logica de reparar
                            if(CountG!=50){
                                //botones disable y reparar
                                reparar();
                            }

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

        return CountG;
        //return count2[0];
        //return aux;
        //return CountG;
    }

    private void reparar() {
    }

    private String[][] metodo(String IDP) {

        //elige/atrapa las preguntas de una prueba
        String URl2 ="http://192.168.0.100/android/Pregunta/CargarStringArregloPreguntas.php?id="+IDP;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URl2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //insertando_pruebas(response); en la variable
                Var = DatosResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(jsonArrayRequest);

        return Var;
    }

    //Aqui es doonde llenamos el arreglo (matriz)
    private String[][] DatosResponse(JSONArray response) {

        String[][] Aux = new String[5][response.length()];

        for (int i =0 ; i<response.length();i++){

            try {
                //JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(i)));
                JSONObject objeto2 = new JSONObject(response.get(i).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");

               /* Pruebas.append(i+1 + " :");
                Pruebas.append("    Nombre prueba: "+objeto2.getString("Nombre"));
                // Pruebas.append("    Codigo: "+objeto2.getString("id_prueba"));
                Pruebas.append("\n");*/
                Aux[0][i] = objeto2.getString("id_pregunta");
                Aux[1][i] = objeto2.getString("Super_enunciado");
                Aux[2][i] = objeto2.getString("Enunciado");
                Aux[3][i] = objeto2.getString("modulo");
                Aux[4][i] = objeto2.getString("Tipo_pregunta");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return Aux;
    }

    private void vistas() {
        TVV = (TextView) findViewById(R.id.TVnombrePrueba);

       //temp Toast.makeText(this,"Valor  N " + NombreP, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this,"Valor ID " + IDP, Toast.LENGTH_SHORT).show();

        TVV.setText(NombreP);

        PBpruebas = (ProgressBar) findViewById(R.id.PGobj);

        PBpruebas.setVisibility(View.VISIBLE);
    }

    public void Mat (View r){
        //Preguntas
        Intent i = new Intent(this, Preguntas.class);
        i.putExtra("NombreP", NombreP);
        i.putExtra("id_prueba", IDP);
        //CountG
        i.putExtra("modulo", "1");
       // String CGS= String.valueOf(CountG);
        i.putExtra("CountG", String.valueOf(CountG));
        //i.putExtra("Var", Var);

        startActivity(i);

        /*
           //Nombre de la otra activity  SegundoAct
        Intent i = new Intent(this,Prueba.class);
        //Enviar parametros
        i.putExtra("Nombre", Nombre);
        i.putExtra("id_prueba", Codigo);

        startActivity(i);


        */
    }

    public void Lectura (View r){

        Intent i = new Intent(this, Preguntas.class);
        i.putExtra("NombreP", NombreP);
        i.putExtra("id_prueba", IDP);
        i.putExtra("modulo", "2");
        i.putExtra("CountG",  String.valueOf(CountG));
        // i.putExtra("Var", Var);
        startActivity(i);
    }

    public void SC (View r){
        Intent i = new Intent(this, Preguntas.class);
        i.putExtra("NombreP", NombreP);
        i.putExtra("id_prueba", IDP);
        i.putExtra("modulo", "3");
        i.putExtra("CountG",  String.valueOf(CountG));
        // i.putExtra("Var", Var);
        startActivity(i);
    }

    public void CN (View r){
        Intent i = new Intent(this, Preguntas.class);
        i.putExtra("NombreP", NombreP);
        i.putExtra("id_prueba", IDP);
        i.putExtra("modulo", "4");
        i.putExtra("CountG",  String.valueOf(CountG));
        // i.putExtra("Var", Var);
        startActivity(i);
    }

    public void Ingles (View r){
        Intent i = new Intent(this, Preguntas.class);
        i.putExtra("NombreP", NombreP);
        i.putExtra("id_prueba", IDP);
        i.putExtra("modulo", "5");
        i.putExtra("CountG",  String.valueOf(CountG));
        // i.putExtra("Var", Var);
        startActivity(i);
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