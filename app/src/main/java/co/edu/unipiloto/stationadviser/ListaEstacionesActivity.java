package co.edu.unipiloto.stationadviser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Estacion> estacionesList; // Guardar objetos completos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_estaciones);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewEstaciones);
        datos = new ArrayList<>();
        estacionesList = new ArrayList<>();

        cargarEstaciones();

        // Al hacer clic en una estación, ir a editar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Estacion estacion = estacionesList.get(position);
                Intent intent = new Intent(ListaEstacionesActivity.this, RegistrarEstacionActivity.class);
                intent.putExtra("modo_edicion", true);
                intent.putExtra("estacion_id", estacion.getId());
                intent.putExtra("estacion_nombre", estacion.getNombre());
                intent.putExtra("estacion_nit", estacion.getNit());
                intent.putExtra("estacion_ubicacion", estacion.getUbicacion());
                startActivity(intent);
            }
        });
    }

    private void cargarEstaciones() {
        estacionesList = dbHelper.obtenerTodasLasEstaciones();
        datos.clear();

        if (estacionesList.isEmpty()) {
            datos.add("No hay estaciones registradas.");
        } else {
            for (Estacion e : estacionesList) {
                datos.add("Nombre: " + e.getNombre() + "\nNIT: " + e.getNit() +
                        "\nUbicación: " + e.getUbicacion() + "\n(Toca para editar)");
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEstaciones(); // Recargar al volver
    }
}