package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Preguntas extends AppCompatActivity {

    LinearLayout LYV=null;
    LinearLayout LYinterno=null;

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
    String CountGS=null;

    String modulo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        //Usando el dato enviado
        NombreP = getIntent().getStringExtra("NombreP");
        IDP = getIntent().getStringExtra("id_prueba");
        modulo = getIntent().getStringExtra("modulo");
        CountGS = getIntent().getStringExtra("CountG");

        ;

        vistas();

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //tenemos el id del usuario de la base de datos
        // ID_BD=ReadUser(mAuth.getCurrentUser().getUid());
        //ID de la prueba IDP

        Toast.makeText(this,"Valor NombreP " + NombreP, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Valor IDP " + IDP, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Valor modulo " + modulo, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Valor CountGS " + CountGS, Toast.LENGTH_SHORT).show();

        Var = metodo(IDP, Integer.parseInt(modulo));
        //Almacena las preguntas [][]

    }

    private void vistas() {


        LYV = (LinearLayout) findViewById(R.id.LYPreguntas);
        LYinterno = (LinearLayout) findViewById(R.id.LYinterno);


        //temp Toast.makeText(this,"Valor  N " + NombreP, Toast.LENGTH_SHORT).show();
        //  Toast.makeText(this,"Valor ID " + IDP, Toast.LENGTH_SHORT).show();


    }

    private String[][] metodo(String IDP, int Modulo) {

        //& Caracter para concatenar

        //elige/atrapa las preguntas de una prueba
        String URl2 ="http://192.168.0.100/android/Pregunta/CargarStringArregloPreguntasModulo.php?id="+IDP+"&Mo="+Modulo;
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

    public  void imprimir(View r) {

        String [][] copia = Var.clone();
        //Toast.makeText(this,"tamaño arreglo "+ Var.length, Toast.LENGTH_SHORT).show();
        int z = -1;

        int Count=0;
        //Count = Metodocountconsulta();

        CountG= Integer.parseInt(CountGS);
        Toast.makeText(this,"Real tamaño arreglo "+ CountGS, Toast.LENGTH_SHORT).show();

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