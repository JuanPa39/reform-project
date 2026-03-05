package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class ConsultarPrecioActivity extends AppCompatActivity {

    private ListView listViewPrecios;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_precio);

        listViewPrecios = findViewById(R.id.listViewPrecios);
        db = new DatabaseHelper(this);

        List<String> listaPrecios = db.obtenerPreciosCombustible();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaPrecios
        );

        listViewPrecios.setAdapter(adapter);
    }
}