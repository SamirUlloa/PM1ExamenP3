package com.aplicacion.pm1examenp3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.pm1examenp3.Clases.SQLiteConexion;
import com.aplicacion.pm1examenp3.Clases.Transacciones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvPeriocidad;
    EditText txtDescripcion, txtCantidad, txtPeriocidad;
    Spinner spTiempo;
    Button btnSQL, btnFoto, btnlista;
    Bitmap imagenGlobal;
    String seleccion = "";
    String spTiem; //Contenido del item seleccionado en el Spinner.

    ImageView ObjImagen;
    String CurrentPhotoPath;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        tvPeriocidad = (TextView) findViewById(R.id.tvPeriocidad);
        txtPeriocidad = (EditText) findViewById(R.id.txtPeriocidad);
        spTiempo = (Spinner) findViewById(R.id.spTiempo);

        btnSQL = (Button) findViewById(R.id.btnSQL);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        btnlista = (Button) findViewById(R.id.btnLista);
        ObjImagen = (ImageView) findViewById(R.id.ObjImagen);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tiempo, android.R.layout.simple_spinner_item);
        spTiempo.setAdapter(adapter);

        spTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seleccion = adapterView.getItemAtPosition(i).toString();

                //txtPeriocidad.setText(seleccion);

                String s = txtPeriocidad.getText().toString().trim();

                String Text = String.valueOf(spTiempo.getSelectedItem());

                //Toast.makeText(getApplicationContext(), "" + Text, Toast.LENGTH_SHORT).show();

                //if (Text == "Seleccione"){
                 // tvPeriocidad.setText("Seleccione");
                //}

                //tvPeriocidad.setText(Text);
                /*if (adapterView.getItemAtPosition(i).toString().trim() == "Horas"){
                    Toast.makeText(getApplicationContext(), "Horas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Dias", Toast.LENGTH_SHORT).show();
                }

                if (s == "Horas"){
                    //tvPeriocidad.setText("Horas1");
                    Toast.makeText(getApplicationContext(), "horas", Toast.LENGTH_SHORT).show();
                } else if (s == "Diaria"){
                    //tvPeriocidad.setText("Dias2");
                    Toast.makeText(getApplicationContext(), "dias", Toast.LENGTH_SHORT).show();
                } else {
                //    txtPeriocidad.setText("sin tiempo");
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Boton para guardar en la BD
        btnSQL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!txtDescripcion.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty()
                        && ObjImagen.getDrawable() != null)
                {
                    agregarPictureSQL();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Boton para tomar la foto
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        //Boton para ver la lista de Registros
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListado.class);
                startActivity(intent);
            }
        });
    }

    private void agregarPictureSQL(){

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        spTiem = spTiempo.getSelectedItem().toString();

        if (spTiem == "") {
            Toast.makeText(getApplicationContext(), "Seleccione el Tiempo en el Spinner", Toast.LENGTH_LONG).show();
        } else {

            ContentValues values = new ContentValues();

            values.put(Transacciones.descripcion, txtDescripcion.getText().toString());
            values.put(Transacciones.cantidad, txtCantidad.getText().toString());
            values.put(Transacciones.tiempo, spTiem.toString());
            values.put(Transacciones.periocidad, txtPeriocidad.getText().toString());
            values.put(Transacciones.pathImage, CurrentPhotoPath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream(10480);

            imagenGlobal.compress(Bitmap.CompressFormat.JPEG, 0, baos);

            byte[] blob = baos.toByteArray();

            values.put(Transacciones.image, blob);

            Long result = db.insert(Transacciones.tablafotos, Transacciones.id, values);

            Toast.makeText(getApplicationContext(), "Registro exitoso " + result.toString()
                    , Toast.LENGTH_LONG).show();

            db.close();

            LimpiarPatalla();
        }
    }

    //Limpiar Pantalla
    private void LimpiarPatalla()
    {
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPeriocidad.setText("");
        ObjImagen.setImageBitmap(null);
        imagenGlobal = null;
    }

    //Permisos
    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAM);
        }
        else
        {
            dispatchTakePictureIntent();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.toString();
            }
            // Continue only if the File was successfully created
            try {
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.aplicacion.pm1examenp3.fileprovider",

                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, TAKE_PIC_REQUEST);
                }
            }catch (Exception e){
                Log.i("Error", "dispatchTakePictureIntent: " + e.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso a la camara", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Byte[] arreglo;

        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bitmap image = BitmapFactory.decodeFile(CurrentPhotoPath);
            imagenGlobal = image;
            ObjImagen.setImageBitmap(image);

            Toast.makeText(getApplicationContext(), "Registro de imagen exitoso en almacenamiento "
                    ,Toast.LENGTH_LONG).show();
        }
    }
}