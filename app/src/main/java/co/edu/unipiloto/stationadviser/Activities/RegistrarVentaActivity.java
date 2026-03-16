package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.R;

public class RegistrarVentaActivity extends AppCompatActivity {

    EditText editTipo, editLitros, editPrecio;
    Button buttonRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        editTipo = findViewById(R.id.editTipo);
        editLitros = findViewById(R.id.editLitros);
        editPrecio = findViewById(R.id.editPrecio);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        buttonRegistrar.setOnClickListener(v -> {

            String tipo = editTipo.getText().toString();
            String litros = editLitros.getText().toString();
            String precio = editPrecio.getText().toString();

            if(tipo.isEmpty() || litros.isEmpty() || precio.isEmpty()){
                Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Venta registrada",Toast.LENGTH_SHORT).show();
            }

        });
    }
}