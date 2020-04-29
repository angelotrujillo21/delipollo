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

import com.example.consultores.delipollo.R;

/**
 * Created by consultores on 10/06/2019.
 */

public class dialogCambiarClave extends AppCompatDialogFragment {
    private TextInputLayout txt_password,txt_newpassword;
    private dialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_cambiar_clave,null);
        builder.setView(view).
                setTitle("CAMBIAR CONTRASEÑA").setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String pass = txt_password.getEditText().getText().toString();
                String new_pass = txt_newpassword.getEditText().getText().toString();
                listener.CambiarClave(pass,new_pass);

            }
        }).setIcon(R.drawable.ic_password_color)
        ;


        txt_password = (TextInputLayout) view.findViewById(R.id.password);
        txt_newpassword = (TextInputLayout) view.findViewById(R.id.new_password);



        return  builder.create();
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
        void CambiarClave(String pass, String new_pass);

    }

}
