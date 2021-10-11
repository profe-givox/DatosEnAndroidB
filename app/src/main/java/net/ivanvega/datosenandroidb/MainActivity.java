package net.ivanvega.datosenandroidb;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RadioGroup  radioGroupTipoAlamacenamiento;
    EditText txtTexto;
    Button btnAbrir, btnGuardar;

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    guardarArchivoEnExterna();
                } else {
//                     Explain to the user that the feature is unavailable because the
//                     features requires a permission that the user has denied. At the
//                     same time, respect the user's decision. Don't link to system
//                     settings in an effort to convince the user to change their
//                     decision.

                    Toast.makeText(this, "Explain to the user that the feature is unavailable because the\n" +
                            "                     features requires a permission that the user has denied. At the\n" +
                            "                     same time, respect the user's decision. Don't link to system\n" +
                            "                     settings in an effort to convince the user to change their\n" +
                            "                     decision",
                            Toast.LENGTH_LONG).show();

                    inhabilitarAlmExt();

                }
            });

    private void inhabilitarAlmExt() {
        btnGuardar.setEnabled(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        inicilizarUI();
    }

    private void inicilizarUI() {
        radioGroupTipoAlamacenamiento = 
                findViewById(R.id.radioGroupAlamacenamiento);
        
        txtTexto = findViewById(R.id.txtTexto);
        btnAbrir = findViewById(R.id.btnAbrir);
        btnGuardar = findViewById(R.id.btnGUardar);
        
        btnAbrir.setOnClickListener(view -> abriArchivo());
        btnGuardar.setOnClickListener(view -> guardarArchivo());
        
    }

    private void guardarArchivo() {
        if(radioGroupTipoAlamacenamiento.getCheckedRadioButtonId()
        == R.id.optExterna
        ){
            validarPermisoEscrituraExterna();
        }

    }

    private void validarPermisoEscrituraExterna() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
            guardarArchivoEnExterna();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                Toast.makeText(this, "Este permiso es necesario para almacenar" +
                        "el archivo ne la memoria externa", Toast.LENGTH_LONG).show();

                requestPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void guardarArchivoEnExterna() {
        File rootExternal =  getExternalFilesDir(null);

        File file = new File(rootExternal, "MiArchiv.txt");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(txtTexto.getText().toString().getBytes());

            fileOutputStream.close();

            txtTexto.setText("");

            Toast.makeText(this, "Archivo guardado",
                    Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void abriArchivo() {
        if (this.radioGroupTipoAlamacenamiento.getCheckedRadioButtonId() ==
        R.id.optExterna
        ){
            abriArchivoExterno();
        }
    }

    private void abriArchivoExterno() {

        try {
            FileInputStream fileInputStream = new FileInputStream(
                    new File(getExternalFilesDir(null), "MiArchiv.txt"));

            String texto = "";
            int caracter;

            do{

                   caracter  = fileInputStream.read();
                   if (caracter != -1)
                       texto += (char)caracter;
            }while(caracter!=-1);

            fileInputStream.close();

            txtTexto.setText(texto);

            Toast.makeText(this, "Archivo Abierto",
                    Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}