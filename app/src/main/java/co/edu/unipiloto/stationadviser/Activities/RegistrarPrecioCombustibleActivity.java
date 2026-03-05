package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.Model.Estacion;
import co.edu.unipiloto.stationadviser.R;


public class RegistrarPrecioCombustibleActivity extends AppCompatActivity {

    private Spinner spinnerCombustible, spinnerEstaciones;
    private EditText editPrecio;
    private Button buttonGuardar;

    private DatabaseHelper db;
    private List<Estacion> estaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_precio_combustible);

        spinnerCombustible = findViewById(R.id.spinnerCombustible);
        spinnerEstaciones = findViewById(R.id.spinnerEstaciones);
        editPrecio = findViewById(R.id.editPrecio);
        buttonGuardar = findViewById(R.id.buttonGuardarPrecio);

        db = new DatabaseHelper(this);

        cargarTiposCombustible();
        cargarEstaciones();

        buttonGuardar.setOnClickListener(v -> guardarPrecio());
    }

    private void cargarTiposCombustible() {
        String[] tipos = {"Corriente", "Extra", "Diesel"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,        // ← cambio
                tipos
        );

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);  // ← cambio
        spinnerCombustible.setAdapter(adapter);
    }

    private void cargarEstaciones() {
        estaciones = db.obtenerTodasLasEstaciones();
        List<String> nombres = new ArrayList<>();

        for (Estacion e : estaciones) {
            nombres.add(e.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,        // ← cambio
                nombres
        );

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);  // ← cambio
        spinnerEstaciones.setAdapter(adapter);
    }

    private void guardarPrecio() {

        String tipo = spinnerCombustible.getSelectedItem().toString();
        String precioTexto = editPrecio.getText().toString();

        if (precioTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el precio", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioTexto);

        int posicion = spinnerEstaciones.getSelectedItemPosition();
        Estacion estacionSeleccionada = estaciones.get(posicion);

        boolean resultado = db.registrarPrecio(
                tipo,
                precio,
                estacionSeleccionada.getId()
        );

        if (resultado) {
            Toast.makeText(this, "Precio registrado", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar precio", Toast.LENGTH_SHORT).show();
        }
    }
}