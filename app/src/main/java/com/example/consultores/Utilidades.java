package com.example.consultores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.util.Categoria;
import com.example.consultores.delipollo.util.Platillo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class Utilidades {
   //public static  final String server ="http://192.168.0.32/";
   public static  final String server ="https://mmconsultoresinformaticos.net/";

    public static final ArrayList<Categoria> category = null;
    public static final ArrayList<Platillo> Platillo = null;
    public static  ArrayList<Platillo> getPlatillo() {
        return Platillo;
    }

    public static ArrayList<Categoria> getCategory() {
        return category;
    }

    public static String date ="";



    public static String  Format(String nro) {
        String format  = String.format(Locale.ENGLISH,"%1$,.2f",Double.parseDouble(nro));
        return format;
    }



    public static void  Toast(Context tc , String data) {
        Toast.makeText(tc,data,Toast.LENGTH_SHORT).show();
    }


    public static  String GetDate(){
       date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        return  date ;
    }


    public static String FormtCantidad(double d) {
        NumberFormat nf = new DecimalFormat("##.###");
        return nf.format(d);
    }

    public static void LimpiarApp(Context context) {
        bd obj = new bd(context);
        obj.LimpiarTable(Utilidades.table_user);
        obj.LimpiarTable(Utilidades.table_venta);
        obj.LimpiarTable(Utilidades.table_serie_numeros);
        obj.CerrarConexion();
    }

    public static void SerieCorrelativoGrupoVenta(Context context) {
        bd obj = new bd(context);
        ContentValues valores  = new ContentValues();
        valores.put(Utilidades.table_serie_numeros_descripcion,"GrupoVenta");
        valores.put(Utilidades.table_serie_numeros_correlativo,0);
        obj.Insert(Utilidades.table_serie_numeros,Utilidades.table_serie_numeros_id,valores);
        obj.CerrarConexion();

    }



    public static int GetSerieCorrelativoGrupoVenta(Context context) {
       int id=0;
        bd obj = new bd(context);
        Cursor cursor =
        obj.ReadTable(Utilidades.table_serie_numeros);

        while (cursor.moveToNext())
        {
            Log.e("grupo1" , cursor.getString(0));
            Log.e("grupo2" , cursor.getString(2));
            Log.e("grupo2" , String.valueOf(cursor.getInt(2)));

            id = (cursor.getInt(2));
        }
        id++;
        obj.CerrarConexion();
        return  id;

    }



    public static void UpdateSerieCorrelativoGrupoVenta(Context context,int correlativo) {
        bd obj = new bd(context);
        ContentValues valores  = new ContentValues();
        valores.put(Utilidades.table_serie_numeros_correlativo,correlativo);
        String where = Utilidades.table_serie_numeros_id +" = " + "1";

        obj.Update(Utilidades.table_serie_numeros,valores,where);
        obj.CerrarConexion();
    }

    //campos tabla productos_venta
    public static  final String table_venta ="venta";
    public static  final String venta_campo_codigoVenta ="codVenta";
    public static  final String venta_campo_codigoCategoria ="codCategoria";
    public static  final String venta_campo_codigoProd ="codProd";
    public static  final String venta_campo_DescripcionProd ="descripcion";
    public static  final String venta_campo_Detalle ="detalle";
    public static  final String venta_campo_precio ="precio";
    public static  final String venta_campo_cantidad ="cantidad";
    public static  final String venta_campo_importe ="importe";
    public static  final String venta_campo_observacion ="observacion";
    public static  final String venta_campo_imagen ="imagen";
    public static  final String venta_campo_fecha ="fecha";
    public static  final String venta_campo_grupo ="grupo";


    public static  final String CREATE_TABLE_VENTA="CREATE TABLE  "
            +Utilidades.table_venta
            + " ( "
            +Utilidades.venta_campo_codigoVenta+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
            +Utilidades.venta_campo_codigoCategoria+ " STRING , "
            +Utilidades.venta_campo_codigoProd+ " STRING , "
            +Utilidades.venta_campo_DescripcionProd+ " STRING , "
            +Utilidades.venta_campo_Detalle+ " STRING , "
            +Utilidades.venta_campo_precio+ " STRING , "
            +Utilidades.venta_campo_cantidad+ " STRING , "
            +Utilidades.venta_campo_importe+ " STRING , "
            +Utilidades.venta_campo_observacion+ " STRING , "
            +Utilidades.venta_campo_imagen+ " STRING , "
            +Utilidades.venta_campo_fecha+ " STRING , "
            +Utilidades.venta_campo_grupo+ " INTEGER  "
            +" ) "
            ;


    //fin de tabla productos_venta



    public static  final String table_user ="user";
    public static  final String table_user_tipo_doc ="tipoDoc";
    public static  final String table_user_nrodoc="nroDoc ";
    public static  final String table_user_nombre="nombre";
    public static  final String table_user_email="email";
    public static  final String table_user_fechaNac="fechaNac";
    public static  final String table_user_genero="genero";
    public static  final String table_user_imagen_fb="img";

    public static  final String CREATE_TABLE_USER="CREATE TABLE  "
            +Utilidades.table_user
            + " ( "
            +Utilidades.table_user_nrodoc+ "INTEGER PRIMARY KEY  , "
            +Utilidades.table_user_tipo_doc+ " STRING  , "
            +Utilidades.table_user_nombre+ " STRING , "
            +Utilidades.table_user_email+ " STRING ,"
            +Utilidades.table_user_fechaNac+ " STRING ,"
            +Utilidades.table_user_genero+ " STRING , "
            +Utilidades.table_user_imagen_fb+ " STRING  "

            +" ) "
            ;




    public static  final String table_serie_numeros ="serienumeros";
    public static  final String table_serie_numeros_id="id";
    public static  final String table_serie_numeros_descripcion="descripcion";
    public static  final String table_serie_numeros_correlativo = "correlativo";


    public static  final String CREATE_TABLE_SERIES_NUMEROS="CREATE TABLE  "
            +Utilidades.table_serie_numeros
            + " ( "
            +Utilidades.table_serie_numeros_id+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  , "
            +Utilidades.table_serie_numeros_descripcion+ " STRING  , "
            +Utilidades.table_serie_numeros_correlativo+ " INTEGER  "
            +" ) "
            ;





}
