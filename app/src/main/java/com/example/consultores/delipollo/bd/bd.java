package com.example.consultores.delipollo.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class bd {
    String db = "bd_delipollo";
    ConexionSQLiteHelper conexion  ;
    SQLiteDatabase database,databaseRead;
    // where KEY_NAME + "=" + name
        public bd(Context context ) {
        conexion = new ConexionSQLiteHelper(context,db,null,1);
        database  =conexion.getWritableDatabase();
        databaseRead = conexion.getReadableDatabase();
        }

    public Cursor ReadTable(String table) {
                String sql= "SELECT * FROM "+table;
                Cursor cursor = databaseRead.rawQuery(sql,null);
                return cursor;

            }



    public Cursor ReadTableWhere(String table,String IdColumnName,String value) {
        Cursor cursor=null;
        try {
             cursor = databaseRead.rawQuery(
                     "SELECT * FROM " + table + " WHERE " + IdColumnName + " = ? ",
                    new String[]{value});
        }
        catch (Exception e){
            Log.d("error",e.getMessage());
        }
        return cursor;
    }


        public void LimpiarTable(String table) {
            String sql= "DELETE FROM  "+table;
            database.execSQL(sql);
        }



    public void Insert(String tabla ,String id , ContentValues valores){
          try{
          database.insert(tabla,id,valores);}
          catch (Exception e){
              Log.d("Error SQLite",e.getMessage().toString());
          }
      }
        public void Update(String tabla , ContentValues valores,String where ){
            try {
                database.update(tabla, valores, where, null);
            }catch (Exception e){
                Log.d("Error SQLite",e.getMessage().toString());


            }
        }

        public void Delete(String tabla ,String where ){
            try {
                database.delete(tabla, where, null);
            }
            catch (Exception e){
                Log.d("Error SQLite",e.getMessage().toString());

            }

            }


            public  void CerrarConexion(){
                database.close();
            }

    public int CantidadTabla(String table){
        int numRows = (int) DatabaseUtils.longForQuery(databaseRead, "SELECT COUNT(*) FROM "+table, null);
        return  numRows;
    }


    }



