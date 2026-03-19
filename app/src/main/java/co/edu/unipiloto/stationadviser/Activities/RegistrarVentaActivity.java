package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class RegistrarVentaActivity extends AppCompatActivity {

    Spinner spinnerTipo;
    EditText editLitros, editPrecio;
    Button buttonGuardar;
    DatabaseHelper db;

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

            if(litrosStr.isEmpty() || precioStr.isEmpty()){
                Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show();
                return;
            }

            double litros = Double.parseDouble(litrosStr);
            double precio = Double.parseDouble(precioStr);

            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date());

            boolean resultado = db.registrarVenta(tipo, litros, precio, fecha);

            if(resultado){
                Toast.makeText(this,"Venta registrada",Toast.LENGTH_SHORT).show();
                editLitros.setText("");
                editPrecio.setText("");
            }else{
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}