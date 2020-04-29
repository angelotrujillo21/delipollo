package com.example.consultores.delipollo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.adapter.AdapterItem;
import com.example.consultores.delipollo.recycle.GridLayoutItemDecoration;
import com.example.consultores.delipollo.util.Categoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class categorias extends AppCompatActivity {
    ArrayList<Categoria> category = new ArrayList<Categoria>();

    RecyclerView recycle ;
    BottomNavigationView bottomNavigationView;
    //private BottomBar mBottomBar;
    String url = Utilidades.server+"api/public/api/categorias";
    RequestQueue queue ;
    AdapterItem adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        try{
            recycle = (RecyclerView) findViewById(R.id.listaRecicle);
            CargarCategorias(category);
            menuBottom();
        }
        catch (Exception e){
            Log.d("Error ->",e.getMessage());
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



    public void Adaptar(ArrayList<Categoria> category){
        adapter= new AdapterItem(this,category);

        recycle.setHasFixedSize(true);
        recycle.addItemDecoration(new GridLayoutItemDecoration(6));
        recycle.setLayoutManager(new GridLayoutManager(this,2));

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int  codigoPlatillo =
                        Utilidades.getCategory().get(recycle.getChildAdapterPosition(v)).getCategoryId();
                String  NomCategoria =
                        Utilidades.getCategory().get(recycle.getChildAdapterPosition(v)).getTitle();

                Intent siguiente=new Intent(getApplicationContext(),platillos.class);
                siguiente.putExtra("codigoPlatillo",String.valueOf(codigoPlatillo));
                siguiente.putExtra("NomCategoria",NomCategoria);

                startActivity(siguiente);


            }
        });
        recycle.setAdapter(adapter);


    }






    public void CargarCategorias(final ArrayList<Categoria> category){
            final ProgressDialog cargando  = ProgressDialog.show(
                    this,"Cargando Informacion ....","Por Favor Espere ..",
                    false,false );

            queue = Volley.newRequestQueue(this);  // this = context
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                cargando.dismiss();
                                JSONArray Jarray = response.getJSONArray("data");

                                for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject json = Jarray.getJSONObject(i);
                                Categoria cat = new Categoria();
                                cat.setCategoryId(Integer.parseInt(json.getString("id")));
                                cat.setTittle(json.getString("descripcion"));
                                cat.setUrlImagen(Utilidades.server+json.getString("imagen"));
                                 category.add(cat);
                                }

                                Adaptar(category);
                                cambiaValor(category);
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.getMessage());
                        }
                    }

            );

            queue.add(getRequest);


    }


    public  void cambiaValor(ArrayList<Categoria> category) {
        try {
            Field field = Utilidades.class.getDeclaredField("category");
            field.setAccessible(true);
            field.set(null, category);
        } catch (Exception e) {
            System.out.println("No se pudo cambiar el valor ");
            e.printStackTrace(System.out);
        }
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
                                Toast.makeText(getApplicationContext(),"ESTAS AQUI",Toast.LENGTH_SHORT).show();
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
                                Intent miCuenta = new Intent(getApplicationContext(),user.class);
                                startActivity(miCuenta);
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


}
