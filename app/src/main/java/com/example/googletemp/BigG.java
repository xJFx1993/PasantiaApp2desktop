package com.example.googletemp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
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

public class BigG extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;

    private GoogleApiClient googleApliClient;

    private SignInButton GlobalSignInB;



    //constante
    public static final int SIGN_IN_CODE=666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_g);

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

        mAuth = FirebaseAuth.getInstance();

        ///



        GlobalSignInB = (SignInButton)findViewById(R.id.SignIn);

        //custom del boton google
        /*
        GlobalSignInB.setSize(SignInButton.SIZE_WIDE);
        GlobalSignInB.setColorScheme(SignInButton.COLOR_DARK);
*/
       googleApliClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
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

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        
        
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
                Toast.makeText(BigG.this,account.getDisplayName(),Toast.LENGTH_SHORT).show();
                Toast.makeText(BigG.this,account.getEmail(),Toast.LENGTH_SHORT).show();
                Toast.makeText(BigG.this,account.getId(),Toast.LENGTH_SHORT).show();




                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }
    // [END onactivityresult]
    private void updateUI(FirebaseUser currentUser) {
        //llevarlo a la vista correcta
       /* if (currentUser != null){
            startActivity(new Intent(BigG.this,RG.class ));
            finish();

        };*/

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(BigG.this,RG.class ));
            finish();

        };

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //No se que es ese Snackbar
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

/*
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if(resultCode==SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //manipulamos resuultado
            HandleResult(result);
        }
    }*/

    private void HandleResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            goMainScreen();

        }else{
            Toast.makeText(BigG.this,"No se pudo iniciar sesion",Toast.LENGTH_SHORT).show();
        }
    }

    //Inicia la activity y limpia registros para que no quede una detras de la otra
    private void goMainScreen() {
        Intent intent = new Intent(this, RG.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }

    //Metodo para cambiar
    public void Cambiar (View view){
        //Nombre de la otra activity  SegundoAct
        Intent i = new Intent(this,Login.class);
        startActivity(i);

    }
}