package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Nickname extends AppCompatActivity {

    //creacion varaibleas las mismas que en la vista

    String mail="";

    private TextView variableTantiguonick;
    private TextView variableTnuevonick;
    private Button Variablebuttonmodificar;
    private FirebaseAuth mAuth;
    RequestQueue RQ;
    //
    String x="";
    String x2="";
    //
    String x3="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        Vistas();

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);

        mail = mAuth.getCurrentUser().getEmail();
        ReadUser(mAuth.getCurrentUser().getUid());
int x =0;


//
    }

    public void Ingresar (View r){
        //Validar cual es la sesion a mostrar por ahora predeterminada estudiante
        Intent i = new Intent(this, SesionEstudiante.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);

    }



    public void IngresarEst (View r){
        Intent i = new Intent(this, SesionEstudiante.class);
        startActivity(i);
        finish();
    }

    public void IngresarAdm (View r){
        Intent i = new Intent(this, SesionAdministrador.class);
        startActivity(i);
        finish();
    }

    public void IngresarEnc (View r){

        Intent i = new Intent(this, SesionEncargado.class);
        startActivity(i);
        finish();


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
                        String requestnick;
                        try {
                            //rol = response.getInt("Rol");
                            requestnick = response.getString("Nombre");
                            String aux = "Antiguo nombre:";
                            requestnick= aux + " " + requestnick;


                            variableTantiguonick.setText(requestnick);



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

    public void Vistas(){


        variableTantiguonick = (TextView)findViewById(R.id.TAntiguoNick);
        variableTnuevonick = (TextView)findViewById(R.id.TNewnickname);
        Variablebuttonmodificar = (Button)findViewById(R.id.modificarN);

    }

}