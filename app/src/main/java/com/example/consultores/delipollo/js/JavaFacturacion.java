package com.example.consultores.delipollo.js;

import android.content.Context;
import android.webkit.JavascriptInterface;


public class JavaFacturacion {

  InterfaceJs inter;
    Context context;


    public JavaFacturacion(Context context , InterfaceJs inter  ) {
        this.context = context;
        this.inter =inter;
    }

    public interface InterfaceJs {
        void  facturacion(String codigo_tipo_doc,
                          String tipodoc_texto,
                          String nro_doc,
                          String nombre,
                          String direccion,
                          String email,
                          String facGlobal
        );

    }

    @JavascriptInterface
    public void facturacion(String codigo_tipo_doc,
                            String tipodoc_texto,
                            String nro_doc,
                            String nombre,
                            String direccion,
                            String email,
                            String facGlobal

    ) {

        inter.facturacion(codigo_tipo_doc,tipodoc_texto,nro_doc,nombre,direccion,email,facGlobal);


    }





}
