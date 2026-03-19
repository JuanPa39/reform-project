package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class RegistrarInventarioActivity extends AppCompatActivity {

    Spinner spinnerTipo;
    EditText editCantidad;
    Button buttonGuardar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_inventario);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        editCantidad = findViewById(R.id.editCantidad);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        db = new DatabaseHelper(this);

        // Spinner opciones
        String[] tipos = {"Corriente", "Diesel", "Extra"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                tipos
        );

        spinnerTipo.setAdapter(adapter);

        buttonGuardar.setOnClickListener(v -> {

            String tipo = spinnerTipo.getSelectedItem().toString();
            String cantidadStr = editCantidad.getText().toString();

            if(cantidadStr.isEmpty()){
                Toast.makeText(this,"Ingrese cantidad",Toast.LENGTH_SHORT).show();
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);

            boolean resultado = db.registrarInventario(tipo, cantidad);

            if(resultado){
                Toast.makeText(this,"Inventario guardado",Toast.LENGTH_SHORT).show();
                editCantidad.setText("");
            }else{
                Toast.makeText(this,"Error al guardar",Toast.LENGTH_SHORT).show();
            }
        });
    }
}