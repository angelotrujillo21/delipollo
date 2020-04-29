package com.example.consultores.delipollo.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.example.consultores.delipollo.R;

import java.util.HashMap;
import java.util.Map;


public class dialogEnviarMsg extends AppCompatDialogFragment {
    private TextInputLayout txt_mensaje;
    private Button btn_enviar;
    private dialogListener listener;
    RequestQueue queue ;
    TextView txt_orden,txt_salir;
    ImageView img ;

    private Context context;
    private String idCliente ;
    private String idOrden ;
    String estado ;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    private  int colores[]={
            R.color.chart_value_1,
            R.color.chart_value_2_2,
            R.color.chart_value_3,
            R.color.chart_value_4,

    };


    private  int dataImg[]={
            R.drawable.lista,
            R.drawable.turkey,
            R.drawable.place,
            R.drawable.shield
    };


    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }



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
        View view = inflater.inflate(R.layout.dialog_enviar_msg,null);

        builder.setView(view);

         init(view);
        ListenerButton();
        Render(Integer.parseInt(getEstado()));
        return  builder.create();
    }

    private void ListenerButton() {
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviarMensaje();
            }
        });

                txt_salir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
    }

    private void init(View view) {
        queue = Volley.newRequestQueue(getContext());  // this = context

        txt_mensaje = (TextInputLayout) view.findViewById(R.id.txt_mensaje);
        btn_enviar = (Button) view.findViewById(R.id.btn_enviar);
        txt_orden = (TextView) view.findViewById(R.id.txt_orden);
        txt_salir = (TextView) view.findViewById(R.id.txt_salir);

        img = (ImageView) view.findViewById(R.id.img);


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



    public Boolean ValidarMensaje(){
        String Texto =  txt_mensaje.getEditText().getText().toString().trim();

        if (Texto.isEmpty()){
            txt_mensaje.setError("INGRESE DATOS AL CAMPO");
            return  false;
        } else {
            txt_mensaje.setError(null);

            return  true;
        }

    }




    public  void EnviarMensaje(){
        try {

            String url = Utilidades.server + "api/public/api/quejas/nuevo";

            final ProgressDialog cargando;
            cargando = ProgressDialog.show(getContext(),
                    "Procesando Mensaje", "Por Favor Espere ..", false, false);

            if (ValidarMensaje()) {

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response
                                cargando.dismiss();
                                String rpta = response.toString();
                                Log.d("Response", rpta);
                                if (rpta.equals("1")) {
                                    dismiss();
                                    OpenDialogMsg();
                                } else {
                                    Utilidades.Toast(getContext(),"HUBO UN ERROR AL PROCESAR EL MENSAJE");

                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                cargando.dismiss();
                                Log.d("Error.Response", error.getMessage());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("idOrden", getIdOrden());
                        params.put("mensaje", txt_mensaje.getEditText().getText().toString());
                        Log.d("params",params.toString());
                        return params;
                    }
                };
                queue.add(postRequest);

            }
            else
                {
                    cargando.dismiss();
                    Toast.makeText(getContext(), "Ingrese Datos", Toast.LENGTH_SHORT).show();
                }

        }catch (Exception e){
            Log.e("error",e.getMessage().toString());
        }

    }




    private void OpenDialogMsg() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_msg_queja);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_aceptar);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Limpiar();

            }
        });

        dialog.show();

    }



    public void Opcion1(){
        txt_orden.setTextColor(getColor(getContext(),colores[0]));
        img.setImageResource(dataImg[0]);
    }


    public void Opcion2(){
        txt_orden.setTextColor(getColor(getContext(),colores[1]));
        img.setImageResource(dataImg[1]);

    }


    public void Opcion3(){
        txt_orden.setTextColor(getColor(getContext(),colores[2]));
        img.setImageResource(dataImg[2]);

    }

    public void Opcion4(){
        txt_orden.setTextColor(getColor(getContext(),colores[3]));
        img.setImageResource(dataImg[3]);
    }



    public void Render(int estado){
        if (estado==1){
            Opcion1();
        }else if(estado==2){
            Opcion2();

        }else if(estado==3){
            Opcion3();

        }else if(estado==4){
            Opcion4();

        }


    }



    private void Limpiar() {
        txt_mensaje.getEditText().setText("");
  //      btn_enviar.setEnabled(true);
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
