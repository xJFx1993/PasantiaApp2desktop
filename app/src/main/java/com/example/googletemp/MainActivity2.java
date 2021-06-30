package com.example.googletemp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private Button BIniciarSesion;

    private SignInButton GlobalSignInB;

    private GoogleApiClient googleApliClient;

    private FirebaseAuth mAuth;

    //SQL
    RequestQueue RQ;

    //pones la dirreccion Ip de tu computadora
    private static final String URL1= "http://192.168.0.100/android/persona/save.php";

    //constante
    public static final int SIGN_IN_CODE=666;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Vistas();

        mAuth = FirebaseAuth.getInstance();
        RQ= Volley.newRequestQueue(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        GlobalSignInB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                //un intent de lector
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApliClient);
                startActivityForResult(intent, SIGN_IN_CODE);*/
                signIn();

            }
        });

    }

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    // [START onactivityresult]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
               // Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());


                /*
                  Tnombre.setText(account.getDisplayName());
            Ttexto.setText( account.getEmail());
            Tid.setText(account.getId());
                 */
               /* Toast.makeText(MainActivity2.this,account.getDisplayName(),Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity2.this,account.getEmail(),Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity2.this,account.getId(),Toast.LENGTH_SHORT).show();*/

             //   Toast.makeText(MainActivity2.this," 1 " + account.getId(),Toast.LENGTH_SHORT).show();




               firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }
    // [END onactivityresult]

    public void IniciarSesion(View r){
        Intent i = new Intent(this, Sesion2.class);
        startActivity(i);

    }

    public void Registrarse(View r){
        Intent i = new Intent(this, Registrarse.class);
        startActivity(i);
    }

    public void RPass(View r){
        Intent i = new Intent(this, RecuperarPass.class);
        startActivity(i);
    }

    private void Vistas() {
        BIniciarSesion= (Button)findViewById(R.id.BIniciarSesion);
        GlobalSignInB = (SignInButton)findViewById(R.id.SignIn);


    }

    public void Ingresar (View r){

        //Por ahora la sesion predeterminada es estudiante
        //Con un IF enviarlo al metodo correspondiente
        //averiguamos quien inicio y lo mandamos a su pantalla
        Intent i = new Intent(this, SesionEstudiante.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
        finish();


    }

    public void IngresarEst (View r){
        Intent i = new Intent(this, SesionEstudiante.class);
        startActivity(i);
    }

    public void IngresarAdm (View r){
        Intent i = new Intent(this, SesionAdministrador.class);
        startActivity(i);
    }

    public void IngresarEnc (View r){

        Intent i = new Intent(this, SesionEncargado.class);
        startActivity(i);


    }

    public void registrar(View r){
        Intent i = new Intent(this, Registrar.class);
        startActivity(i);
    }

    public void login(View r){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");



                            createUser(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getEmail(),"",mAuth.getCurrentUser().getUid());
                            // (String vname, String vemail, String vpassword, String identificador_F)
                            Toast.makeText(MainActivity2.this," 2 " + mAuth.getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();


                           // updateUI(null);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //No se que es ese Snackbar
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(String identificador_F) {
        //llevarlo a la vista correcta
       /* if (currentUser != null){
            startActivity(new Intent(BigG.this,RG.class ));
            finish();

        };*/

      /*  if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity2.this,RG.class ));
            finish();

        };*/
        //actualizamos la vista
        ReadUser(identificador_F);

    }

    private void createUser(String vname, String vemail, String vpassword, String identificador_F) {
        //creamos el usuario en la base de datos
        StringRequest SR = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(MainActivity2.this, "Correct", Toast.LENGTH_SHORT).show();
                        updateUI(identificador_F);

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
                params.put ("name", vname);
                params.put ("email", vemail);
                params.put ("password", vpassword);
                params.put ("id_F", identificador_F);
                return params;
            }
        };

        RQ.add(SR);
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

}