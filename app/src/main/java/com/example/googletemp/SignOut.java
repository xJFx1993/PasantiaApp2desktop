package com.example.googletemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class SignOut extends AppCompatActivity {



    private Button MB;
    private FirebaseAuth mAuth;


    private TextView Globalname;
    private TextView Globalmail;

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        mAuth = FirebaseAuth.getInstance();


         Button MB = (Button) findViewById(R.id.B);

        Globalname = (TextView) findViewById(R.id.TN);
        Globalmail = (TextView) findViewById(R.id.TM);

        MB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //volver a la pantalla
                startActivity(new Intent(SignOut.this,Login.class));
                finish();

            }
        });
        ObtenerInfo();
    }


    private void ObtenerInfo(){
        //Obtenemos el id del usuario que inicio sesion
        String id= mAuth.getCurrentUser().getUid();


        /*
        mDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //mail name pass
                    String name =snapshot.child("name").getValue().toString();
                    String mail =snapshot.child("mail").getValue().toString();

                    Globalname.setText(name);
                    Globalmail.setText(mail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

}