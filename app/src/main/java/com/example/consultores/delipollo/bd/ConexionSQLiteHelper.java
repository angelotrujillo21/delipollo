package com.example.consultores.delipollo.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.consultores.Utilidades;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREATE_TABLE_SERIES_NUMEROS);
        db.execSQL(Utilidades.CREATE_TABLE_VENTA);
        db.execSQL(Utilidades.CREATE_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.table_venta);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.table_user);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.table_serie_numeros);
        onCreate(db);
    }
}
