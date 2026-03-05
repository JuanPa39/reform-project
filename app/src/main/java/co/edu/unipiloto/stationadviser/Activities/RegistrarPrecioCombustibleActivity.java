package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import co.edu.unipiloto.stationadviser.R;

public class RegistrarPrecioCombustibleActivity extends AppCompatActivity {

    EditText editTipoCombustible, editPrecio;
    Button buttonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_precio_combustible);

        editTipoCombustible = findViewById(R.id.editTipoCombustible);
        editPrecio = findViewById(R.id.editPrecio);
        buttonGuardar = findViewById(R.id.buttonGuardarPrecio);

        buttonGuardar.setOnClickListener(v -> {

            String tipo = editTipoCombustible.getText().toString();
            String precio = editPrecio.getText().toString();

            Toast.makeText(this, "Precio registrado", Toast.LENGTH_SHORT).show();
        });
    }
}