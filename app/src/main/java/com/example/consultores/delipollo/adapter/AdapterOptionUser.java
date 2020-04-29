package com.example.consultores.delipollo.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.OptionUser;

import java.util.ArrayList;

public class AdapterOptionUser extends RecyclerView.Adapter<AdapterOptionUser.ViewHolderDatos>
implements View.OnClickListener

{

    Activity activity;
    ArrayList<OptionUser> optionUser;
    private  View.OnClickListener listener;

    public AdapterOptionUser(Activity activity, ArrayList<OptionUser> optionUser) {
        this.activity = activity;
        this.optionUser = optionUser;

    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_user,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.asignarDatos(optionUser,position);


    }

    @Override
    public int getItemCount() {
        try{
        return optionUser.size();
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



        public void asignarDatos(ArrayList<OptionUser> optionUser, int position) {
         //   CargarImgWeb(optionUser.get(position).getUrlImagen(),img);

            tittle.setText(optionUser.get(position).getTitle());
            img.setImageResource(optionUser.get(position).getIcon());

        }







    }
}



