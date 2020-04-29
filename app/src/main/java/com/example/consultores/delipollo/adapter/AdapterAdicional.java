package com.example.consultores.delipollo.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.R;
import com.example.consultores.delipollo.util.SectionOrRow;
import com.example.consultores.delipollo.util.comprasProd;

import java.util.ArrayList;

public class AdapterAdicional extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    AdapterAdicional.EventoCambio inter;
    private ArrayList<SectionOrRow> mData = new ArrayList<SectionOrRow>();
    //private  View.OnClickListener listener;


    public AdapterAdicional(ArrayList<SectionOrRow> data, AdapterAdicional.EventoCambio inter ) {
        this.mData=data;
        this.inter=inter;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        SectionOrRow item = mData.get(position);
        if(!item.isRow()) {
            return 0;
        } else {
            return 1;
        }
    }


    public interface EventoCambio {
        void ListaAcionalTerminada(ArrayList<comprasProd> list);
        void Update();
        void importe(double acu);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("viewtype",String.valueOf(viewType));

        if(viewType==0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adicional_label, parent, false);
            return new SectionViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adicional, parent, false);
            return new RowViewHolder(v);
        }

    }



    public class RowViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_descripcion,txt_precio,txt_cantidad;
        private Button btn_sumar ,btn_restar ;
        private LinearLayout contenedor;
        public RowViewHolder(View itemView) {
            super(itemView);
            txt_descripcion = (TextView) itemView.findViewById(R.id.txt_descripcion);
            txt_precio = (TextView) itemView.findViewById(R.id.txt_precio);
            txt_cantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);
            btn_sumar = (Button) itemView.findViewById(R.id.btn_sumar);
            btn_restar = (Button) itemView.findViewById(R.id.btn_restar);
            contenedor = (LinearLayout) itemView.findViewById(R.id.single_post_circuito_linearbox);

        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_section;
        public SectionViewHolder(View itemView) {
            super(itemView);
            txt_section = (TextView) itemView.findViewById(R.id.section_label);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
        final SectionOrRow item = mData.get(position);
        if(item.isRow()) {

            final RowViewHolder h = (RowViewHolder) holder;
            double cantidad = Double.parseDouble(item.getRow().getCantidad());



            h.txt_descripcion.setText(item.getRow().getDescripcion());
             h.txt_cantidad.setText(
                     Utilidades.FormtCantidad(
                             cantidad
                     ));
            double monto = Double.parseDouble(item.getRow().getMonto());
            if (monto==0){
                h.txt_precio.setText(
                        "S./"+ Utilidades.Format(
                                item.getRow().getPrecio()
                        )
                );

            }else{
                h.txt_precio.setText(
                        "S./"+ Utilidades.Format(
                                item.getRow().getMonto()
                        )
                );

            }



            if (cantidad>0){
                h.contenedor.setBackgroundColor(Color.parseColor("#e8f8f5"));
            }else{
                h.contenedor.setBackgroundColor(Color.parseColor("#fdfefe"));
            }







          LogicRow(
                  h.txt_descripcion,
                  h.txt_precio,
                  h.txt_cantidad,
                  h.btn_sumar,
                  h.btn_restar,
                  h.contenedor,
                  item.getRow()
                  );




        } else {
            SectionViewHolder h = (SectionViewHolder) holder;
            h.txt_section.setText(item.getSection());
        }}
        catch (Exception e){
            Log.d("error",e.getMessage());
        }
    }





    private void LogicRow(TextView txt_descripcion,
                          TextView txt_precio,
                          final TextView txt_cantidad,
                          Button btn_sumar,
                          Button btn_restar,
                          final LinearLayout contenedor,
                          final comprasProd row) {


        btn_sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double cant =
                        Double.parseDouble(txt_cantidad.getText().toString())
                                +1;
                double precio = cant*Double.parseDouble(
                        row.getPrecio());
                CambiarDatos(row,cant,precio,true);


            }
        });

        btn_restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double cant =
                        Double.parseDouble(txt_cantidad.getText().toString())
                                -1;
                if (cant>0) {
                    double precio = cant * Double.parseDouble(row.getPrecio());
                    CambiarDatos(row,cant,precio,false);
                }
                if (cant==0){
                    CambiarDatos(row,0,
                            Double.parseDouble(row.getPrecio())
                           ,false);
                }

            }
        });



    }



    public void CambiarDatos(comprasProd row, double cant, double precio, Boolean estado){
        try {
            row.setCantidad(String.valueOf(cant));
            row.setMonto(String.valueOf(String.valueOf(precio)));
            inter.Update();
            ImporteAdicionalTotal();



        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }
    }

    public void ImporteAdicionalTotal() {
        double acu = 0 ;
        for (int i = 0; i < mData.size() ; i++) {
            if (mData.get(i).isRow()){
           if (Double.parseDouble(mData.get(i).getRow().getCantidad())>0){
            acu+=Double.parseDouble(
                    mData.get(i).getRow().getMonto()
            );
            }
            }
        }
        inter.importe(acu);
    }



    public void ObjctAdcionales() {
        ArrayList<comprasProd> list = new  ArrayList<comprasProd>();
        double acu = 0 ;
        for (int i = 0; i < mData.size() ; i++) {
            if (mData.get(i).isRow()){
                if (Double.parseDouble(mData.get(i).getRow().getCantidad())>0){
                    list.add(mData.get(i).getRow());
                }
            }
        }
        inter.ListaAcionalTerminada(list);
    }






}
