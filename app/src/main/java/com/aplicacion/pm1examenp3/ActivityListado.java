package com.aplicacion.pm1examenp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aplicacion.pm1examenp3.Clases.Medicamentos;
import com.aplicacion.pm1examenp3.Clases.SQLiteConexion;
import com.aplicacion.pm1examenp3.Clases.Transacciones;

import java.util.ArrayList;

public class ActivityListado extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView lista;
    ArrayList<Medicamentos> listaempleados;
    ArrayList<String> ArregloEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        conexion  = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        lista = (ListView) findViewById(R.id.listMedicamentos);

        ObtenerListaEmpleados();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloEmpleados);
        lista.setAdapter(adp);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarPicture(i);
            }
        });
    }

    private void mostrarPicture(int i){

       Medicamentos medicamentos = listaempleados.get(i);

        Bundle bundle = new Bundle();
        bundle.putSerializable("empleados", medicamentos);

        Intent intent = new Intent(getApplicationContext(), ActivityDetalle.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void ObtenerListaEmpleados()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Medicamentos list_emple = null;
        listaempleados = new ArrayList<Medicamentos>();

        Cursor cursor = db.rawQuery(Transacciones.SELECT_ALL_TABLE_PICTURE,null);

        while(cursor.moveToNext())
        {
            list_emple = new Medicamentos();

            list_emple.setId(cursor.getInt(0));
            list_emple.setDescripcion(cursor.getString(1));
            list_emple.setCantidad(cursor.getString(2));
            list_emple.setTiempo(cursor.getString(3));
            list_emple.setPeriocidad(cursor.getString(4));
            list_emple.setPathImage(cursor.getString(5));
            list_emple.setImage(cursor.getBlob(6));

            listaempleados.add(list_emple);
        }

        cursor.close();

        llenalista();
    }

    private void llenalista()
    {
        ArregloEmpleados = new ArrayList<String>();

        for(int i=0; i<listaempleados.size(); i++)
        {
            ArregloEmpleados.add(listaempleados.get(i).getId() +" | "+
                    listaempleados.get(i).getDescripcion() +" | "+
                    listaempleados.get(i).getCantidad() +" | "+
                    listaempleados.get(i).getTiempo() +" | "+
                    listaempleados.get(i).getPeriocidad());
        }

    }
}