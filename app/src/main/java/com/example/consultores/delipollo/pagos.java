package com.example.consultores.delipollo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.dialog.dialogImporteEfectivo;
import com.example.consultores.delipollo.tablas.tabla;
import com.example.consultores.delipollo.util.comprasProd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class pagos extends AppCompatActivity implements dialogImporteEfectivo.dialogListener {
    Button btn_pagar;
    bd obj ;
    ArrayList<comprasProd> listacompra = new ArrayList<>();
    double monto ;
    EditText txt_direccion,txt_coment,txt_referencia ;
    RequestQueue queue ;
    Spinner spiner_distrito ;
    List<String> distrito;
    ArrayAdapter<String> adapterSpinner;
    String metodoPago = "",efectivo="0.00",vuelto="0.00";
    RadioButton radio_efectivo , radio_tarjeta;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        try{
           this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().getDecorView().findViewById(android.R.id.content).clearFocus();

            obj = new bd(this);
            btn_pagar = (Button) findViewById(R.id.btn_pagar);
            txt_direccion = (EditText)  findViewById(R.id.txt_direccion);
            txt_coment = (EditText)  findViewById(R.id.txt_coment);
            txt_referencia = (EditText)  findViewById(R.id.txt_referencia);
            spiner_distrito = (Spinner) findViewById(R.id.spiner_distrito);

            radio_efectivo = (RadioButton)  findViewById(R.id.radio_efectivo);
            radio_tarjeta = (RadioButton)  findViewById(R.id.radio_tarjeta);

            Masyuculas();
            CargarInformacion();
            CargarDistrito();
            menuBottom();

        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }


    }

    private void Masyuculas() {

        txt_direccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txt_coment.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txt_referencia.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


    }


    public void CargarDistrito(){
        distrito = new ArrayList<>();
        String url = Utilidades.server + "api/public/api/distrito";
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
                                distrito.add(json.getString("distrito"));

                            }


                        adaptar();



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

    private void adaptar() {
        try {
            adapterSpinner = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, distrito);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner_distrito.setAdapter(adapterSpinner);
        }catch (Exception e){
            Log.d("error",e.getMessage().toString());
        }

    }


    private void CargarInformacion() {
        Cursor cursor = obj.ReadTable(Utilidades.table_venta);
        double acu = 0;
        while (cursor.moveToNext()) {
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
            listacompra.add(cp);
            acu += Double.parseDouble(cursor.getString(7));
        }
        monto = acu;
              CatgarTabla(listacompra);
        PrintButton(acu);


    }

    private void CatgarTabla(ArrayList<comprasProd> listacompra) {
        try {
            ArrayList<String> cabecera = new ArrayList<>();
            cabecera.add("DESCRIPCION");
            cabecera.add("CANTIDAD");
            cabecera.add("IMPORTE");
            tabla objTabla = new tabla(this, (TableLayout) findViewById(R.id.tabla));
            objTabla.agregarCabecera(cabecera);


            for(int i = 0; i < listacompra.size(); i++)
            {
                ArrayList<String> elementos = new ArrayList<String>();
                elementos.add(listacompra.get(i).getDescripcion());
                elementos.add(listacompra.get(i).getCantidad());
                elementos.add("S./ " +Utilidades.Format(listacompra.get(i).getMonto()));
                objTabla.agregarFilaTabla(elementos);
            }



        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }


    }


    private void PrintButton(Double acu) {
        btn_pagar.setText("TOTAL A PAGAR (S./ "+
                Utilidades.Format(String.valueOf(acu))
                +" )");
    }













    public void onRadioButtonClicked(View view) {
// Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
// hacemos un case con lo que ocurre cada vez que pulsemos un botón
        switch(view.getId()) {
            case R.id.radio_tarjeta:
                if (checked)
                    metodoPago ="TARJETA";
                    efectivo = vuelto = "0.00";
                    radio_efectivo.setText("EFECTIVO");
                    break;
            case R.id.radio_efectivo:
                if (checked)
                    metodoPago ="EFECTIVO";
                   OpenDialogCantidad();
                    break;

        }
    }



    public void Pasar(View v){
        try {
            Intent siguiente = null;
            if (metodoPago.contentEquals("TARJETA")) {
                siguiente= new Intent(this, moduloTarjeta.class);
            }else if(metodoPago.contentEquals("EFECTIVO")) {
                 siguiente = new Intent(this, moduloFacturacion.class);
            }

            siguiente.putExtra("monto",String.valueOf(monto));
            siguiente.putExtra("efectivo",String.valueOf(efectivo));
            siguiente.putExtra("vuelto",String.valueOf(vuelto));

            siguiente.putExtra("direccion",txt_direccion.getText().toString());
            siguiente.putExtra("distrito",spiner_distrito.getSelectedItem().toString());
            siguiente.putExtra("referencia",
                    !txt_referencia.getText().toString().contentEquals("") ? txt_referencia.getText().toString() : "");
            siguiente.putExtra("coment",
                    !txt_coment.getText().toString().contentEquals("") ? txt_coment.getText().toString() : "");

            siguiente.putExtra("metodoPago",String.valueOf(metodoPago));



            startActivity(siguiente);
        }catch (Exception e){
            Log.d("Error",e.getMessage().toString());

        }
    }


    private void OpenDialogCantidad() {
        dialogImporteEfectivo dialog = new dialogImporteEfectivo();
        dialog.setMonto(String.valueOf(monto));
        dialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        dialog.show(getSupportFragmentManager(),"Dialog Efectivo");
    }


    @Override
    public void applyTexts(String cantidad, boolean estado) {
        try {
            double efectivo  = Double.parseDouble(cantidad);
            if (efectivo>0)  {
                if (estado) {
                double vuelto = efectivo - monto;
                    this.efectivo =  Utilidades.Format(String.valueOf(efectivo));
                    this.vuelto =  Utilidades.Format(String.valueOf(vuelto));
                    if (vuelto>0){
                        radio_efectivo.setText("EFECTIVO (S./ "+
                                Utilidades.Format(String.valueOf(efectivo)) + ") || VUELTO " +
                                "(S./ "+
                                Utilidades.Format(String.valueOf(vuelto))+")");

                    }
                    else {
                        radio_efectivo.setText("EFECTIVO S./ "+ Utilidades.Format(String.valueOf(monto)));
                        Utilidades.Toast(this,"INGRESE UN MONTO MAYOR AL IMPORTE!");
                    }

                } else{
                    radio_efectivo.setText("EFECTIVO S./ "+ Utilidades.Format(String.valueOf(monto)));

                }

            }

            else{
                Utilidades.Toast(this,"INGRESE UN MONTO!");
                radio_efectivo.setText("EFECTIVO S./ "+ Utilidades.Format(String.valueOf(monto)));

            }

        }catch (Exception e){
            radio_efectivo.setText("EFECTIVO S./ "+ Utilidades.Format(String.valueOf(monto)));
            Utilidades.Toast(this,"INGRESE UN MONTO!");
            Log.d("error",e.getMessage().toString());
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
