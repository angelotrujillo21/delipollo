package com.example.consultores.delipollo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.consultores.delipollo.adapter.adapterPedido;
import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.dialog.dialogEnviarMsg;
import com.example.consultores.delipollo.dialog.dialogPedidoPendiente;
import com.example.consultores.delipollo.dialog.dialogViewMoto;
import com.example.consultores.delipollo.recycle.GridLayoutItemDecoration;
import com.example.consultores.delipollo.util.Pedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class historial extends AppCompatActivity implements adapterPedido.EventoCambio  {
    RecyclerView recycle ;
    BottomNavigationView bottomNavigationView;
    RequestQueue queue ;
    ArrayList<Pedido> listPedido = new ArrayList<>();
    adapterPedido adapter;
    SwipeRefreshLayout swipeRefreshLayout ;
    bd obj ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        try{
            recycle = (RecyclerView) findViewById(R.id.listaRecicle);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);

            obj = new bd(this);
            queue = Volley.newRequestQueue(this);  // this = context

            menuBottom();
            CargarPedidos();
            Refresh();

        }
        catch (Exception e){
            Log.d("Error ->",e.getMessage());
        }


    }

    private void Refresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.chart_value_1,R.color.chart_value_2,R.color.chart_value_3);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            finish();
                            startActivity(getIntent());
                    }
                },1000);

            }
        });
    }


    public  String GetIdCliente(){
        String id ="";
        Cursor cursor_moto = obj.ReadTable(Utilidades.table_user);
        while (cursor_moto.moveToNext())
        {
            id = cursor_moto.getString(0);
        }
        return  id;

    }




    private void OpenDialogPedidoPendiente(String idOrden) {
        dialogPedidoPendiente dialog = new dialogPedidoPendiente();
        dialog.setIdOrden(idOrden);
        dialog.setContext(this);
        dialog.setIdCliente(GetIdCliente());
        dialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        dialog.show(getSupportFragmentManager(),"Dialog Orden Pendiente");

    }



    private void OpenDialogMensaje(String idOrden,String estado) {
        dialogEnviarMsg dialog = new dialogEnviarMsg();
        dialog.setIdOrden(idOrden);
        dialog.setContext(this);
        dialog.setEstado(estado);
        dialog.setIdCliente(GetIdCliente());
        dialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        dialog.show(getSupportFragmentManager(),"Dialog Msg");

    }



    public void CargarPedidos(){
        final ProgressDialog cargando  = ProgressDialog.show(
                this,"Cargando Informacion ....","Por Favor Espere ..",
                false,false );
        String url = Utilidades.server+"api/public/api/pedido/"+GetIdCliente();
Log.d("url",url);
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
                                Pedido pedido = new Pedido();

                                pedido.setIdPedido((json.getString("id_Pedido")));
                                pedido.setFecha((json.getString("fAtencion_Pedido")));
                                pedido.setPuntuacion((json.getString("puntuacion")));
                                pedido.setMetodoPago((json.getString("N_Preliquidacion")));
                                pedido.setCod_estado((json.getString("estado_Pedido")));
                                pedido.setDesc_estado((json.getString("descripcion")));
                                pedido.setMontoTotal((json.getString("Importe_Total")));


                                listPedido.add(pedido);
                            }

                            Adaptar(listPedido);
                        } catch (JSONException e) {
                            cargando.dismiss();
                            e.printStackTrace();

                        }


                    }
                },


                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cargando.dismiss();
                        Log.d("Error.Response", error.getMessage());
                    }
                }

        );

        queue.add(getRequest);


    }


    public void Adaptar(final ArrayList<Pedido> pedido){
        adapter= new adapterPedido(this,pedido,this);

        recycle.setHasFixedSize(true);
        recycle.addItemDecoration(new GridLayoutItemDecoration(6));
        recycle.setLayoutManager(new GridLayoutManager(this,1));

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          String  idPedido =   pedido.get(recycle.getChildAdapterPosition(v)).getIdPedido();

          int   estadoPedido =  Integer.parseInt(pedido.get(recycle.getChildAdapterPosition(v)).getCod_estado());
                if (estadoPedido==4){
                    OpenDialogPedidoPendiente(idPedido);
                }else{
                    OpenDialogMensaje(idPedido,String.valueOf(estadoPedido));
                }

                  //  Utilidades.Toast(getApplicationContext(),idPedido);


            }
        });
        recycle.setAdapter(adapter);


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
                                Toast.makeText(getApplicationContext(),"ESTAS AQUI",Toast.LENGTH_SHORT).show();

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
    public void viewMoto(String idPedido) {
        OpenDialogViewMoto(idPedido);
    }


    private void OpenDialogViewMoto(String idPedido) {
        dialogViewMoto dialog = new dialogViewMoto();
        dialog.setIdOrden(idPedido);
        dialog.setContext(this);
        dialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        dialog.show(getSupportFragmentManager(),"Dialog View Moto");
    }

}
