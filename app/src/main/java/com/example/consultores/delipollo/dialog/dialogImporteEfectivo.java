package com.example.consultores.delipollo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;

/**
 * Created by consultores on 10/06/2019.
 */

public class dialogImporteEfectivo extends AppCompatDialogFragment {
    private TextInputLayout txt_cantidad;
    private dialogListener listener;
    public String monto;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_efectivo_compra,null);
        builder.setView(view).
                setTitle("MONTO : S./ " +Utilidades.Format(getMonto()) )
                .setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyTexts("0",false);
            }
        }).setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String cantidad = txt_cantidad.getEditText().getText().toString();
                listener.applyTexts(cantidad,true);

            }
        }).setIcon(R.drawable.ic_money)
        ;


        txt_cantidad = (TextInputLayout) view.findViewById(R.id.txt_cantidad);
        //txt_cantidad.getEditText().setText();


        return  builder.create();
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
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
        void applyTexts(String cantidad, boolean estado);

    }

}
