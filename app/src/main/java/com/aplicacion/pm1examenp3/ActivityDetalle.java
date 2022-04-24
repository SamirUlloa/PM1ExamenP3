package com.aplicacion.pm1examenp3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aplicacion.pm1examenp3.Clases.Medicamentos;
import com.aplicacion.pm1examenp3.Clases.SQLiteConexion;
import com.aplicacion.pm1examenp3.Clases.Transacciones;

import java.io.ByteArrayInputStream;

public class ActivityDetalle extends AppCompatActivity {

    ImageView imageViewMostrarIMG;
    EditText id, Descripcion, Cantidad, Tiempo, Periocidad;
    Button btnActializar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        imageViewMostrarIMG = (ImageView) findViewById(R.id.IMGVMostrarFotoP);
        id = (EditText) findViewById(R.id.txtMostId);
        Descripcion = (EditText) findViewById(R.id.txtMostNombre);
        Cantidad = (EditText) findViewById(R.id.txtMostDescripcion);
        Tiempo = (EditText) findViewById(R.id.txtMostTiempo);
        Periocidad = (EditText) findViewById(R.id.txtMostPeriocidad);
        Bundle objetEnvia = getIntent().getExtras();
        Medicamentos imagen = null;

        if(objetEnvia != null){
            imagen = (Medicamentos) objetEnvia.getSerializable("empleados");

            String idRec = "" + imagen.getId();
            id.setText(idRec);
            Descripcion.setText(imagen.getDescripcion());
            Cantidad.setText(imagen.getCantidad());
            Tiempo.setText(imagen.getTiempo());
            Periocidad.setText(imagen.getPeriocidad());
            mostrarImagen(imagen.getImage());
            Bitmap image = BitmapFactory.decodeFile(imagen.getPathImage());
            imageViewMostrarIMG.setImageBitmap(image);
        }

        //Boton para actualizar Medicamentos
        btnActializar = (Button) findViewById(R.id.btnActualizar);
        btnActializar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarMedicamento();
            }
        });

        //Boton para Eliminar Medicamentos
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarMedicamento();
            }
        });
    }

    private void mostrarImagen(byte[] image) {
        Bitmap bitmap = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        bitmap = BitmapFactory.decodeStream(bais);
        imageViewMostrarIMG.setImageBitmap(bitmap);
    }

    public void actualizarMedicamento(){
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        String cod = id.getText().toString().trim();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.descripcion, Descripcion.getText().toString());
        valores.put(Transacciones.cantidad, Cantidad.getText().toString());
        valores.put(Transacciones.tiempo, Tiempo.getText().toString());
        valores.put(Transacciones.periocidad, Periocidad.getText().toString());

        if (!id.getText().toString().isEmpty()){
            db.update("fotos", valores, "id=" + cod, null);
            Toast.makeText(this, "Se Actualizo el Registro: " + cod, Toast.LENGTH_LONG).show();
        }
    }

    public void eliminarMedicamento(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetalle.this);
        builder.setMessage("Esta seguro que desea eliminar el Registro:? " + id.getText().toString())
                .setTitle("Atención");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SQLiteConexion conexion = new SQLiteConexion(ActivityDetalle.this, Transacciones.NameDatabase,null, 1);
                SQLiteDatabase db = conexion.getWritableDatabase();

                String cod = id.getText().toString();

                db.delete("fotos", "id=" + cod, null);
                Toast.makeText(ActivityDetalle.this, "Registro " + cod + " Eliminado Correctamente", Toast.LENGTH_LONG).show();

                db.close();

                Intent intent = new Intent(ActivityDetalle.this, ActivityListado.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}