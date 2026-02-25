package co.edu.unipiloto.stationadviser;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ListaEstacionesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_estaciones);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewEstaciones);
        datos = new ArrayList<>();

        cargarEstaciones();
    }

    private void cargarEstaciones() {
        List<Estacion> estaciones = dbHelper.obtenerTodasLasEstaciones();
        datos.clear();

        if (estaciones.isEmpty()) {
            datos.add("No hay estaciones registradas.");
        } else {
            for (Estacion e : estaciones) {
                datos.add("Nombre: " + e.getNombre() + "\nNIT: " + e.getNit() + "\nUbicación: " + e.getUbicacion());
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEstaciones(); // recargar al volver
    }
}