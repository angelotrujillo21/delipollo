package com.example.consultores.delipollo.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.Pedido;

import java.util.ArrayList;

public class adapterPedido extends
        RecyclerView.Adapter<adapterPedido.ViewHolderDatos>
        implements View.OnClickListener

{
    EventoCambio inter;
    Activity activity;
    ArrayList<Pedido> pedido;
    private  View.OnClickListener listener;

        private  int colores[]={
                R.color.chart_value_1,
                R.color.chart_value_2_2,
                R.color.chart_value_3,
                R.color.chart_value_4,

        };

    public adapterPedido(Activity activity, ArrayList<Pedido> pedido,EventoCambio inter) {
        this.activity = activity;
        this.pedido = pedido;
        this.inter = inter;

    }

    public interface EventoCambio {
        void viewMoto(String idPedido);

    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_pagado,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.asignarDatos(pedido,position);


    }

    @Override
    public int getItemCount() {
        try{
            return pedido.size();
        }catch(Exception e){
            return 0;
        }
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



    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView txt_estado,txt_fecha,txt_id_pedido,txt_MetodoPago,txt_total;
        LinearLayout content_1,content_2,content_3,content_4;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            txt_estado = (TextView) itemView.findViewById(R.id.txt_estado);
            txt_fecha = (TextView) itemView.findViewById(R.id.txt_fecha);
            txt_id_pedido = (TextView) itemView.findViewById(R.id.txt_id_pedido);
            txt_MetodoPago = (TextView) itemView.findViewById(R.id.txt_metodoPago);
            txt_total = (TextView) itemView.findViewById(R.id.txt_total);

            content_1 = (LinearLayout) itemView.findViewById(R.id.content_1);
            content_2 = (LinearLayout) itemView.findViewById(R.id.content_2);
            content_3 = (LinearLayout) itemView.findViewById(R.id.content_3);
            content_4 = (LinearLayout) itemView.findViewById(R.id.content_4);


        }



          public void Opcion1(){
              txt_estado.setTextColor(getColor(activity,colores[0]));



              content_1.setVisibility(View.VISIBLE);

              content_2.setVisibility(View.INVISIBLE);
              content_3.setVisibility(View.INVISIBLE);
              content_4.setVisibility(View.INVISIBLE);

          }


        public void Opcion2(){
            txt_estado.setTextColor(getColor(activity,colores[1]));

            content_1.setVisibility(View.VISIBLE);
            content_2.setVisibility(View.VISIBLE);

            content_3.setVisibility(View.INVISIBLE);
            content_4.setVisibility(View.INVISIBLE);

        }


        public void Opcion3(){
            txt_estado.setTextColor(getColor(activity,colores[2]));

            content_1.setVisibility(View.VISIBLE);
            content_2.setVisibility(View.VISIBLE);
            content_3.setVisibility(View.VISIBLE);

            content_4.setVisibility(View.INVISIBLE);

        }

        public void Opcion4(){
            txt_estado.setTextColor(getColor(activity,colores[3]));

            content_1.setVisibility(View.VISIBLE);
            content_2.setVisibility(View.VISIBLE);
            content_3.setVisibility(View.VISIBLE);
            content_4.setVisibility(View.VISIBLE);

        }



        public void asignarDatos( final ArrayList<Pedido> pedido, final int position) {
            txt_estado.setText(pedido.get(position).getDesc_estado());
            txt_id_pedido.setText("N° DE PEDIDO : "+pedido.get(position).getIdPedido());
            txt_fecha.setText(pedido.get(position).getFecha());
            txt_MetodoPago.setText("METODO DE PAGO : "+pedido.get(position).getMetodoPago());
            txt_total.setText("S. /"+pedido.get(position).getMontoTotal());

              int estado = Integer.parseInt(pedido.get(position).getCod_estado());

            if (estado==1){
                Opcion1();
            }else if(estado==2){
                Opcion2();

            }else if(estado==3){
                Opcion3();

            }else if(estado==4){
                Opcion4();

            }


                 ViewMotorizado(pedido.get(position).getIdPedido());

        }



        private void ViewMotorizado(final String idPedido) {
            content_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                inter.viewMoto(idPedido);

                }
            });
        }



    }



    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }

    }




}
