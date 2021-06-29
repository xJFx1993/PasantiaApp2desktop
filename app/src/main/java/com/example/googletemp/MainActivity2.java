package com.example.googletemp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class MainActivity2 extends AppCompatActivity {

    private Button BIniciarSesion;

    private SignInButton GlobalSignInB;

    private GoogleApiClient googleApliClient;

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
        GlobalSignInB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Esuchando el boton
                Ingresar(null);
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();




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
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());


                /*
                  Tnombre.setText(account.getDisplayName());
            Ttexto.setText( account.getEmail());
            Tid.setText(account.getId());
                 */
                Toast.makeText(MainActivity2.this,account.getDisplayName(),Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity2.this,account.getEmail(),Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity2.this,account.getId(),Toast.LENGTH_SHORT).show();


             //  firebaseAuthWithGoogle(account.getIdToken());
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


}