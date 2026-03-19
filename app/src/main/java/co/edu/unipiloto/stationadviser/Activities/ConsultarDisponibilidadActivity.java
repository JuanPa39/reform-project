package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class ConsultarDisponibilidadActivity extends AppCompatActivity {

    Spinner spinnerTipo;
    EditText editLitros;
    Button buttonConsultar;
    TextView textResultado;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_disponibilidad);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        editLitros = findViewById(R.id.editLitros);
        buttonConsultar = findViewById(R.id.buttonConsultar);
        textResultado = findViewById(R.id.textResultado);

        db = new DatabaseHelper(this);

        // Spinner
        String[] tipos = {"Corriente", "Diesel", "Extra"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                tipos
        );
        spinnerTipo.setAdapter(adapter);

        buttonConsultar.setOnClickListener(v -> {

            String tipo = spinnerTipo.getSelectedItem().toString();
            String litrosStr = editLitros.getText().toString();

            if(litrosStr.isEmpty()){
                Toast.makeText(this,"Ingrese litros",Toast.LENGTH_SHORT).show();
                return;
            }

            double litros = Double.parseDouble(litrosStr);

            int disponible = db.obtenerCantidadDisponible(tipo);

            boolean subsidio = db.aplicaSubsidio(tipo, litros);

            String resultado = "Disponible: " + disponible + " litros\n";

            if(disponible >= litros){
                resultado += "✔ Puede comprar\n";
            } else {
                resultado += "❌ No hay suficiente combustible\n";
            }

            if(subsidio){
                resultado += "💰 Aplica subsidio";
            } else {
                resultado += "🚫 No aplica subsidio";
            }

            textResultado.setText(resultado);

        });
    }
}