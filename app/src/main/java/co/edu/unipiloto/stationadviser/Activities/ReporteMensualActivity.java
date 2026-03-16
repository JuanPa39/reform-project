package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.R;

public class ReporteMensualActivity extends AppCompatActivity {

    Button buttonGenerar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_mensual);

        buttonGenerar = findViewById(R.id.buttonGenerar);

        buttonGenerar.setOnClickListener(v ->
                Toast.makeText(this,"Reporte generado",Toast.LENGTH_SHORT).show()
        );
    }
}