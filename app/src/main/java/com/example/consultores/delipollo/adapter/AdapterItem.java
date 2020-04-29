package com.example.consultores.delipollo.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.Categoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolderDatos>
implements View.OnClickListener

{

    Activity activity;
    ArrayList<Categoria> category;
    private  View.OnClickListener listener;

    public AdapterItem(Activity activity,ArrayList<Categoria> category) {
        this.activity = activity;
        this.category = category;

    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.asignarDatos(category,position);


    }

    @Override
    public int getItemCount() {
        try{
        return category.size();
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
        TextView tittle;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            tittle = (TextView) itemView.findViewById(R.id.txt_title);

        }



        public void asignarDatos(ArrayList<Categoria> category, int position) {
         //   CargarImgWeb(category.get(position).getUrlImagen(),img);
            Log.d("IMG->",category.get(position).getUrlImagen());
            Picasso.with(activity).load(category.get(position).getUrlImagen())
                    .placeholder(R.drawable.ic_carrito_rojo).
                    error(R.drawable.ic_carrito_rojo).
                    into(img);

            tittle.setText(category.get(position).getTitle());


        }







    }
}



