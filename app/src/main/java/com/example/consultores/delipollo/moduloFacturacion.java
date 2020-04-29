package com.example.consultores.delipollo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.js.JavaFacturacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class moduloFacturacion extends AppCompatActivity implements
        JavaFacturacion.InterfaceJs {
    RequestQueue queue;

    bd obj;
    WebView webView;
    String monto, efectivo, vuelto, direccionDeli, distrito, referencia, coment, email = "", token = "", metodoPago;
    String url;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_facturacion);
        try {
            obj = new bd(this);


            monto = getIntent().getExtras().getString("monto");
            efectivo = getIntent().getExtras().getString("efectivo");
            vuelto = getIntent().getExtras().getString("vuelto");

            direccionDeli = getIntent().getExtras().getString("direccion");
            distrito = getIntent().getExtras().getString("distrito");
            referencia = getIntent().getExtras().getString("referencia");
            coment = getIntent().getExtras().getString("coment");

            metodoPago = getIntent().getExtras().getString("metodoPago");

            LoadWebView();



        } catch (Exception e) {
            Log.d("Error ->", e.getMessage());
        }


    }

    private void WebViewUrl(String estado) {
        String valor = ValidarEstado(estado);

        if (isTarjeta()) {
            email = !getIntent().getExtras().getString("email").contentEquals("")
                    ? getIntent().getExtras().getString("email") : "";
            token = !getIntent().getExtras().getString("token").contentEquals("")
                    ? getIntent().getExtras().getString("token") : "";
            this.url = Utilidades.server + "modulos/facturacion.html?estado=true&email="+email+"&fac="+valor;
        } else {
            this.url = Utilidades.server + "modulos/facturacion.html?estado=false"+"&fac="+valor;

        }

        Log.d("url",this.url);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        JavaFacturacion jsInterface = new JavaFacturacion(this, this);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(this.url);
        WebSettings webSettings = webView.getSettings();
        webView.addJavascriptInterface(jsInterface, "Android");
        webSettings.setJavaScriptEnabled(true);


    }

    private String ValidarEstado(String estado) {
        if (estado.contentEquals("SI")){
            return "true";
        }else {
            return "false";
        }


    }


    public void LoadWebView(){
        final ProgressDialog cargando  = ProgressDialog.show(
                this,"Cargando Informacion ....","Por Favor Espere ..",
                false,false );
        String  url  = Utilidades.server+"api/public/api/facturacion/GetEstadoFacturacion";

        queue = Volley.newRequestQueue(this);  // this = context
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cargando.dismiss();
                            String estado=null;
                            JSONArray Jarray = response.getJSONArray("data");

                            for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject json = Jarray.getJSONObject(i);
                                estado = json.getString("fact_elect");
                            }
                            WebViewUrl(estado);


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




    public boolean isTarjeta() {
        if (metodoPago.contentEquals("TARJETA")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isTarjeta()) {
            Utilidades.Toast(this, "REALIZE LA FACTURACION !");
        } else {
            if (webView.canGoBack()) {
                webView.goBack();

            } else {
                super.onBackPressed();

            }
        }
    }


    @Override
    public void
    facturacion
            (String codigo_tipo_doc,
             String tipodoc_texto,
             String nro_doc,
             String nombre,
             String direccion,
             String emailWeb,
             String facGlobal) {
        try {
            queue = Volley.newRequestQueue(this);  // this = context

            String url = Utilidades.server + "api/public/api/facturacion/nuevo";

            StringRequest postRequest;

            final JSONObject json_send = new JSONObject();
            JSONArray data = new JSONArray();

            Cursor cursor_cliente = obj.ReadTable(Utilidades.table_user);
            while (cursor_cliente.moveToNext()) {
                json_send.put("id_cliente", cursor_cliente.getString(0));
                json_send.put("razon_social", cursor_cliente.getString(2));
            }

            json_send.put("codigo_tipo_doc", codigo_tipo_doc);
            json_send.put("tipodoc_texto", tipodoc_texto);
            json_send.put("nrodoc", nro_doc);
            json_send.put("razonsocial", nombre);
            json_send.put("direccionFactura", direccion);

            json_send.put("direccionDelivery", direccionDeli);
            json_send.put("distrito", distrito);
            json_send.put("referencia", referencia);
            json_send.put("metodoPago", metodoPago);

            json_send.put("token", token);
            json_send.put("emailUser", emailWeb);

            json_send.put("montoTotal", Utilidades.Format(monto));
            json_send.put("efectivo", Utilidades.Format(efectivo));
            json_send.put("vuelto", Utilidades.Format(vuelto));

            json_send.put("comentario", coment);
            json_send.put("fac", facGlobal);


            Cursor cursor = obj.ReadTable(Utilidades.table_venta);
            while (cursor.moveToNext()) {
                JSONObject d = new JSONObject();
                d.put("ITEM", cursor.getString(0));
                d.put("CODIGO_CATEGORIA", cursor.getString(1));
                d.put("CODIGO_PRODUCTO", cursor.getString(2));
                d.put("DESCRIPCION_PRODUCTO", cursor.getString(3));
                d.put("DETALLE", cursor.getString(4));
                d.put("PRECIO", cursor.getString(5));
                d.put("CANT", cursor.getString(6));
                d.put("MONTO", cursor.getString(7));
                d.put("OBSERVACION", cursor.getString(8));
                data.put(d);
            }

            json_send.put("PRODUCTOS_CARRITO", data);

            final ProgressDialog cargando
                    = ProgressDialog.show(this,
                    "Procesando Pedido", "Por Favor Espere ..", false, false);


            postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                cargando.dismiss();
                                Log.d("rpta", response);

                                String rpta = response.toString();
                                String[] separated = rpta.split("\\|");
                                if (separated[0].contentEquals("1")) {
                                    obj.LimpiarTable(Utilidades.table_venta);
                                    OpenDialogMsg(separated[1]);

                                } else {
                                    OpenDialogError();
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Error! Verifique Datos Ingresados o Vuelva a Intentarlo",
                                            Toast.LENGTH_SHORT).show();


                                }

                            } catch (Exception e) {
                                cargando.dismiss();
                                OpenDialogError();
                                Log.d("ERROR", e.getMessage().toString());
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            OpenDialogError();
                            cargando.dismiss();
                            String err = (error.getMessage() == null) ? "Fallo en la conexion° " : error.getMessage();
                            Log.d("Error.Response", err);
                            Toast.makeText(getApplicationContext(), "VUELVA A INTENTARLO PROBLEMAS CON EL SERVIDOR! ", Toast.LENGTH_SHORT).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    Log.d("json_send", json_send.toString());
                    params.put("data", json_send.toString());
                    return params;
                }
            };


            queue.add(postRequest);
            postRequest.setRetryPolicy(
                    new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );


        } catch (Exception e) {
            Log.d("ERROR", e.getMessage().toString());
        }


    }


    private void OpenDialogMsg(String nrodoc) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_msg);

        TextView text = (TextView) dialog.findViewById(R.id.nrodoc);
        text.setText("N° DOCUMENTO  : " + nrodoc);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_aceptar);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cat = new Intent(getApplicationContext(), categorias.class);
                startActivity(cat);

            }
        });

        dialog.show();


    }


            private void OpenDialogError() {
                try {

                    new AlertDialog.Builder(this)
                            .setTitle("¿Desea continuar?")
                            .setMessage(" Hubo un error al procesar la factura")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    obj.LimpiarTable(Utilidades.table_venta);
                                    Intent cat = new Intent(getApplicationContext(), categorias.class);
                                    startActivity(cat);
                                }
                            })
                            .setNegativeButton("NO", null)
                            .setIcon(R.drawable.exclamation)
                            .show();
                }catch (Exception e){
                    Log.e("error",e.getMessage().toString());
                }

            }


}
