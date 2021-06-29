package com.example.googletemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GenericLifecycleObserver;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RG extends AppCompatActivity {

    private TextView Tnombre;
    private TextView Tid;
    private TextView Ttexto;

    private Button Logout;
    private Button Revoke;

    private ImageView IV;
//
    private GoogleApiClient googleApliClient;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_g);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Tnombre = (TextView)findViewById(R.id.TnombreR);
        Ttexto = (TextView)findViewById(R.id.TmailR);
        Tid = (TextView)findViewById(R.id.TidR);

        Logout = (Button)findViewById(R.id.button2);
        Revoke = (Button)findViewById(R.id.button3);

        IV = (ImageView)findViewById(R.id.imageView);

        Tnombre.setText(user.getDisplayName());
        Ttexto.setText( user.getEmail());




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //quisque inicio silencioso
      /*  OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApliClient);

        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleresult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleresult(googleSignInResult);
                }
            });
        }*/
    }

    private void handleresult(GoogleSignInResult result) {
        //peticion silenciosa
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            //ya podemos acceder a los datos

            Tnombre.setText(account.getDisplayName());
            Ttexto.setText( account.getEmail());
            Tid.setText(account.getId());
/*
            Logout = (Button)findViewById(R.id.button2);
            Revoke = (Button)findViewById(R.id.button3);

            IV = (ImageView)findViewById(R.id.imageView);*/

        }else{
            GoLoginScreen();
        }
    }

    private void GoLoginScreen() {

        Intent intent = new Intent(this, BigG.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }






}