package com.example.googletemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class SesionAdministrador extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_administrador);

        mAuth =  FirebaseAuth.getInstance();
        
        vistas();
    }

    private void vistas() {
    }


    public void goToCRUDAsks(View r) {


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