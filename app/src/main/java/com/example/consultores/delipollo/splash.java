package com.example.consultores.delipollo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.bd.bd;

public class splash extends AppCompatActivity  {

    bd obj ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        try{
            obj = new bd(this);

            int cant =obj.CantidadTabla(Utilidades.table_user);
            if (cant==0){
                Intent login = new Intent(this,login.class);
                startActivity(login);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Utilidades.SerieCorrelativoGrupoVenta(getApplicationContext());
                finish();
            }
            else {
                Intent cat = new Intent(this,categorias.class);
                startActivity(cat);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }


        }
        catch (Exception e){
            Log.d("Error ->",e.getMessage());
        }




    }














}
