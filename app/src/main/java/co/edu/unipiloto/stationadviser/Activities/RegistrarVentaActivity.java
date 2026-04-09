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

    private TextView tvEstacionAsignada;
    private Spinner spinnerTipo;
    private EditText editLitros, editPrecio;
    private Button buttonGuardar;
    private DatabaseHelper db;
    private String nombreEstacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        tvEstacionAsignada = findViewById(R.id.tvEstacionAsignada);
        spinnerTipo        = findViewById(R.id.spinnerTipo);
        editLitros         = findViewById(R.id.editLitros);
        editPrecio         = findViewById(R.id.editPrecio);
        buttonGuardar      = findViewById(R.id.buttonGuardar);

        db = new DatabaseHelper(this);

        // Obtener estación asignada al empleado
        int estacionId = getIntent().getIntExtra("estacionId", 0);
        if (estacionId > 0) {
            Estacion estacion = db.getEstacionById(estacionId);
            if (estacion != null) {
                nombreEstacion = estacion.getNombre();
                tvEstacionAsignada.setText("Estación: " + nombreEstacion);
            }
        } else {
            tvEstacionAsignada.setText("Estación: No asignada");
            nombreEstacion = "Sin estación";
        }

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

            verificarInventarioAntesDeVender(tipo, litros, precio);
        });
    }

    private void verificarInventarioAntesDeVender(String tipo, double litros, double precio) {
        int cantidadActual = db.verificarNivelCombustible(tipo);

        if (cantidadActual < litros) {
            Toast.makeText(this, "No hay suficiente inventario. Disponible: "
                    + cantidadActual + " litros", Toast.LENGTH_LONG).show();
            return;
        }

        int cantidadRestante = (int) (cantidadActual - litros);

        if (ReglasCombustible.isNivelBajo(tipo, cantidadRestante)) {
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ Alerta de inventario bajo")
                    .setMessage("Después de esta venta, " + tipo + " quedará en "
                            + cantidadRestante + " litros (mínimo recomendado: "
                            + ReglasCombustible.getUmbralMinimo(tipo) + " L).\n\n"
                            + "¿Desea continuar con la venta?")
                    .setPositiveButton("Continuar", (dialog, which) ->
                            realizarVenta(tipo, litros, precio))
                    .setNegativeButton("Cancelar", null)
                    .show();
        } else {
            realizarVenta(tipo, litros, precio);
        }
    }

    private void realizarVenta(String tipo, double litros, double precio) {
        String numeroFactura = "INV-" + System.currentTimeMillis();
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());

        boolean resultado = db.registrarVenta(numeroFactura, tipo, litros, precio,
                fecha, nombreEstacion);

        if (resultado) {
            Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show();

            List<String> alertas = db.verificarTodosLosNiveles();
            for (String alerta : alertas) {
                Toast.makeText(this, alerta, Toast.LENGTH_LONG).show();
            }

            Intent facturaIntent = new Intent(RegistrarVentaActivity.this,
                    FacturaElectronicaActivity.class);
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
}