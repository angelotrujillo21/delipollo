package com.example.consultores.delipollo.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class volley {

    public RequestQueue queue ;
    public   JSONObject data;

    public volley(Context context) {
        queue = Volley.newRequestQueue(context);
    }



    public void GetData(String url,final VolleyCallback callback){
        try {

            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String err = (error.getMessage() == null) ? "Fallo en la conexion° " : error.getMessage();
                            Log.d("Error.Response", err);

                        }
                    }

            );
            queue.add(getRequest);



            Log.d("RESPONSE  DARA ->>", data.toString());

        }catch (Exception e){
            Log.d("ERROR CONSULTA", e.getMessage());

        }
    }




    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }

}
