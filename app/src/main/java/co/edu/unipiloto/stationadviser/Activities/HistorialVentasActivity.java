package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class HistorialVentasActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ventas);

        listView = findViewById(R.id.listView);
        db = new DatabaseHelper(this);

        List<String> lista = db.obtenerHistorialVentas();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lista
        );

        listView.setAdapter(adapter);
    }
}