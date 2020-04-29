package com.example.consultores.delipollo.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class dialogViewMoto extends AppCompatDialogFragment {
    private dialogListener listener;
    RequestQueue queue ;
    String idOrden;
    Button btn_listo ;
    TextView txt_orden,txt_nombre ,txt_placa,txt_detalles  ;
    Context context ;

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_motorizado,null);


        builder.setView(view);
        init(view);
        CargarData();
        return  builder.create();
    }

    private void init(View view) {
        btn_listo = (Button) view.findViewById(R.id.btn_listo);
        txt_orden = (TextView) view.findViewById(R.id.txt_orden);
        txt_nombre = (TextView) view.findViewById(R.id.txt_nombre);
        txt_placa = (TextView) view.findViewById(R.id.txt_placa);
        txt_detalles = (TextView) view.findViewById(R.id.txt_detalle);

        ButtonListenerOnclick();
    }

    private void ButtonListenerOnclick() {

        btn_listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (dialogListener) context;
        }catch (Exception e){
            Log.d("Error",e.getMessage().toString());
        }

    }

    public interface  dialogListener{
        void applyTexts(String mensaje);

    }





    public void CargarData(){
        try {

            final ProgressDialog cargando  = ProgressDialog.show(
                    getContext(),"Cargando Informacion ....","Por Favor Espere ..",
                    false,false );

            String url = Utilidades.server + "api/public/api/pedido/moto/" + getIdOrden();
            queue = Volley.newRequestQueue(getContext());  // this = context
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String  nombre = null;
                                String  placa = null;
                                String  detalles = null;
                                String  urlImagen = null;

                                JSONArray Jarray = response.getJSONArray("data");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json = Jarray.getJSONObject(i);

                                    nombre = (json.getString("nombre"));
                                    placa = (json.getString("placa"));
                                    detalles = (json.getString("detallesmoto"));
                                    urlImagen = (json.getString("url_imagen"));

                                }
                                LoadInfo(getIdOrden(),nombre,placa,detalles,urlImagen);
                                cargando.dismiss();

                            } catch (JSONException error) {
                                 cargando.dismiss();
                                dismiss();
                                String err = (error.getMessage() == null) ? "Fallo en la conexion° " : error.getMessage();
                                Log.d("Error.Response", err);

                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cargando.dismiss();
                            dismiss();
                            String err = (error.getMessage() == null) ? "Fallo en la conexion° " : error.getMessage();
                            Log.d("Error.Response", err);
                        }
                    }

            );

            queue.add(getRequest);
        }
        catch (Exception e){
            Log.e("error",e.getMessage().toString());
        }
    }



    private void LoadInfo(String idOrden, String nombre, String placa, String detalles, String urlImagen) {
            txt_orden.setText("N° ORDEN : "+idOrden);
            txt_nombre.setText("NOMBRE : "+nombre);
            txt_placa.setText("PLACA : "+placa);
            txt_detalles.setText("DETALLES : "+detalles);

    }


}
