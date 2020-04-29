package com.example.consultores.delipollo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.bd.bd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginUser extends AppCompatActivity {

    TextInputLayout email ;
    TextInputLayout pass ;
    RequestQueue queue;
    bd obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
           try{
               obj = new bd(this);
               queue = Volley.newRequestQueue(this);  // this = context
               email  =(TextInputLayout) findViewById(R.id.email) ;
               pass= (TextInputLayout) findViewById(R.id.password) ;


        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }


    }




    public Boolean ValidarEmail(){
        String Texto =  email.getEditText().getText().toString().trim();

        if (Texto.isEmpty()){
            email.setError("INGRESE DATOS AL CAMPO");
            return  false;
        } else {
            email.setError(null);

            return  true;
        }

    }



    public Boolean ValidaPass(){
        String Texto =  pass.getEditText().getText().toString().trim();

        if (Texto.isEmpty()){
            pass.setError("INGRESE DATOS AL CAMPO");
            return  false;
        } else {
            pass.setError(null);

            return  true;
        }

    }




    public  void Confirmar(View v){
        try{
        final ProgressDialog cargando;
        cargando  = ProgressDialog.show(this,"Validando Informacion ....","Por Favor Espere ..",false,false );
        String url = Utilidades.server+"api/public/api/login/verificarUser" ;
        if ( ValidarEmail() && ValidaPass()) {

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                cargando.dismiss();
                                String rpta = response.toString();

                                JSONObject jsonObject = new JSONObject(rpta);

                                JSONArray Jarray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json = Jarray.getJSONObject(i);

                                    if (json.get("login").equals("1")) {
                                        CargarDataCuenta();
                                        Intent siguiente = new Intent(getApplicationContext(), categorias.class);
                                        startActivity(siguiente);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Verifique su email o password", Toast.LENGTH_SHORT).show();

                                    }

                                }


                            } catch (Exception e) {
                                Log.d("e", e.getMessage());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("email", email.getEditText().getText().toString());
                    params.put("clave", pass.getEditText().getText().toString());

                    return params;
                }
            };
            queue.add(postRequest);



        } else {
            cargando.dismiss();
            Toast.makeText(getApplicationContext(),"Ingrese Datos",Toast.LENGTH_SHORT).show();
        }
        }catch(Exception e){

            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            Log.d("error->", e.getMessage());

        }




    }


    private void InsertarDataUser(ContentValues valores) {
        obj.Insert(Utilidades.table_user,Utilidades.table_user_nrodoc,valores);

    }



    public  void CargarDataCuenta() {
        int cant = obj.CantidadTabla(Utilidades.table_user);
        Log.d("cant", String.valueOf(cant));
        if (cant == 0) {
            String url = Utilidades.server + "api/public/api/cliente/" + email.getEditText().getText().toString();
            queue = Volley.newRequestQueue(this);  // this = context
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray Jarray = response.getJSONArray("data");

                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json = Jarray.getJSONObject(i);
                                    ContentValues valores = new ContentValues();
                                    Log.d("dataCuenta", json.getString("Email"));

                                    valores.put(Utilidades.table_user_nrodoc, json.getString("NroDocumento"));
                                    valores.put(Utilidades.table_user_tipo_doc, json.getString("tipoDocumento"));
                                    valores.put(Utilidades.table_user_nombre, json.getString("Nombre"));
                                    valores.put(Utilidades.table_user_email, json.getString("Email"));
                                    valores.put(Utilidades.table_user_fechaNac, json.getString("FechaNac"));
                                    valores.put(Utilidades.table_user_genero, json.getString("Genero"));

                                    InsertarDataUser(valores);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.getMessage());
                        }
                    }

            );

            queue.add(getRequest);

        }

    }



    @Override
    public void onBackPressed() {
        Intent siguiente = new Intent(getApplicationContext(), login.class);
        startActivity(siguiente);

    }






}





