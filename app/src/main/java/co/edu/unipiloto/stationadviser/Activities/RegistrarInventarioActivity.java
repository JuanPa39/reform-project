package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.R;

public class RegistrarInventarioActivity extends AppCompatActivity {

    EditText editTipo, editCantidad;
    Button buttonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_inventario);

        editTipo = findViewById(R.id.editTipo);
        editCantidad = findViewById(R.id.editCantidad);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        buttonGuardar.setOnClickListener(v -> {

            String tipo = editTipo.getText().toString();
            String cantidad = editCantidad.getText().toString();

            if(tipo.isEmpty() || cantidad.isEmpty()){
                Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Inventario registrado",Toast.LENGTH_SHORT).show();
            }

        });
    }
}