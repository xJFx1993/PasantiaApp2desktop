package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class Preguntas extends AppCompatActivity {


    //InsertarPreguntas es el metodo que realmente ahi que trabajar
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

    int CountM=0;

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

    /*    Toast.makeText(this,"Valor NombreP " + NombreP, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Valor IDP " + IDP, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Valor modulo " + modulo, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Valor CountGS " + CountGS, Toast.LENGTH_SHORT).show();*/


        //InsertarPreguntas es el metodo que realmente ahi que trabajar
        //ni se que hace esto para que count M , contar modulo que, alguna variable que cuenta algo
        //linea

        //CountM = metodoCountPreMod(modulo,IDP);
        //el que nos interesa aparentemente es "metodo"
        //linea

        //Var = metodo(IDP, Integer.parseInt(modulo));
        //Almacena las preguntas [][]
        //al final el almacenacmiento en Var no va a ser muy util

        metodo2(IDP, Integer.parseInt(modulo));



        CreandoBoton();

        CreandoBoton();
        CreandoBoton();

    }

    private void CreandoBoton() {



        //creacion de los text view al menos meter los enunciados
        Button BAuxD = new Button(this);
       // TextView TVDinamico = new TextView(this);
    //    TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        BAuxD.setText("BAuxD");
               // BAuxD.setId(2);
        BAuxD.setTextColor(Color.BLACK);

        BAuxD.setOnClickListener(new ButtonsOnClickListener(BAuxD.getId()));

        LYinterno.addView(BAuxD);

    }

    class ButtonsOnClickListener implements View.OnClickListener{
        public ButtonsOnClickListener(int numButton) {
            this.numButton = numButton;
        }

        int numButton;

        @Override
        public void onClick(View v) {

            Button b = (Button) v;

            Toast.makeText(getApplicationContext(),"Pulsado " +  b.getText(),Toast.LENGTH_SHORT).show();

            String mensaje="";
            if (numButton%2==0)
                mensaje="Boton PAR "+String.format("%02d", numButton );
            else
                mensaje="Boton IMPAR "+String.format("%02d", numButton );

            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();

        }
    }


    private int metodoCountPreMod(String modulo, String idp) {

        String URl22 ="http://192.168.0.100/android/relacionpruebapregunta/count-P-M.php?id="+IDP+"&Mo="+modulo;
        JsonObjectRequest jsOR = new JsonObjectRequest(
                Request.Method.GET,
                URl22,
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
                            CountM=aux;
                            //count2[0] =aux;

                            //aqui es ddonde viene la logica de reparar
                         /*   if(CountG!=50){ // response.getInt("count(*)")!=50 // CountG!=50
                                //botones disable y reparar
                                reparar();
                            }*/

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

        return CountM;


    }

    private void vistas() {


        LYV = (LinearLayout) findViewById(R.id.LYPreguntas);
        LYinterno = (LinearLayout) findViewById(R.id.LYinterno);


        //temp Toast.makeText(this,"Valor  N " + NombreP, Toast.LENGTH_SHORT).show();
        //  Toast.makeText(this,"Valor ID " + IDP, Toast.LENGTH_SHORT).show();


    }

    //InsertarPreguntas es el metodo que realmente ahi que trabajar
    private String[][] metodo(String IDP, int Modulo) {

        //& Caracter para concatenar

        //elige/atrapa las preguntas de una prueba
        String URl2 ="http://192.168.0.100/android/Pregunta/CargarStringArregloPreguntasModulo.php?id="+IDP+"&Mo="+Modulo;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URl2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //insertando_pruebas(response); en la variable
                Var = DatosResponse(response);
                InsertarPreguntas(response);
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

    //InsertarPreguntas es el metodo que realmente ahi que trabajar
    private void metodo2(String IDP, int Modulo) {

        //& Caracter para concatenar

        //elige/atrapa las preguntas de una prueba
        String URl2 ="http://192.168.0.100/android/Pregunta/CargarStringArregloPreguntasModulo.php?id="+IDP+"&Mo="+Modulo;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URl2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //insertando_pruebas(response); en la variable
                Var = DatosResponse(response);
                InsertarPreguntas(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(jsonArrayRequest);


    }

    //InsertarPreguntas es el metodo que realmente ahi que trabajar
    //insertar las preguntas en el layout vertical
    private void InsertarPreguntas(JSONArray response) {
        //creacion de objetos dinamicos
        //aqui dentro van los if

        for (int i1 =0 ; i1<response.length();i1++){

            int tipoPregunta=-1;
            String SuperEnun, Enun = null;
            int IDPregunta=-1;
            int IDP=-1;

            try {
                //JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(i)));
                JSONObject objeto2 = new JSONObject(response.get(i1).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");

                IDP = Integer.parseInt(objeto2.getString("id_pregunta"));
                SuperEnun = objeto2.getString("Super_enunciado");
                Enun = objeto2.getString("Enunciado");
                // Aux[3][i] = objeto2.getString("modulo");
                // Aux[4][i] = objeto2.getString("Tipo_pregunta");

                tipoPregunta= Integer.parseInt(objeto2.getString("Tipo_pregunta"));


                String URlNum ="http://192.168.0.100/android/Pregunta/countimagenesPregunta.php?id="+IDP;

                int finalTipoPregunta = tipoPregunta;
                int finalIDP = IDP;
                String finalEnun = Enun;
                int finalI = i1;
                JsonObjectRequest jsORNum = new JsonObjectRequest(
                        Request.Method.GET,
                        URlNum,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                int aux=-1;
                                try {

                                    // ID_BD = response.getInt("Id_persona");
                                    //return ID_BD;
                                    //entrnadoenelresponse(response);
                                    //CargarPruebas(ID_BD);
                                    //count(*)
                                    aux =response.getInt("count(*)");
                                    //count2[0] =aux;



                                    if (finalTipoPregunta ==0){
                                        //individual, simple, sencilla
                                        logicasencilla(finalIDP, finalEnun, finalI,aux);
                                    }else{
                                        logicaCompuesta(finalIDP,SuperEnun, finalEnun, finalI,aux);
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
                RequestQueue requestQueueNum = Volley.newRequestQueue(this);
                requestQueueNum.add(jsORNum);





            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        CreandoBoton();

        CreandoBoton();
        CreandoBoton();

    }

    private void logicaCompuesta(int idp, String superEnun, String enun, int i1,int V) {
        //Esto retorna el numero de imagenes por pregunta, deberia


        if (V ==0){//no tiene imagenes
            Logica_sin_imagenesC(idp,superEnun,enun,V,i1);
        }else if (V ==-1){
            Toast.makeText(this,"COmpuesta el valor esta siendo -1 : " + V, Toast.LENGTH_SHORT).show();
        }else{
            Logica_con_imagenesC(idp,superEnun,enun,V,i1);
        }

    }


    private void logicasencilla(int idp, String enun, int i1,int V) {

        //Esto retorna el numero de imagenes por pregunta, deberia


        if (V ==0){//no tiene imagenes
            Logica_sin_imagenesS(idp,enun,V,i1);
        }else if (V ==-1){
            Toast.makeText(this,"Sencilla el valor esta siendo -1 : " + V, Toast.LENGTH_SHORT).show();
        }else{
            Logica_con_imagenesS(idp,enun,V,i1);
        }

    }

    private void Logica_con_imagenesC(int idp, String superEnun, String enun, int v, int i1) {
        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText(enun);
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamico);
    }

    private void Logica_sin_imagenesC(int idp, String superEnun, String enun, int v, int i1) {

        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText(enun);
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamico);
    }



    private void Logica_con_imagenesS(int idp, String enun, int v, int i1) {
        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText(enun);
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamico);

    }

    private void Logica_sin_imagenesS(int idp, String enun, int v, int i1) {



        //if tipo_respuesta eso lo averiguamos desde la tabla respuesta
        /*if (tipo_respuesta ==0 ){
            Logia_Imagen();
        }else{
            LogicaTexto();
        }*/

        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText(enun);
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamico);



        CreandoBoton();




    }

    //Numero de imagenes que tiene esta pregunta
    //esto es un request
    //al final esto no termino funcionando
    private int NImagePRe(int idp) {


        final int[] NumReturn = {-1};
        String URlNum ="http://192.168.0.100/android/Pregunta/countimagenesPregunta.php?id="+idp;

        JsonObjectRequest jsORNum = new JsonObjectRequest(
                Request.Method.GET,
                URlNum,
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
                            //count2[0] =aux;
                            NumReturn[0] = aux;

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
        RequestQueue requestQueueNum = Volley.newRequestQueue(this);
        requestQueueNum.add(jsORNum);

        return  NumReturn[0];
    }

    //Aqui es doonde llenamos el arreglo (matriz)
    //Al final esta matriz como que no va a servir para nada
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

    public void imprimir(View r) {

        String [][] copia = Var.clone();
        //Toast.makeText(this,"tamaño arreglo "+ Var.length, Toast.LENGTH_SHORT).show();
        int z = -1;

        int Count=0;
        //Count = Metodocountconsulta();

        CountG= Integer.parseInt(CountGS);
        //CountM
        Toast.makeText(this,"Real tamaño arreglo "+ CountGS, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"tamaño arreglo por modulos "+ CountM, Toast.LENGTH_SHORT).show();


        try{

            for (int j = 0 ; j<CountM;j++){
                //length 5
                for (int i = 0 ; i<Var.length;i++){
                    z=j;
                    //Var[i][j]
                    //  Toast.makeText(this,"arreglo sobrepasado, "+ Var[i][j] + " limite", Toast.LENGTH_SHORT).show();


                    if(i==0){
                        Toast.makeText(this,
                                "id_pregunta, "+ Var[i][j] + " - "

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

    //request insert para almacenar las respuestas seleccionadas
    public void AlmacenarRepuestas(View r){

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