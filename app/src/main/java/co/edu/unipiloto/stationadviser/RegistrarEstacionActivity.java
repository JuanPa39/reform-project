package co.edu.unipiloto.stationadviser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrarEstacionActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editTextNombre, editTextNit, editTextUbicacion;
    private Button buttonRegistrar;
    private TextView textViewMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_estacion);

        dbHelper = new DatabaseHelper(this);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextNit = findViewById(R.id.editTextNit);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        textViewMensaje = findViewById(R.id.textViewMensaje);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEstacion();
            }
        });
    }

    private void registrarEstacion() {
        String nombre = editTextNombre.getText().toString().trim();
        String nit = editTextNit.getText().toString().trim();
        String ubicacion = editTextUbicacion.getText().toString().trim();

        if (nombre.isEmpty() || nit.isEmpty() || ubicacion.isEmpty()) {
            textViewMensaje.setText("Todos los campos son obligatorios");
            return;
        }

        boolean exito = dbHelper.addEstacion(nombre, nit, ubicacion);

        if (exito) {
            Toast.makeText(this, "Estación registrada con éxito", Toast.LENGTH_SHORT).show();
            // Limpiar campos
            editTextNombre.setText("");
            editTextNit.setText("");
            editTextUbicacion.setText("");
            textViewMensaje.setText("");
        } else {
            textViewMensaje.setText("Error: El NIT ya existe o hubo un problema");
        }
    }
}