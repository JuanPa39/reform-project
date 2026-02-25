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
    private TextView textViewMensaje, textViewTitulo;

    private boolean modoEdicion = false;
    private int estacionId = -1;

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
        textViewTitulo = findViewById(R.id.textViewTitulo);

        // Verificar si venimos en modo edición
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("modo_edicion", false)) {
            modoEdicion = true;
            estacionId = extras.getInt("estacion_id", -1);

            // Cambiar UI para modo edición
            textViewTitulo.setText("Editar Estación");
            buttonRegistrar.setText("Actualizar Estación");

            // Cargar datos existentes
            editTextNombre.setText(extras.getString("estacion_nombre"));
            editTextNit.setText(extras.getString("estacion_nit"));
            editTextUbicacion.setText(extras.getString("estacion_ubicacion"));

            // El NIT no debería editarse (es único) - opcional
            // editTextNit.setEnabled(false);
        }

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modoEdicion) {
                    actualizarEstacion();
                } else {
                    registrarEstacion();
                }
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
            limpiarCampos();
        } else {
            textViewMensaje.setText("Error: El NIT ya existe o hubo un problema");
        }
    }

    private void actualizarEstacion() {
        String nombre = editTextNombre.getText().toString().trim();
        String nit = editTextNit.getText().toString().trim();
        String ubicacion = editTextUbicacion.getText().toString().trim();

        if (nombre.isEmpty() || nit.isEmpty() || ubicacion.isEmpty()) {
            textViewMensaje.setText("Todos los campos son obligatorios");
            return;
        }

        boolean exito = dbHelper.updateEstacion(estacionId, nombre, nit, ubicacion);

        if (exito) {
            Toast.makeText(this, "Estación actualizada con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Volver a la lista
        } else {
            textViewMensaje.setText("Error al actualizar");
        }
    }

    private void limpiarCampos() {
        editTextNombre.setText("");
        editTextNit.setText("");
        editTextUbicacion.setText("");
        textViewMensaje.setText("");
    }
}