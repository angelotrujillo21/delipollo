package com.example.consultores.delipollo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.bd.bd;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity  implements View.OnClickListener{
    TextView txt_registro ;
    CallbackManager callbackManager;
    String str_facebookname, str_facebookemail, str_facebookid;
    boolean boolean_login;
    RequestQueue queue;


    ImageView  iv_facebook;
    TextView tv_facebook;
    LinearLayout ll_facebook;
    bd obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            init();
            listener();
            Registro();




        }
        catch (Exception e){
            Log.d("error",e.getMessage().toString());
        }
    }

    private void Registro() {
        txt_registro  = (TextView) findViewById(R.id.txt_registro);
        txt_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siguiente = new Intent(getApplicationContext(),registro.class);
                startActivity(siguiente);

            }
        });

    }


    private void init() {
        queue = Volley.newRequestQueue(this);  // this = context
        obj = new bd(this);

        iv_facebook = (ImageView) findViewById(R.id.iv_facebook);
        tv_facebook = (TextView) findViewById(R.id.tv_facebook);
        ll_facebook = (LinearLayout) findViewById(R.id.ll_facebook);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
    }



    private void listener() {
        tv_facebook.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);
        iv_facebook.setOnClickListener(this);

    }



    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    Log.d("obj",object.toString());


                                    boolean_login = true;

                                    str_facebookname = object.getString("name");

                                    try {
                                        str_facebookemail = object.getString("email");
                                    } catch (Exception e) {
                                        str_facebookemail = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        str_facebookid = object.getString("id");
                                    } catch (Exception e) {
                                        str_facebookid = "";
                                        e.printStackTrace();

                                    }


                                    if (!str_facebookemail.contentEquals("")) {
                                        Log.d("id",str_facebookid);
                                        LogearFb(str_facebookid, str_facebookname, str_facebookemail);
                                    }
                                    else{
                                        Utilidades.Toast(getApplicationContext(),
                                                "EL FACEBOOK INGRESADO NO CONTIENE UN EMAIL");
                                    }


                                } catch (Exception e) {
                                    Log.e("ERROR->",e.getMessage().toString());


                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("public_profile,email"));
                        facebookLogin();

                    }
                }).executeAsync();


            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logInWithReadPermissions(
                        (Activity) login.this, Arrays.asList("public_profile,email"));
            }
        });
    }

    private void LogearFb(final String str_facebookid, final String str_facebookname, final String str_facebookemail) {
            Log.d("id",str_facebookid);


        String url = Utilidades.server+"api/public/api/login/nuevo" ;

        final ProgressDialog cargando;
        cargando  = ProgressDialog.show(this,
                "Procesando Registro","Por Favor Espere ..",false,false );


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        cargando.dismiss();
                        String rpta= response.toString();
                        Log.d("Response", rpta);
                        if (rpta.equals("1"))
                        {
                            CargarDataCuenta(str_facebookemail);
                        }

                        else{
                            CargarDataCuenta(str_facebookemail);
                            Next();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        cargando.dismiss();
                        Log.d("Error.Response",error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tipodoc", "FB_ID");
                params.put("nrodoc", str_facebookid);
                params.put("nombre", str_facebookname);
                params.put("genero","");
                params.put("fecha","");
                params.put("email",str_facebookemail);
                params.put("clave",str_facebookid);

                return params;
            }
        };
        queue.add(postRequest);


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Log.d("error",e.getMessage().toString());

        }

    }


    public  void CargarDataCuenta(String email) {
        int cant = obj.CantidadTabla(Utilidades.table_user);
        Log.d("cant", String.valueOf(cant));
        if (cant == 0) {
            String url = Utilidades.server + "api/public/api/cliente/"+email;
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
                                    String imgUrlFb =  "https://graph.facebook.com/"+
                                            json.getString("NroDocumento")+
                                            "/picture?type=large";

                                    valores.put(Utilidades.table_user_nrodoc, json.getString("NroDocumento"));
                                    valores.put(Utilidades.table_user_tipo_doc, json.getString("tipoDocumento"));
                                    valores.put(Utilidades.table_user_nombre, json.getString("Nombre"));
                                    valores.put(Utilidades.table_user_email, json.getString("Email"));
                                    valores.put(Utilidades.table_user_fechaNac, json.getString("FechaNac"));
                                    valores.put(Utilidades.table_user_genero, json.getString("Genero"));
                                    valores.put(Utilidades.table_user_imagen_fb, imgUrlFb);

                                    Log.d("IMG FB ->",imgUrlFb);

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


    private void InsertarDataUser(ContentValues valores) {
        obj.Insert(Utilidades.table_user, Utilidades.table_user_nrodoc, valores);
        Next();
    }

    private void Next() {
        try {
            Intent siguiente = new Intent(this, categorias.class);
            startActivity(siguiente);
        }catch (Exception e){
            Log.d("error",e.getMessage().toString());
        }}


    @Override
    public void onClick(View view) {
        if (boolean_login) {
            boolean_login = false;
            LoginManager.getInstance().logOut();

        } else {
            LoginManager.getInstance().
                    logInWithReadPermissions(login.this, Arrays.asList("public_profile,email"));
            facebookLogin();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }





        @Override
    public void onBackPressed() {
        finishAffinity();
    }





    public void pasar(View v ){
        Intent siguiente  = new Intent(this,loginUser.class);
        startActivity(siguiente);
    }



}
