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

    int Numeracion = 1; // para controlar el for e imprimir el numero de preguntas correctamente , en orden 1 2 3 ...

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
       TExtviewModulo(modulo);

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



      /*  CreandoBoton();

        CreandoBoton();
        CreandoBoton();*/

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
        //y aqui dentro se hace otra consulta, un count de imagenes por pregunta, numero de imagenes desde 0 , 1, 2



        for (int i1 =0 ; i1<response.length();i1++){

            int tipoPregunta=-1;
            String SuperEnun, Enun = null;
            int IDPregunta=-1;
            int IDPre=-1;

            try {
                //JSONObject objeto = new JSONObject(String.valueOf(response.getJSONObject(i)));
                JSONObject objeto2 = new JSONObject(response.get(i1).toString());
                //ArregloPreguntasIds[JF] = objeto2.getString("id_pregunta");

                IDPre = Integer.parseInt(objeto2.getString("id_pregunta"));
                SuperEnun = objeto2.getString("Super_enunciado");
                Enun = objeto2.getString("Enunciado");
                // Aux[3][i] = objeto2.getString("modulo");
                // Aux[4][i] = objeto2.getString("Tipo_pregunta");

                tipoPregunta= Integer.parseInt(objeto2.getString("Tipo_pregunta"));


                String URlNum ="http://192.168.0.100/android/Pregunta/countimagenesPregunta.php?id="+IDPre;

                int finalTipoPregunta = tipoPregunta;
                int finalIDP = IDPre;
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


                                    //aqui empezaria lo que vendria siendo el arbol de IFs
                                    if (finalTipoPregunta ==0){
                                        //individual, simple, sencilla
                                        logicasencilla(finalIDP, finalEnun, finalI,aux,Numeracion);
                                    }else{
                                        logicaCompuesta(finalIDP,SuperEnun, finalEnun, finalI,aux,Numeracion);
                                    }

                                    Numeracion=Numeracion+1;

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

       /* CreandoBoton();

        CreandoBoton();
        CreandoBoton();*/

    }

    private void logicaCompuesta(int idpre, String superEnun, String enun, int i1,int V , int Numeracion) {
        //Esto retorna el numero de imagenes por pregunta, deberia
        //en V tenemos el valor de imagenes por pregunta


        if (V ==0){//no tiene imagenes
            Logica_sin_imagenesC(idpre,superEnun,enun,V,i1,Numeracion);
        }else if (V ==-1){
            Toast.makeText(this,"COmpuesta el valor esta siendo -1 : " + V, Toast.LENGTH_SHORT).show();
        }else{
            Logica_con_imagenesC(idpre,superEnun,enun,V,i1,Numeracion);
        }

    }


    private void logicasencilla(int idpre, String enun, int i1,int V, int Numeracion) {

        //Esto retorna el numero de imagenes por pregunta, deberia


        if (V ==0){//no tiene imagenes
            Logica_sin_imagenesS(idpre,enun,V,i1,Numeracion);
        }else if (V ==-1){
            Toast.makeText(this,"Sencilla el valor esta siendo -1 : " + V, Toast.LENGTH_SHORT).show();
        }else{
            Logica_con_imagenesS(idpre,enun,V,i1,Numeracion);
        }

    }

    private void Logica_con_imagenesC(int idpre, String superEnun, String enun, int v, int i1, int Numeracion) {
       //super enunciado
        TextView TVDinamicoSE = new TextView(this);
        TVDinamicoSE.setMovementMethod(new ScrollingMovementMethod());

        TVDinamicoSE.setText((Numeracion) + ". " + superEnun);
        TVDinamicoSE.setId(i1);
        TVDinamicoSE.setTextColor(Color.BLACK);


        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText(enun + " \n");
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamicoSE);
        LYinterno.addView(TVDinamico);

        Numeracion++;
    }

    private void Logica_sin_imagenesC(int idpre, String superEnun, String enun, int v, int i1, int Numeracion) {
        //super enunciado
        TextView TVDinamicoSE = new TextView(this);
        TVDinamicoSE.setMovementMethod(new ScrollingMovementMethod());

        TVDinamicoSE.setText((Numeracion) + ". " + superEnun);
        TVDinamicoSE.setId(i1);
        TVDinamicoSE.setTextColor(Color.BLACK);

        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText(enun + " \n");
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamicoSE);
        LYinterno.addView(TVDinamico);

        Numeracion++;
    }



    private void Logica_con_imagenesS(int idpre, String enun, int v, int i1, int Numeracion) {
        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText((Numeracion) + ". " + enun + " \n");
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamico);
        Numeracion++;

    }

    private void Logica_sin_imagenesS(int idpre, String enun, int v, int i1, int Numeracion) {
        //if tipo_respuesta eso lo averiguamos desde la tabla respuesta
        /*if (tipo_respuesta ==0 ){
            Logia_Imagen();
        }else{
            LogicaTexto();
        }*/

        //creacion de los text view al menos meter los enunciados
        TextView TVDinamico = new TextView(this);
        TVDinamico.setMovementMethod(new ScrollingMovementMethod());

        TVDinamico.setText((Numeracion) + ". " + enun + " \n");
        TVDinamico.setId(i1);
        TVDinamico.setTextColor(Color.BLACK);

        LYinterno.addView(TVDinamico);

        Numeracion++;

        //CreandoBoton();




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

    //textview del modulo
    private void TExtviewModulo(String modulo) {

        //Integer.parseInt(modulo)
        TextView TVDinamicoM = new TextView(this);
        TVDinamicoM.setMovementMethod(new ScrollingMovementMethod());

        int Test = Integer.parseInt(modulo);
        switch (Test)
        {
            case  1:
            //Ejecuta la acción cuando Test=1
                //creacion de los text view al menos meter los enunciados

                TVDinamicoM.setText("Matematicas \n");
                TVDinamicoM.setId(0);
                TVDinamicoM.setTextColor(Color.BLACK);
                TVDinamicoM.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                LYinterno.addView(TVDinamicoM);
            break;
            case 2:
            //Ejecuta la acción cuando Test=2
                //creacion de los text view al menos meter los enunciados

                TVDinamicoM.setText("Lectura critica \n");
                TVDinamicoM.setId(0);
                TVDinamicoM.setTextColor(Color.BLACK);
                TVDinamicoM.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                LYinterno.addView(TVDinamicoM);
            break;
            case 3:
                //Ejecuta la acción cuando Test=3
                //creacion de los text view al menos meter los enunciados


                TVDinamicoM.setText("Sociales y ciudadanas \n");
                TVDinamicoM.setId(0);
                TVDinamicoM.setTextColor(Color.BLACK);
                TVDinamicoM.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                LYinterno.addView(TVDinamicoM);
                break;
            case 4:
                //Ejecuta la acción cuando Test=4
                //creacion de los text view al menos meter los enunciados


                TVDinamicoM.setText("Ciencias naturales \n");
                TVDinamicoM.setId(0);
                TVDinamicoM.setTextColor(Color.BLACK);
                TVDinamicoM.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                LYinterno.addView(TVDinamicoM);
                break;
            case 5:
                //Ejecuta la acción cuando Test=5
                //creacion de los text view al menos meter los enunciados


                TVDinamicoM.setText("Ingles \n");
                TVDinamicoM.setId(0);
                TVDinamicoM.setTextColor(Color.BLACK);
                TVDinamicoM.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                LYinterno.addView(TVDinamicoM);
                break;

            default://sin valor por default
                //Ejecuta la acción cuando Test tiene un valor distinto
                break;
        }

        // Integer.parseInt(modulo)

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