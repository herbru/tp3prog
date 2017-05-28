package com.example.gabriel.tp2programacion;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

    public  DbHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory fabrica, int version){
        super(contexto, nombre, fabrica, version);
    }

    @Override
    public void onCreate(SQLiteDatabase DB){
        //creo la tabla personas con una columna "nombre"
        String CreartablaPersonas = "create table Personas (Nombre text)";
        DB.execSQL(CreartablaPersonas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase Db, int versionAnterior, int versionNueva){

    }
}
