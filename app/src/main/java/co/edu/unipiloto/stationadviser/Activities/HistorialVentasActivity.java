package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import co.edu.unipiloto.stationadviser.R;

public class HistorialVentasActivity extends AppCompatActivity {

    TextView textHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ventas);

        textHistorial = findViewById(R.id.textHistorial);

        textHistorial.setText("Historial de ventas\n\nVenta 1 - 20L\nVenta 2 - 15L\nVenta 3 - 30L");

    }
}