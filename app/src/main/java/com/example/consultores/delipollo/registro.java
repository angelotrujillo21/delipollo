package com.example.consultores.delipollo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.R.style.Theme_Holo_Light_Dialog_MinWidth;

public class registro extends AppCompatActivity {
    Spinner spinner,spinnerGenero;
    String[] opcionesTipoDoc = {"DNI","RUC"};
    String[] opcionesGenero = {"Femenino","Masculino"};

    TextInputLayout nombre ;
    TextInputLayout apellidos ;
    TextInputLayout email ;
    TextInputLayout pass ;
    TextInputLayout numero ;

    RequestQueue queue;
     DatePickerDialog.OnDateSetListener  listenerFecha;

    TextInputEditText fechaEditext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
           try{
               queue = Volley.newRequestQueue(this);  // this = context

               spinner= (Spinner) findViewById(R.id.select_tipo);
               spinnerGenero = (Spinner) findViewById(R.id.select_genero);
               fechaEditext  = (TextInputEditText) findViewById(R.id.fechaEditext);

               nombre = (TextInputLayout) findViewById(R.id.nombre);
               apellidos = (TextInputLayout) findViewById(R.id.apellidos);
               email = (TextInputLayout) findViewById(R.id.email);
               pass = (TextInputLayout) findViewById(R.id.password);
               numero = (TextInputLayout) findViewById(R.id.nrodoc);



               DialogFecha();

               spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opcionesTipoDoc));
               spinnerGenero.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opcionesGenero));


               listenerFecha = new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                       month = month+1;
                       String  date = dayOfMonth+" / "+month+" / "+year;
                       fechaEditext.setText(date);

                   }
               };





           }catch (Exception e){
            Log.d("Error",e.getMessage());
        }


    }

    private void DialogFecha() {
        fechaEditext.setInputType(InputType.TYPE_NULL);


      fechaEditext.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              if(MotionEvent.ACTION_UP == event.getAction()) {
                  showDatePickerDialog();
              }

              return true; // return is important.
          }
      });




    }


    private void showDatePickerDialog() {
        try {
            Calendar c_start = Calendar.getInstance();

            int year = c_start.get(Calendar.YEAR);
            int month = c_start.get(Calendar.MONTH);
            int day = c_start.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    this, Theme_Holo_Light_Dialog_MinWidth,listenerFecha,year,month,day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();




        }
        catch (Exception e){
            Log.d("error",e.getMessage().toString());
        }





    }




    public Boolean ValidarNombre(){
       String Texto =  nombre.getEditText().getText().toString().trim();

        if (Texto.isEmpty()){
            nombre.setError("INGRESE DATOS AL CAMPO");
            return  false;
        } else {
            nombre.setError(null);
            return  true;
        }

    }



    public Boolean ValidarNro(){
        String Texto =  numero.getEditText().getText().toString().trim();

        if (Texto.isEmpty()){
            numero.setError("INGRESE DATOS AL CAMPO");
            return  false;
        } else {
            numero.setError(null);
            return  true;
        }

    }






    public Boolean ValidarApellidos(){
        String Texto =  apellidos.getEditText().getText().toString().trim();

        if (Texto.isEmpty()){
            apellidos.setError("INGRESE DATOS AL CAMPO");
            return  false;
        } else {
            apellidos.setError(null);

            return  true;
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
        String url = Utilidades.server+"api/public/api/login/nuevo" ;

        final ProgressDialog cargando;
        cargando  = ProgressDialog.show(this,
                "Procesando Registro","Por Favor Espere ..",false,false );


        if (ValidarNombre() && ValidarApellidos() && ValidarEmail() && ValidaPass() && ValidarNro()){

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
                                    Utilidades.Toast(getApplicationContext(),"GENIAL ESTAS REGISTRADO ! ");
                                    Intent  login = new Intent(getApplicationContext(),loginUser.class);
                                    startActivity(login);
                                }

                            else{
                                Toast.makeText(getApplicationContext(),
                                       rpta, Toast.LENGTH_LONG).show();
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
                    params.put("tipodoc", spinner.getSelectedItem().toString());
                    params.put("nrodoc", numero.getEditText().getText().toString());
                    params.put("nombre", nombre.getEditText().getText().toString() + " "+
                            apellidos.getEditText().getText().toString());
                    params.put("genero",spinnerGenero.getSelectedItem().toString());
                    params.put("fecha",fechaEditext.getText().toString());
                    params.put("email",email.getEditText().getText().toString());
                    params.put("clave",pass.getEditText().getText().toString());

                    return params;
                }
            };
            queue.add(postRequest);








        } else {
            cargando.dismiss();
            Toast.makeText(getApplicationContext(),"Ingrese Datos",Toast.LENGTH_SHORT).show();
        }



    }




}
