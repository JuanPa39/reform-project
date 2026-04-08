package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;
import co.edu.unipiloto.stationadviser.Rules.ReglasCombustible;

public class RegistrarVentaActivity extends AppCompatActivity {

    private Spinner spinnerTipo;
    private EditText editLitros, editPrecio;
    private Button buttonGuardar;
    private DatabaseHelper db;
    private double litrosAPagar; // Variable para mantener el valor si el usuario confirma

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        editLitros = findViewById(R.id.editLitros);
        editPrecio = findViewById(R.id.editPrecio);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        db = new DatabaseHelper(this);

        String[] tipos = {"Corriente", "Diesel", "Extra"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                tipos
        );

        spinnerTipo.setAdapter(adapter);

        buttonGuardar.setOnClickListener(v -> {
            String tipo = spinnerTipo.getSelectedItem().toString();
            String litrosStr = editLitros.getText().toString();
            String precioStr = editPrecio.getText().toString();

            if (litrosStr.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double litros = Double.parseDouble(litrosStr);
            double precio = Double.parseDouble(precioStr);
            litrosAPagar = litros;

            // Verificar si hay suficiente inventario
            verificarInventarioAntesDeVender(tipo, litros, precio);
        });
    }

    private void verificarInventarioAntesDeVender(String tipo, double litros, double precio) {
        int cantidadActual = db.verificarNivelCombustible(tipo);

        // Verificar si hay suficiente inventario
        if (cantidadActual < litros) {
            Toast.makeText(this, "No hay suficiente inventario. Disponible: " + cantidadActual + " litros", Toast.LENGTH_LONG).show();
            mostrarAlertaBajoNivel(tipo, cantidadActual);
            return;
        }

        // Verificar si después de la venta quedará por debajo del mínimo
        int cantidadRestante = (int) (cantidadActual - litros);

        if (ReglasCombustible.isNivelBajo(tipo, cantidadRestante)) {
            // Mostrar advertencia pero permitir la venta
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ Alerta de inventario bajo")
                    .setMessage("Después de esta venta, " + tipo + " quedará en " + cantidadRestante +
                            " litros (mínimo recomendado: " + ReglasCombustible.getUmbralMinimo(tipo) + " L).\n\n" +
                            "¿Desea continuar con la venta?")
                    .setPositiveButton("Continuar", (dialog, which) -> {
                        realizarVenta(tipo, litros, precio);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        } else {
            realizarVenta(tipo, litros, precio);
        }
    }

    private void realizarVenta(String tipo, double litros, double precio) {
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());

        boolean resultado = db.registrarVenta(tipo, litros, precio, fecha);

        if (resultado) {
            Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show();
            editLitros.setText("");
            editPrecio.setText("");

            // Verificar niveles después de la venta
            verificarNivelesPostVenta();
        } else {
            Toast.makeText(this, "Error al registrar la venta", Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarNivelesPostVenta() {
        List<String> alertas = db.verificarTodosLosNiveles();
        for (String alerta : alertas) {
            Toast.makeText(this, alerta, Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarAlertaBajoNivel(String tipo, int cantidadActual) {
        String mensaje = ReglasCombustible.getMensajeAlerta(tipo, cantidadActual);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}