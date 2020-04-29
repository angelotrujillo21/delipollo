package com.example.consultores.delipollo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.Platillo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterItemPlatillo  extends RecyclerView.Adapter<AdapterItemPlatillo.ViewHolderDatos>
implements View.OnClickListener

{

    Activity activity;
    ArrayList<Platillo> platillo;
    private  View.OnClickListener listener;

    public AdapterItemPlatillo(Activity activity,ArrayList<Platillo> platillos) {
        this.activity = activity;
        this.platillo = platillos;

    }

    @Override
    public AdapterItemPlatillo.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_platillo,null,false);
        view.setOnClickListener(this);
        return new AdapterItemPlatillo.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdapterItemPlatillo.ViewHolderDatos holder, int position) {
        holder.asignarDatos(platillo,position);
    }

    @Override
    public int getItemCount() {
        try{
            return platillo.size();
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
        ImageView img ;
        TextView txt_titulo;
      TextView txt_precio;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt_titulo = (TextView) itemView.findViewById(R.id.txt_titulo_platillo);
            txt_precio = (TextView) itemView.findViewById(R.id.txt_precio);

        }



        public void asignarDatos(ArrayList<Platillo> platillos, int position) {
            //   CargarImgWeb(category.get(position).getUrlImagen(),img);
            Log.d("DETALLE ->",platillos.get(position).getTitle() + " | "+platillos.get(position).getPrecio());

            Picasso.with(activity).load(platillos.get(position).getUrlImagen())
                    .placeholder(R.drawable.ic_carrito_rojo).
                    error(R.drawable.ic_carrito_rojo).
                    into(img);



            txt_titulo.setText(platillos.get(position).getTitle());
           txt_precio.setText(
                  "S./ "+ Utilidades.Format(platillos.get(position).getPrecio()));

        }







    }
    

}