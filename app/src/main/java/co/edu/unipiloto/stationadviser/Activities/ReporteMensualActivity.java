package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class ReporteMensualActivity extends AppCompatActivity {

    TextView textReporte;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_mensual);

        textReporte = findViewById(R.id.textReporte);
        db = new DatabaseHelper(this);

        List<String> ventas = db.obtenerHistorialVentas();

        double total = 0;

        for(String v : ventas){
            String[] partes = v.split("\\$");
            total += Double.parseDouble(partes[1].split(" ")[0]);
        }

        textReporte.setText("Total ventas: $" + total);
    }
}