package com.example.consultores.delipollo;

import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.adapter.adapterCarrito;
import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.util.comprasProd;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class carrito extends AppCompatActivity
        implements  adapterCarrito.EventoCambio {
    bd obj ;
    RecyclerView recycle ;
    BottomNavigationView bottomNavigationView;
    ArrayList<comprasProd>  listacompra = new ArrayList<>();
    adapterCarrito adapter;
    Button btn_pagar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        try{

            menuBottom();

            recycle = (RecyclerView) findViewById(R.id.listaRecicle);
            btn_pagar = (Button) findViewById(R.id.btn_pagar);

            obj = new bd(this);
            ListarPedidos();





        }catch (Exception e){
            Log.d("Error ->",e.getMessage());
        }


    }

    private void ListarPedidos() {
        listacompra.clear();

        Cursor cursor = obj.ReadTable(Utilidades.table_venta);

        double acu = 0;

        while (cursor.moveToNext())
        {
            comprasProd cp   = new comprasProd();
            cp.setIdCarrito(cursor.getString(0));
            cp.setCodigoCategoria(cursor.getString(1));
            cp.setCodigo(cursor.getString(2));
            cp.setDescripcion(cursor.getString(3));
            cp.setDetalle(cursor.getString(4));
            cp.setPrecio(cursor.getString(5));
            cp.setCantidad(cursor.getString(6));
            cp.setMonto(cursor.getString(7));
            cp.setObservacion(cursor.getString(8));
            cp.setImagen(cursor.getString(9));
            cp.setFecha(cursor.getString(10));
            cp.setGrupoVenta(cursor.getInt(11));

            listacompra.add(cp);

            acu+=Double.parseDouble(cursor.getString(7));


        }

        adaptar(listacompra);
        PrintButton(acu);

    }

    private void adaptar(final ArrayList<comprasProd> listacompra) {
         adapter= new adapterCarrito(this,listacompra,this);
        recycle.setLayoutManager(new GridLayoutManager(this,1));

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int  IdGrupoVenta =   listacompra.get(recycle.getChildAdapterPosition(v)).getGrupoVenta();
               /* Utilidades.Toast(getApplicationContext(),
                        String.valueOf(IdGrupoVenta)
                        );*/
                Editar(IdGrupoVenta);


            }
        });
        recycle.setAdapter(adapter);


    }

    private void Editar(int idGrupoVenta) {
        Intent edit  = new Intent(this,compra.class);
        edit.putExtra("GrupoVenta",idGrupoVenta);
        edit.putExtra("edit",true);
        startActivity(edit);
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
                                Toast.makeText(getApplicationContext(),"ESTAS AQUI",Toast.LENGTH_SHORT).show();

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


    @Override
    public void cambio(ArrayList<comprasProd> compra) {
        UpdateDb(compra);
    }

    @Override
    public void delete(String id) {
        obj.Delete(Utilidades.table_venta,Utilidades.venta_campo_codigoVenta +"="+id);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void importe(double acu) {
        PrintButton(acu);
    }

    private void UpdateDb(ArrayList<comprasProd> compra) {
        for (int i = 0; i <compra.size() ; i++) {
            String where =  Utilidades.venta_campo_codigoVenta + "=" + compra.get(i).getIdCarrito() ;
            ContentValues valores  = new ContentValues();
            valores.put(Utilidades.venta_campo_codigoCategoria,compra.get(i).getCodigoCategoria());
            valores.put(Utilidades.venta_campo_codigoProd,compra.get(i).getCodigo() );
            valores.put(Utilidades.venta_campo_DescripcionProd,compra.get(i).getDescripcion());
            valores.put(Utilidades.venta_campo_Detalle,compra.get(i).getDetalle());
            valores.put(Utilidades.venta_campo_precio,compra.get(i).getPrecio());
            valores.put(Utilidades.venta_campo_cantidad,compra.get(i).getCantidad());
            valores.put(Utilidades.venta_campo_importe,compra.get(i).getMonto());
            valores.put(Utilidades.venta_campo_observacion,compra.get(i).getDescripcion());
            valores.put(Utilidades.venta_campo_imagen,compra.get(i).getImagen());
            valores.put(Utilidades.venta_campo_fecha,compra.get(i).getFecha());
            obj.Update(Utilidades.table_venta,valores,where);

        }

        adapter.notifyDataSetChanged();
    }

    private void PrintButton(double acu) {
 btn_pagar.setText
         ("REALIZAR PAGO  ( S./ "+Utilidades.Format(String.valueOf(acu))+" )");
    }



    public void Pagar(View v){
        try{
            if (listacompra.size()>0){
                Intent  siguiente  = new Intent(this,pagos.class);
                startActivity(siguiente);

            } else {
                Utilidades.Toast(this,"No tiene productos en el carrito");
            }

        }catch (Exception e){
            Log.d("Error",e.getMessage().toString());
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



}
