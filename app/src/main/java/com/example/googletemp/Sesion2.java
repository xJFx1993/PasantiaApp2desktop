package com.example.googletemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Sesion2 extends AppCompatActivity {

    private Button BIngresar ;

    private EditText M;
    private EditText P;

    private String email="";
    private String pass="";

    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    //SQL
    RequestQueue RQ;

    private static final String TAG = "CustomAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion2);

        Vistas();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth =  FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);


        //utilizarlo para blanquear los campos cada que se navega

        M.setText("registroadmin@gmail.com");
        P.setText("123456789");
        /*
        M.setText("Mailgenericoicfes@gmail.com");
        P.setText("123456");
*/

    }

    public void Ingresar (View r){

        email= M.getText().toString();
        pass=  P.getText().toString();

        if(!email.isEmpty() && !pass.isEmpty()){
            LoginU();
        }
        else
        {Toast.makeText(Sesion2.this,"Completar campos",Toast.LENGTH_SHORT).show();}

        /*
        //Por ahora la sesion predeterminada es estudiante
        //Con un IF enviarlo al metodo correspondiente
        //averiguamos quien inicio y lo mandamos a su pantalla
        Intent i = new Intent(this, SesionEstudiante.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();

*/
        /*
        *    Intent intent = new Intent(this, BigG.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
        *
        * */


    }

    //Metodo para iniciar la sesion
    private void LoginU() {

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateUI(mAuth.getCurrentUser().getUid());
                }else{
                    Toast.makeText(Sesion2.this,"No se pudo iniciar sesion",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(String identificador_F) {
        //actualizamos la vista
        ReadUser(identificador_F);
    }

    public void IngresarEst (){
        Intent i = new Intent(this, SesionEstudiante.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();
    }

    public void IngresarAdm (){
        Intent i = new Intent(this, SesionAdministrador.class);
        startActivity(i);
    }

    public void IngresarEnc (){

        Intent i = new Intent(this, SesionEncargado.class);
        startActivity(i);


    }

    public void RPass(View r){
        Intent i = new Intent(this, RecuperarPass.class);
        startActivity(i);
    }


    private void Vistas() {

        BIngresar= (Button)findViewById(R.id.BIngresar);

        P= (EditText)findViewById(R.id.Tsesion2Pass);
        M= (EditText)findViewById(R.id.Tsesion2Mail);

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
                        int rol;
                        try {
                            rol = response.getInt("Rol");


                            if (rol == 1){
                                IngresarEst();
                            }else if (rol == 2){
                                IngresarEnc();
                            }else if (rol == 3){
                                IngresarAdm();
                            }else if (rol == 4){
                                //para llevarlo a una vista profesor
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
        RQ.add(jsOR);

    }

}