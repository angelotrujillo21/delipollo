package com.example.consultores.delipollo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;


public class dialogPedidoPendiente extends AppCompatDialogFragment {
    private dialogListener listener;
    RequestQueue queue ;


    private Context context;
    private String idCliente ;
    private String idOrden ;
    RatingBar rating;
    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    private TextView txt_porcentaje,txt_descripcion,txt_orden;
    private  int colores[]={
            R.color.chart_value_1,
            R.color.chart_value_2,
            R.color.chart_value_3,
            R.color.chart_value_4,

    };

    FitChart fitChart;


    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pedido_pendiente,null);


        builder.setView(view).setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        init(view);
        CargarData();
        return  builder.create();
    }


    private void init(View view) {

        fitChart = (FitChart)view.findViewById(R.id.chart);
        txt_porcentaje = (TextView) view.findViewById(R.id.txt_porciento);
        txt_descripcion = (TextView) view.findViewById(R.id.txt_descripcion);
        txt_orden = (TextView) view.findViewById(R.id.txt_orden);
        rating = (RatingBar) view.findViewById(R.id.rating);


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
            String url = Utilidades.server + "api/public/api/pedido/GetPedido/" + getIdOrden();
            queue = Volley.newRequestQueue(getContext());  // this = context
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int contador = 0;
                                String idOrden = null;
                                int puntuacion = 0;
                                JSONArray Jarray = response.getJSONArray("data");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json = Jarray.getJSONObject(i);
                                    contador = Integer.parseInt(json.getString("estado_Pedido"));
                                    puntuacion = Integer.parseInt(json.getString("puntuacion"));

                                }

                                PintarGrafico(contador, getIdOrden(),puntuacion);
                            } catch (JSONException error) {
                                dismiss();
                                String err = (error.getMessage() == null) ? "Fallo en la conexion° " : error.getMessage();
                                Log.d("Error.Response", err);

                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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




    private void PintarGrafico(int contador, String idOrden, int puntuacion) {
        try{
            rating.setRating(puntuacion);
        Collection<FitChartValue> values = new ArrayList<>();
        values.clear();
        txt_orden.setText("N° ORDEN : "+idOrden);
        if (contador==1){
            values.add(new FitChartValue(25, getColor(getContext(),colores[0])));
            txt_porcentaje.setText("25%");
            txt_descripcion.setText("ORDEN GENERADA");
        }
        if (contador==2){
            values.add(new FitChartValue(25, getColor(getContext(),colores[0])));
            values.add(new FitChartValue(25, getColor(getContext(),colores[1])));
            txt_porcentaje.setText("50%");
            txt_descripcion.setText("ORDEN ATENDIDA");
        }
        if (contador==3){
            values.add(new FitChartValue(25, getColor(getContext(),colores[0])));
            values.add(new FitChartValue(25, getColor(getContext(),colores[1])));
            values.add(new FitChartValue(25, getColor(getContext(),colores[2])));

            txt_porcentaje.setText("75%");
            txt_descripcion.setText("ORDEN EN CAMINO");
        }
        if (contador==4){
            values.add(new FitChartValue(25, getColor(getContext(),colores[0])));
            values.add(new FitChartValue(25, getColor(getContext(),colores[1])));
            values.add(new FitChartValue(25, getColor(getContext(),colores[2])));
            values.add(new FitChartValue(25, getColor(getContext(),colores[3])));
            txt_porcentaje.setText("100%");
            txt_descripcion.setText("ORDEN ENTREGADA");
        }
        fitChart.setValues(values);}
        catch (Exception e){
            Log.e("error",e.getMessage().toString());
        }


    }


    public static final int getColor(Context context, int id) {
            final int version = Build.VERSION.SDK_INT;
            if (version >= 23) {
                return ContextCompat.getColor(context, id);
            } else {
                return context.getResources().getColor(id);
            }

    }

}
