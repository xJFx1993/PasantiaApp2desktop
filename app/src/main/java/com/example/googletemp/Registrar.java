package com.example.googletemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;


public class Registrar extends AppCompatActivity {


    private TextView Tname;
    private TextView Tmail;
    private TextView Tpass;
    private Button Bregistrar;

    //variables de los datos a registrar
    private String Nombre="";
    private String mail="";
    private String pass="";

    private FirebaseAuth mAuth;

    DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Tname = (TextView) findViewById(R.id.Tname);
        Tmail= (TextView) findViewById(R.id.Tmail);
        Tpass= (TextView) findViewById(R.id.Tpass);


         Tname.setText("NombreNumero1");
         Tmail.setText("Mailgenerico@gmail.com");
        Tpass.setText("123456");



        Bregistrar= (Button) findViewById(R.id.Bregistrar);
        mAuth= FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();



        Bregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nombre= Tname.getText().toString();
                mail= Tmail.getText().toString();
                pass= Tpass.getText().toString();

            if (!Nombre.isEmpty() && !mail.isEmpty() && !pass.isEmpty() ){

                if (pass.length() >=6){
                    RegistrarUsuario();
                }else{
                    Toast.makeText(Registrar.this,"Minimo 6 caracteres en el password",Toast.LENGTH_SHORT).show();
                }



            }else{
                Toast.makeText(Registrar.this,"Completar campos",Toast.LENGTH_SHORT).show();
            }


            }
        });

    }

    private void RegistrarUsuario(){
        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //el usuario se ha creado exitosamente
                    //obtenemos el ID
                    String id = mAuth.getCurrentUser().getUid();
                    //creamos un map
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", Nombre);
                    map.put("mail", mail);
                    map.put("pass", pass);

                    //creamos un nodo hijo y le pasamos el mapa de valores
                    mDataBase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(Registrar.this,"Usuario Registrado",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registrar.this,SignOut.class ));
                                finish();
                            }else{
                                Toast.makeText(Registrar.this,"error database",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }else{
                    Toast.makeText(Registrar.this,"No se pudo regitrar este usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}