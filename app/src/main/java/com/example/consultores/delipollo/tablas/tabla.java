package com.example.consultores.delipollo.tablas;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.consultores.delipollo.R;

import java.util.ArrayList;

/**
 * Created by consultores on 13/06/2019.
 */

public class tabla {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla

    public tabla(Activity actividad, TableLayout tabla)
    {
        this.actividad = actividad;
        this.tabla = tabla;
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }

    public  void agregarCabecera(ArrayList<String> arraycabecera)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);

        COLUMNAS = arraycabecera.size();

        for(int i = 0; i < arraycabecera.size(); i++)
        {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT , TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(arraycabecera.get(i));
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            TextViewCompat.setTextAppearance(texto, R.style.estilo_celda);
            texto.setBackgroundResource(R.drawable.tabla_celda_cabecera);
            texto.setLayoutParams(layoutCelda);
            texto.setTextColor(Color.WHITE);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }


    public void agregarFilaTabla(ArrayList<String> compra)
    {
        TableRow.LayoutParams layoutCelda=null;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< compra.size(); i++)
        {
            TextView texto = new TextView(actividad);
            fila.addView(Texto(texto,layoutCelda,compra.get(i)));


        }


        tabla.addView(fila);
        filas.add(fila);
        FILAS++;
    }
/*
    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }
    */

    private TextView Texto(TextView texto,TableRow.LayoutParams layoutCelda,String valor){

        texto.setText(valor);
        texto.setGravity(Gravity.CENTER_HORIZONTAL);
        TextViewCompat.setTextAppearance(texto, R.style.estilo_celda);
        texto.setBackgroundResource(R.drawable.tabla_celda);
        layoutCelda = new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        texto.setLayoutParams(layoutCelda);
        return  texto;

    }



}


