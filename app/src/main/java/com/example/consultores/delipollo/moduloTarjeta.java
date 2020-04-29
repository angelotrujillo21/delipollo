package com.example.consultores.delipollo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.js.JavaTarjeta;

public class moduloTarjeta extends AppCompatActivity implements
        JavaTarjeta.InterfaceJs{

    WebView webView ;
    String monto,efectivo,vuelto, direccion,distrito,referencia,coment,metodoPago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_tarjeta);
        try{
           this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


            monto = getIntent().getExtras().getString("monto");
            efectivo = getIntent().getExtras().getString("efectivo");
            vuelto = getIntent().getExtras().getString("vuelto");
            metodoPago = getIntent().getExtras().getString("metodoPago");


            direccion = getIntent().getExtras().getString("direccion");
            distrito = getIntent().getExtras().getString("distrito");
            referencia = getIntent().getExtras().getString("referencia");
            coment = getIntent().getExtras().getString("coment");




            JavaTarjeta jsInterface = new JavaTarjeta(this,this);

            webView = (WebView) findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(Utilidades.server+"modulos/?monto="+ Utilidades.Format(monto));
            WebSettings webSettings = webView.getSettings();
            webView.addJavascriptInterface(jsInterface, "Android");
            webSettings.setJavaScriptEnabled(true);




        }
        catch (Exception e){
            Log.d("Error ->",e.getMessage());
        }


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();

        }else{
            super.onBackPressed();

        }
    }

    @Override
    public void culqi(String valor, String email, String token) {

        if (valor.contentEquals("1")){
            Intent siguiente = new Intent(this,moduloFacturacion.class);
            siguiente.putExtra("monto",String.valueOf(monto));
            siguiente.putExtra("efectivo",String.valueOf(efectivo));
            siguiente.putExtra("vuelto",String.valueOf(vuelto));
            siguiente.putExtra("direccion",direccion);
            siguiente.putExtra("distrito",distrito);
            siguiente.putExtra("referencia",referencia);
            siguiente.putExtra("coment",coment);
            siguiente.putExtra("email",email);
            siguiente.putExtra("token",token);
            siguiente.putExtra("metodoPago",metodoPago);
            startActivity(siguiente);

        }

    }
}



