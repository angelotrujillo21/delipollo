package com.example.consultores.delipollo.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.comprasProd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterCarrito extends RecyclerView.Adapter<adapterCarrito.ViewHolderDatos>
        implements View.OnClickListener

{
    EventoCambio inter;
    Activity activity;
    ArrayList<comprasProd> compra;
    private  View.OnClickListener listener;



    public adapterCarrito(Activity activity,ArrayList<comprasProd> compra,EventoCambio inter) {
        this.activity = activity;
        this.compra = compra;
        this.inter = inter;

    }

    public interface EventoCambio {
        void cambio(ArrayList<comprasProd> compra);
        void delete(String id);
        void importe(double acu);
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido,null,false);
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
        ImageButton btn_delete;
        Button btn_sumar , btn_restar ;
        ImageView img ;
        TextView txt_descripcion,txt_detalle,txt_precio,txt_cantidad;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt_descripcion = (TextView) itemView.findViewById(R.id.txt_descripcion);
            txt_detalle = (TextView) itemView.findViewById(R.id.txt_detalle);
            txt_precio = (TextView) itemView.findViewById(R.id.txt_precio);
            txt_cantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);


            btn_sumar = (Button) itemView.findViewById(R.id.btn_sumar);
            btn_restar = (Button) itemView.findViewById(R.id.btn_restar);
            btn_delete = (ImageButton) itemView.findViewById(R.id.btn_delete);


        }





        public void asignarDatos( final ArrayList<comprasProd> compra, final int position) {

            Picasso.with(activity).load(compra.get(position).getImagen())
                    .placeholder(R.drawable.ic_carrito_rojo).
                    error(R.drawable.ic_carrito_rojo).
                    into(img);

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

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Eliminar")
                            .setMessage("¿ Desea eliminar este Producto ? ")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    inter.delete(compra.get(position).getIdCarrito());
                                    compra.remove(position);
                                    importe();
                                    notifyDataSetChanged();

                                }

                            })
                            .setNegativeButton("No", null)
                            .show();




                }
            });





        }











    }



}
