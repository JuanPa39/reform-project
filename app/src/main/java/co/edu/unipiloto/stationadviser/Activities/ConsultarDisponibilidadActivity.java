package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class ConsultarDisponibilidadActivity extends AppCompatActivity {

    Spinner spinnerTipo;
    Spinner spinnerTipoVehiculo;
    EditText editLitros;
    Button buttonConsultar;
    TextView textResultado;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_disponibilidad);

        // Inicializar vistas
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);  // Nuevo
        editLitros = findViewById(R.id.editLitros);
        buttonConsultar = findViewById(R.id.buttonConsultar);
        textResultado = findViewById(R.id.textResultado);

        db = new DatabaseHelper(this);

        // Spinner de tipo de combustible
        String[] tiposCombustible = {"Corriente", "Diesel", "Extra"};
        ArrayAdapter<String> adapterCombustible = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                tiposCombustible
        );
        spinnerTipo.setAdapter(adapterCombustible);

        // Spinner de tipo de vehículo (según Decreto 1428/2025)
        String[] tiposVehiculo = {"Particular", "Taxi", "Servicio Público (Bus)", "Camión de carga", "Oficial", "Diplomático", "Moto"};
        ArrayAdapter<String> adapterVehiculo = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                tiposVehiculo
        );
        spinnerTipoVehiculo.setAdapter(adapterVehiculo);

        // Botón consultar
        buttonConsultar.setOnClickListener(v -> {
            String tipoCombustible = spinnerTipo.getSelectedItem().toString();
            String tipoVehiculo = spinnerTipoVehiculo.getSelectedItem().toString();
            String litrosStr = editLitros.getText().toString();

            if (litrosStr.isEmpty()) {
                Toast.makeText(this, "Ingrese litros", Toast.LENGTH_SHORT).show();
                return;
            }

            double litros = Double.parseDouble(litrosStr);
            int disponible = db.obtenerCantidadDisponible(tipoCombustible);
            boolean subsidio = db.aplicaSubsidio(tipoVehiculo, tipoCombustible, litros);

            String resultado = "Disponible: " + disponible + " litros\n";

            if (disponible >= litros) {
                resultado += "✔ Puede comprar\n";
            } else {
                resultado += "❌ No hay suficiente combustible\n";
            }

            if (subsidio) {
                resultado += "💰 Aplica subsidio según Decreto 1428/2025";
            } else {
                resultado += "🚫 No aplica subsidio según Decreto 1428/2025";
            }

            textResultado.setText(resultado);
        });
    }
}