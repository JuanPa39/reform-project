package co.edu.unipiloto.stationadviser.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.Model.Estacion;
import co.edu.unipiloto.stationadviser.R;
import co.edu.unipiloto.stationadviser.Rules.ReglasCombustible;

public class RegistrarVentaActivity extends AppCompatActivity {

    private Spinner spinnerEstacion, spinnerTipo;
    private EditText editLitros, editPrecio;
    private Button buttonGuardar;
    private DatabaseHelper db;
    private List<Estacion> listaEstaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        spinnerEstacion = findViewById(R.id.spinnerEstacion);
        spinnerTipo     = findViewById(R.id.spinnerTipo);
        editLitros      = findViewById(R.id.editLitros);
        editPrecio      = findViewById(R.id.editPrecio);
        buttonGuardar   = findViewById(R.id.buttonGuardar);

        db = new DatabaseHelper(this);

        // Cargar estaciones desde la BD
        listaEstaciones = db.obtenerTodasLasEstaciones();
        String[] nombresEstaciones = new String[listaEstaciones.size()];
        for (int i = 0; i < listaEstaciones.size(); i++) {
            nombresEstaciones[i] = listaEstaciones.get(i).getNombre();
        }
        ArrayAdapter<String> adapterEstacion = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, nombresEstaciones
        );
        spinnerEstacion.setAdapter(adapterEstacion);

        // Tipos de combustible
        String[] tipos = {"Corriente", "Diesel", "Extra"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, tipos
        );
        spinnerTipo.setAdapter(adapterTipo);

        buttonGuardar.setOnClickListener(v -> {
            String tipo      = spinnerTipo.getSelectedItem().toString();
            String litrosStr = editLitros.getText().toString();
            String precioStr = editPrecio.getText().toString();

            if (litrosStr.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double litros = Double.parseDouble(litrosStr);
            double precio = Double.parseDouble(precioStr);

            // Obtener estación seleccionada
            int posEstacion       = spinnerEstacion.getSelectedItemPosition();
            Estacion estacion     = listaEstaciones.get(posEstacion);
            String nombreEstacion = estacion.getNombre();

            verificarInventarioAntesDeVender(tipo, litros, precio, nombreEstacion);
        });
    }

    private void verificarInventarioAntesDeVender(String tipo, double litros, double precio, String nombreEstacion) {
        int cantidadActual = db.verificarNivelCombustible(tipo);

        if (cantidadActual < litros) {
            Toast.makeText(this, "No hay suficiente inventario. Disponible: " + cantidadActual + " litros", Toast.LENGTH_LONG).show();
            mostrarAlertaBajoNivel(tipo, cantidadActual);
            return;
        }

        int cantidadRestante = (int) (cantidadActual - litros);

        if (ReglasCombustible.isNivelBajo(tipo, cantidadRestante)) {
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ Alerta de inventario bajo")
                    .setMessage("Después de esta venta, " + tipo + " quedará en " + cantidadRestante +
                            " litros (mínimo recomendado: " + ReglasCombustible.getUmbralMinimo(tipo) + " L).\n\n" +
                            "¿Desea continuar con la venta?")
                    .setPositiveButton("Continuar", (dialog, which) -> realizarVenta(tipo, litros, precio, nombreEstacion))
                    .setNegativeButton("Cancelar", null)
                    .show();
        } else {
            realizarVenta(tipo, litros, precio, nombreEstacion);
        }
    }

    private void realizarVenta(String tipo, double litros, double precio, String nombreEstacion) {
        String numeroFactura = "INV-" + System.currentTimeMillis();
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());

        boolean resultado = db.registrarVenta(numeroFactura, tipo, litros, precio, fecha, nombreEstacion);

        if (resultado) {
            Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show();

            // Verificar niveles después de la venta
            verificarNivelesPostVenta();

            // Ir a factura electrónica
            Intent facturaIntent = new Intent(RegistrarVentaActivity.this, FacturaElectronicaActivity.class);
            facturaIntent.putExtra("numeroFactura", numeroFactura);
            facturaIntent.putExtra("fecha", fecha);
            facturaIntent.putExtra("tipo", tipo);
            facturaIntent.putExtra("litros", litros);
            facturaIntent.putExtra("precioUnitario", precio);
            facturaIntent.putExtra("estacion", nombreEstacion);
            startActivity(facturaIntent);

            editLitros.setText("");
            editPrecio.setText("");
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