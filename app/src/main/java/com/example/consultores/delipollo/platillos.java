package com.example.consultores.delipollo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.adapter.AdapterItemPlatillo;
import com.example.consultores.delipollo.recycle.GridLayoutItemDecoration;
import com.example.consultores.delipollo.util.Platillo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class platillos extends AppCompatActivity {
    RecyclerView recycle ;
    ArrayList<Platillo> platillos = new ArrayList<Platillo>();
    String url ;
    RequestQueue queue ;
    AdapterItemPlatillo adapter;
    String codPlatillo,NomCategoria;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos);
               try{
                   codPlatillo = getIntent().getExtras().getString("codigoPlatillo");
                   NomCategoria = getIntent().getExtras().getString("NomCategoria");

                   url =  Utilidades.server+"api/public/api/platillos/"+codPlatillo;
                    this.setTitle(NomCategoria);


                   recycle = (RecyclerView) findViewById(R.id.listaRecicle);
                   recycle.setHasFixedSize(true);
                   recycle.addItemDecoration(new GridLayoutItemDecoration(8));
                   recycle.setLayoutManager(new GridLayoutManager(this,2));
                   CargarPlatillos(platillos,url);
                   menuBottom();



            }
            catch(Exception e){
                Log.d("Error",e.getMessage());
            }


    }



    public void Adaptar(ArrayList<Platillo> pl){

        adapter= new AdapterItemPlatillo(this, pl);

        recycle.setAdapter(adapter);

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  codigoPlatillo =
                        platillos.get(recycle.getChildAdapterPosition(v)).getplatilloId();



                String  NamePlatillo =
                        platillos.get(recycle.getChildAdapterPosition(v)).getTitle();

                String urlImagePlatillo =
                        platillos.get(recycle.getChildAdapterPosition(v)).getUrlImagen();


                String Precio =
                        platillos.get(recycle.getChildAdapterPosition(v)).getPrecio();


                String Detalle =
                        platillos.get(recycle.getChildAdapterPosition(v)).getDetalle();


                Intent siguiente = new Intent(getApplicationContext(),compra.class);
                siguiente.putExtra("categoria",codPlatillo);
                siguiente.putExtra("codigoPlatillo",String.valueOf(codigoPlatillo));
                siguiente.putExtra("NamePlatillo",NamePlatillo);
                siguiente.putExtra("urlImagePlatillo",urlImagePlatillo);
                siguiente.putExtra("Precio",Precio);
                siguiente.putExtra("Detalle",Detalle);
                siguiente.putExtra("crear",true);

                startActivity(siguiente);

            }
        });




    }



    public ArrayList<Platillo> CargarPlatillos(final ArrayList<Platillo> platillos , String url){
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
                                    Platillo platillo = new Platillo();
                                    platillo.setplatilloId(
                                    Integer.parseInt(json.getString("codigo"))
                                    );
                                    platillo.setTittle(json.getString("descripcion"));
                                    platillo.setUrlImagen(Utilidades.server+json.getString("imagen"));
                                    platillo.setPrecio(json.getString("precio"));
                                    platillo.setDetalle(json.getString("detalle_producto"));
                                    Log.d("PRECIO VOLEY->",json.getString("descripcion"));

                                    platillos.add(platillo);
                                }
                               cambiaValor(platillos);
                                Adaptar(platillos);

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



        return Utilidades.getPlatillo();


    }



    public  void cambiaValor(ArrayList<Platillo> platillos) {
        try {
            Field field = Utilidades.class.getDeclaredField("Platillo");
            field.setAccessible(true);
            field.set(null, platillos);
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
