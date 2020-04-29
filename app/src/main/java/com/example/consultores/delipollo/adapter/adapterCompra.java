package com.example.consultores.delipollo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.comprasProd;

import java.util.ArrayList;

public class adapterCompra extends RecyclerView.Adapter<adapterCompra.ViewHolderDatos>
        implements View.OnClickListener

{
    EventoCambio inter;
    Activity activity;
    ArrayList<comprasProd> compra;
    private  View.OnClickListener listener;



    public adapterCompra(Activity activity,
                         ArrayList<comprasProd> compra,
                         EventoCambio inter) {
        this.activity = activity;
        this.compra = compra;
        this.inter = inter;

    }

    public interface EventoCambio {
        void cambio(ArrayList<comprasProd> compra);
        void importe(double acu);

    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adicional,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.asignarDatos(compra,position);

    }

    @Override
    public int getItemCount() {
        try{
            return compra.size();
        }catch(Exception e){
            return 0;
        }
    }


    public  void importe(){
        double acu = 0 ;
        for (int i = 0; i <compra.size() ; i++) {
            acu += Double.parseDouble(compra.get(i).getMonto());
        }
        inter.importe(acu);

    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener= listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }

    }

    public void CambiarDatos( int position, double cant, double precio){
        try {
            this.compra.get(position).setCantidad(String.valueOf(cant));
            this.compra.get(position).setMonto(String.valueOf(String.valueOf(precio)));
            importe();
            notifyDataSetChanged();

            inter.cambio(this.compra);
        }catch (Exception e){
            Log.d("Error",e.getMessage().toString());
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        Button btn_sumar , btn_restar ;
        TextView txt_descripcion,txt_detalle,txt_precio,txt_cantidad;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            txt_descripcion = (TextView) itemView.findViewById(R.id.txt_descripcion);
            txt_precio = (TextView) itemView.findViewById(R.id.txt_precio);
            txt_cantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);
            btn_sumar = (Button) itemView.findViewById(R.id.btn_sumar);
            btn_restar = (Button) itemView.findViewById(R.id.btn_restar);


        }





        public void asignarDatos( final ArrayList<comprasProd> compra, final int position) {
            txt_descripcion.setText(compra.get(position).getDescripcion());
            txt_detalle.setText(compra.get(position).getDetalle());
            txt_cantidad.setText(
                   Utilidades.FormtCantidad(
                        Double.parseDouble(
                                compra.get(position).getCantidad()
                        )
                   )
            );


            txt_precio.setText("S./ "+ Utilidades.Format(compra.get(position).getMonto()));

            btn_sumar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double cant =
                            Double.parseDouble(txt_cantidad.getText().toString())
                            +1;
                    double precio = cant*Double.parseDouble(compra.get(position).getPrecio());
                    CambiarDatos(position,cant,precio);


                }
            });

            btn_restar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double cant =
                            Double.parseDouble(txt_cantidad.getText().toString())
                                    -1;
                    if (cant>0) {
                        double precio = cant * Double.parseDouble(compra.get(position).getPrecio());
                        CambiarDatos(position,cant,precio);
                    }
                }
            });







        }











    }



}
