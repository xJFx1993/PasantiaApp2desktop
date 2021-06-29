package com.example.googletemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText T;
    private EditText P;
    private Button B;

    private String email="";
    private String pass="";

    private FirebaseAuth mAuth;

    private static final String TAG = "CustomAuthActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

          T =(EditText)findViewById(R.id.T1);
          P=(EditText)findViewById(R.id.P1);
          B=(Button)findViewById(R.id.B1);

        mAuth =  FirebaseAuth.getInstance();

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= T.getText().toString();
                pass=  P.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    LoginU();

                }
                else
                {
                    Toast.makeText(Login.this,"Complete los campos",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    //Metodo para iniciar la sesion
    private void LoginU(){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //para movernos de activity
                    startActivity(new Intent(Login.this,SignOut.class ));
                    Log.d(TAG, "signInWithCustomToken:success");
                    finish();
                }else{
                    Toast.makeText(Login.this,"No se pudo iniciar sesion",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(Login.this,SignOut.class ));
            finish();

        }

    }
}