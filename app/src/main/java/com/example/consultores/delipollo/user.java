package com.example.consultores.delipollo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.adapter.AdapterOptionUser;
import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.dialog.dialogCambiarClave;
import com.example.consultores.delipollo.interfaces.UserInterface;
import com.example.consultores.delipollo.util.OptionUser;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class user extends AppCompatActivity implements UserInterface {
    RecyclerView recycle ;
    BottomNavigationView bottomNavigationView;
    ArrayList<OptionUser>  listaoption= new ArrayList<OptionUser>();
    String email,genero ,imgFb ;
    RequestQueue queue;

    ImageView img ;
       int [] icon = {
                R.drawable.ic_location_color,
               R.drawable.ic_historial_color,
               R.drawable.ic_password_color,
               R.drawable.ic_about_color,
               R.drawable.ic_logout_color
       };

       String[] titulo = {
               "Mis direcciones",
               "Historial de ordenes",
               "Cambiar contraseña",
               "Acerca de DeliPollo",
               "Cerrar sesion",
       };
    AdapterOptionUser adapter;
    bd obj ;
  TextView txt_nombre,txt_email,txt_fechaNac,txt_nrodoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        try{
            recycle = (RecyclerView) findViewById(R.id.listaRecicle);
            img = (ImageView) findViewById(R.id.img);

            obj = new bd(this);
            queue = Volley.newRequestQueue(this);  // this = context

            txt_nombre = (TextView) findViewById(R.id.txt_user);
            txt_email = (TextView) findViewById(R.id.txt_email);
            txt_nrodoc = (TextView) findViewById(R.id.txt_nrodoc);
            txt_fechaNac = (TextView) findViewById(R.id.txt_fechaNac);

            ListarDataUser();
            menuBottom();
            CargarListaOptions();




        }
        catch (Exception e){
            Log.d("Error ->",e.getMessage());
        }


    }

    private void CargarListaOptions() {
        for (int i = 0; i <icon.length ; i++) {
            OptionUser ou= new OptionUser();
            ou.setIcon(icon[i]);
            ou.setTitle(titulo[i]);
            listaoption.add(ou);
        }


        Adaptar(listaoption);
    }



    private void ListarDataUser() {

        Cursor cursor = obj.ReadTable(Utilidades.table_user);


        while (cursor.moveToNext())
        {
            txt_nombre.setText(cursor.getString(2));
            txt_email.setText(cursor.getString(3));
            txt_nrodoc.setText(cursor.getString(1) +" : "+ cursor.getString(0) );
            txt_fechaNac.setText("FECHA NAC: "+ cursor.getString(4) );
            genero =  cursor.getString(5);
            email=cursor.getString(3);
            imgFb = cursor.getString(6);

        }

        if (genero.contentEquals("Masculino")){
            img.setImageResource(R.drawable.man);
        }
        else if(genero.contentEquals("Femenino")) {
            img.setImageResource(R.drawable.girl);
        } else{
            Log.d("IMG_FB->",imgFb);
            Picasso.with(this).load(imgFb)
                    .placeholder(R.drawable.ic_user_preview).
                    error(R.drawable.lodideli).
                    into(img);

        }



    }





    public void Adaptar(final ArrayList<OptionUser> listaoption){
        adapter= new AdapterOptionUser(this,listaoption);

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new GridLayoutManager(this,1));
        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String option =  listaoption.get(recycle.getChildAdapterPosition(v)).getTitle();


        if (option.contentEquals(titulo[4])){
            onBackPressed();
        }
        else if(option.contentEquals(titulo[2])){
            OpenDialogCambiarClave();
        }


            }
        });

        recycle.setAdapter(adapter);
    }



    private void OpenDialogCambiarClave() {
        dialogCambiarClave dialog = new dialogCambiarClave();
        dialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        dialog.show(getSupportFragmentManager(),"Dialog change password");

    }


    public void menuBottom(){
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_menu:
                                Intent cat = new Intent(getApplicationContext(),categorias.class);
                                startActivity(cat);

                                break;
                            case R.id.ic_orden:
                                Intent carrito = new Intent(getApplicationContext(),carrito.class);
                                startActivity(carrito);

                                break;
                            case R.id.ic_historial:
                                Intent historial = new Intent(getApplicationContext(),historial.class);
                                startActivity(historial);
                                break;

                            case R.id.ic_user:
                                Toast.makeText(getApplicationContext(),"ESTAS AQUI",Toast.LENGTH_SHORT).show();

                                break;

                        }
                        return false;
                    }
                });


        removeShiftMode(bottomNavigationView);


    }




    private void removeShiftMode(BottomNavigationView bottomNavigationView) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(R.drawable.ic_salir);
        builder1.setMessage("¿ Deseas Salir de la App ? ");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utilidades.LimpiarApp(getApplicationContext());
                        Intent siguiente = new Intent(getApplicationContext(), login.class);
                        startActivity(siguiente);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    @Override
    public void CambiarClave(final String pass, final String new_pass) {
        final ProgressDialog cargando;
        String url = Utilidades.server+"api/public/api/login/CambiarClave" ;
        cargando  = ProgressDialog.show(this,"Validando Informacion ....","Por Favor Espere ..",false,false );


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            cargando.dismiss();
                            String rpta = response.toString();

                            if(rpta.contentEquals("1")){
                                Utilidades.Toast(getApplicationContext(),"CAMBIO REALIZADO! ");
                            }else {
                                Utilidades.Toast(getApplicationContext(),"VERIFIQUE LA CLAVE INGRESADA! ");
                                OpenDialogCambiarClave();
                            }

                        } catch (Exception e) {
                            Log.d("e", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        cargando.dismiss();
                        String err = (error.getMessage()==null)?"Fallo en la conexion° ":error.getMessage();
                        Log.d("Error.Response", err);
                        Toast.makeText(getApplicationContext(),"VUELVA A INTENTARLO PROBLEMAS CON EL SERVIDOR! ",Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("clave", pass);
                params.put("newClave", new_pass);

                return params;
            }
        };
        queue.add(postRequest);




    }
}
