package com.example.gabriel.tp2programacion;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageButton[] arrayBotones;

    DbHelper accesoDb;
    SQLiteDatabase Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaro el array de botones
        arrayBotones = new ImageButton[9];
        arrayBotones[0] = (ImageButton) findViewById(R.id.btnUno);
        arrayBotones[1] = (ImageButton) findViewById(R.id.btnDos);
        arrayBotones[2] = (ImageButton) findViewById(R.id.btnTres);
        arrayBotones[3] = (ImageButton) findViewById(R.id.btnCuatro);
        arrayBotones[4] = (ImageButton) findViewById(R.id.btnCinco);
        arrayBotones[5] = (ImageButton) findViewById(R.id.btnSeis);
        arrayBotones[6] = (ImageButton) findViewById(R.id.btnSiete);
        arrayBotones[7] = (ImageButton) findViewById(R.id.btnOcho);
        arrayBotones[8] = (ImageButton) findViewById(R.id.btnNueve);

    }

    public boolean DbAbierta() {
        boolean responder = false;
        //declaro el helper y la base de datos
        accesoDb = new  DbHelper(this, "baseTP3", null, 1);
        Db = accesoDb.getWritableDatabase();

        //verifico que exista, comporbando que no sea null
        if (Db != null){
            responder = true;
        }
        return responder;
    }

    public boolean YaJugo(String Nombre){
        //entro al if si hay una DB
        if (DbAbierta()){
            //ejecuto una consulta que devuelve los registros
            Cursor Registros = Db.rawQuery("select Nombre from Personas", null);
            //si hay registros entro al if y la repetitiva
            if (Registros.moveToFirst()){
                //leo los registros hasta que encuentre un nombre igual al ingresado o que termine de recorer los registros
                do{
                    //si el nombre ingresado es igual al del registro devuelvo true
                    if (Nombre.compareTo(Registros.getString(0)) == 0){
                        return  true;
                    }
                }while(Registros.moveToNext());
            }
        }
        Db.close();
        //si no encontre un nombre igual o no pude abrir la Db devuelvo false
        return  false;
    }

    public void InsertarUsuario(String Nombre){
        if (DbAbierta()){
            //instancio un registro nuevo
            ContentValues nuevoRegistro = new ContentValues();
            //lleno el registro instanciado con el nombre pasado como parametro
            nuevoRegistro.put("Nombre", Nombre);
            //ejecuto la consulta insert con el registro nuevo
            Db.insert("Personas", null, nuevoRegistro);
        }
        Db.close();
    }

    public boolean verificarSiGano() {

        boolean sonIguales = true;
        Drawable.ConstantState codigoPosic0 = arrayBotones[0].getDrawable().getConstantState();
        for (ImageButton imgbtn : arrayBotones) {
            Drawable.ConstantState codigoImagenBtn = imgbtn.getDrawable().getConstantState();

            if (codigoPosic0 != codigoImagenBtn) sonIguales = false;
        }
        return sonIguales;
    }

    public void invertirImagenBoton(int nroBotonPresionado) {

        //obtengo el codigo de la imagen azul
        Drawable.ConstantState codigoImagenAzul = ContextCompat.getDrawable(this, R.drawable.azul).getConstantState();

        //obtengo el codigo de la imagen del imageview
        Drawable.ConstantState codigoImageBtn = arrayBotones[nroBotonPresionado].getDrawable().getConstantState();
        if (codigoImagenAzul == codigoImageBtn) {
            arrayBotones[nroBotonPresionado].setImageResource(R.drawable.rojo);
        } else {
            arrayBotones[nroBotonPresionado].setImageResource(R.drawable.azul );
        }
    }

    public void generar(View vista) {
        TextView txtYaJugo = (TextView)findViewById(R.id.txtYaJugo);
        txtYaJugo.setVisibility(View.VISIBLE);

        EditText txtNombre = (EditText)findViewById(R.id.txtNombre);

        LinearLayout linear1 = (LinearLayout)findViewById(R.id.Linear1);
        LinearLayout linear2 = (LinearLayout)findViewById(R.id.Linear2);
        LinearLayout linear3 = (LinearLayout)findViewById(R.id.Linear3);

        linear1.setVisibility(View.VISIBLE);
        linear2.setVisibility(View.VISIBLE);
        linear3.setVisibility(View.VISIBLE);

        //verifico si el usuario ya jugo antes
        if (YaJugo(txtNombre.getText().toString())){
            txtYaJugo.setText("Usted ya jugo antes");
        }
        else{
            InsertarUsuario(txtNombre.getText().toString());
            txtYaJugo.setText("Es la primera vez que juega");
        }

        //instancio e incializo un generador de randoms
        Random generadorRandom;
        generadorRandom = new Random();

        boolean estaBien = false;

        //genero un array de numeros, 1 o 0, y verifico que todas las posiciones no sean iguales
        while (!estaBien) {
            for (ImageButton imgbtn : arrayBotones) {
                int nroRandom = generadorRandom.nextInt(2);

                if (nroRandom == 0) {
                    imgbtn.setImageResource(R.drawable.azul);
                } else {
                    imgbtn.setImageResource(R.drawable.rojo);
                }
            }
            //verifico si todas las imagenes son iguales
            estaBien = !verificarSiGano();
        }
    }

    public void Actualizar(View vista) {

        //obtengo el imagebutton que fue presionado
        ImageButton botonPresionado = (ImageButton) vista;

        //obtengo el tag en tipo string del boton botonPresionado
        String tagBotonPresionado = botonPresionado.getTag().toString();

        //convierto el tag en int
        int numeroBotonPresionado = Integer.parseInt(tagBotonPresionado);
        switch (numeroBotonPresionado) {
            //invierto los botones de acuerdo al boton presionado
            case 0:
                invertirImagenBoton(0);
                invertirImagenBoton(1);
                invertirImagenBoton(3);
                invertirImagenBoton(4);
                break;
            case 1:
                invertirImagenBoton(1);
                invertirImagenBoton(0);
                invertirImagenBoton(2);
                invertirImagenBoton(4);
                break;
            case 2:
                invertirImagenBoton(2);
                invertirImagenBoton(1);
                invertirImagenBoton(4);
                invertirImagenBoton(5);
                break;
            case 3:
                invertirImagenBoton(3);
                invertirImagenBoton(0);
                invertirImagenBoton(4);
                invertirImagenBoton(6);
                break;
            case 4:
                invertirImagenBoton(4);
                invertirImagenBoton(1);
                invertirImagenBoton(3);
                invertirImagenBoton(7);
                invertirImagenBoton(5);
                break;
            case 5:
                invertirImagenBoton(5);
                invertirImagenBoton(2);
                invertirImagenBoton(4);
                invertirImagenBoton(8);
                break;
            case 6:
                invertirImagenBoton(6);
                invertirImagenBoton(3);
                invertirImagenBoton(7);
                invertirImagenBoton(4);
                break;
            case 7:
                invertirImagenBoton(7);
                invertirImagenBoton(6);
                invertirImagenBoton(8);
                invertirImagenBoton(4);
                break;
            case 8:
                invertirImagenBoton(8);
                invertirImagenBoton(5);
                invertirImagenBoton(7);
                invertirImagenBoton(4);
                break;
        }
        if (verificarSiGano()) {
            Toast.makeText(this, "GANASTEEE!!!!!", Toast.LENGTH_SHORT).show();
        }

    }
}
