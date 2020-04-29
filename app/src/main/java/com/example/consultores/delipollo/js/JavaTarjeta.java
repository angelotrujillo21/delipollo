package com.example.consultores.delipollo.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.example.consultores.Utilidades;


public class JavaTarjeta {

  InterfaceJs inter;
    Context context;



    public JavaTarjeta(Context context ,InterfaceJs inter  ) {
        this.context = context;
        this.inter =inter;
    }

    public interface InterfaceJs {
        void culqi(String valor, String email, String token);

    }

    @JavascriptInterface
    public void Toast(String valor ,String email,String token) {
        if (valor.contentEquals("1")){
            Utilidades.Toast(context,"PAGO REALIZADO");
            inter.culqi("1",email,token);
        }
        else{
            Utilidades.Toast(context,"HUBO UN ERROR  VUELVA A INTENTARLO");
        }

    }





}
