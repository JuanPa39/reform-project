package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.Model.Estacion;
import co.edu.unipiloto.stationadviser.R;

public class NotificacionInconsistenciasActivity extends AppCompatActivity {

    private Spinner spinnerEstacion;
    private EditText etInconsistencia;
    private Button btnEnviar;
    private LinearLayout contenedorHistorial;
    private DatabaseHelper db;
    private List<Estacion> listaEstaciones;
    private String estacionSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_inconsistencias);

        spinnerEstacion = findViewById(R.id.spinnerEstacion);
        etInconsistencia = findViewById(R.id.etInconsistencia);
        btnEnviar = findViewById(R.id.btnEnviar);
        contenedorHistorial = findViewById(R.id.contenedorHistorial);

        db = new DatabaseHelper(this);

        cargarEstacionesEnSpinner();
        cargarHistorial();

        btnEnviar.setOnClickListener(v -> enviarNotificacion());
    }

    private void cargarEstacionesEnSpinner() {
        listaEstaciones = db.obtenerTodasLasEstaciones();
        List<String> nombresEstaciones = new ArrayList<>();

        for (Estacion e : listaEstaciones) {
            nombresEstaciones.add(e.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresEstaciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstacion.setAdapter(adapter);

        spinnerEstacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estacionSeleccionada = listaEstaciones.get(position).getNombre();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void enviarNotificacion() {
        String inconsistencia = etInconsistencia.getText().toString().trim();

        if (estacionSeleccionada.isEmpty()) {
            Toast.makeText(this, "Selecciona una estación", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inconsistencia.isEmpty()) {
            Toast.makeText(this, "Describe la inconsistencia", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        boolean exito = db.registrarNotificacion(estacionSeleccionada, inconsistencia, fecha);

        if (exito) {
            Toast.makeText(this, "Notificación enviada correctamente", Toast.LENGTH_SHORT).show();
            etInconsistencia.setText("");
            contenedorHistorial.removeAllViews();
            cargarHistorial();
        } else {
            Toast.makeText(this, "Error al enviar la notificación", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarHistorial() {
        List<String[]> notificaciones = db.obtenerNotificaciones();

        if (notificaciones.isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("No hay notificaciones registradas.");
            tvVacio.setTextColor(0xFFAAAAAA);
            tvVacio.setPadding(0, 8, 0, 8);
            contenedorHistorial.addView(tvVacio);
            return;
        }

        for (String[] n : notificaciones) {
            String estacion = n[0];
            String inconsistencia = n[1];
            String estado = n[2];
            String fecha = n[3];

            CardView card = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 12);
            card.setLayoutParams(params);
            card.setCardBackgroundColor(0xFF1A2D42);
            card.setRadius(16f);
            card.setCardElevation(4f);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(28, 20, 28, 20);

            TextView tvEstacion = new TextView(this);
            tvEstacion.setText("Estación: " + estacion);
            tvEstacion.setTextColor(0xFFFFFFFF);
            tvEstacion.setTextSize(14f);

            TextView tvInconsistencia = new TextView(this);
            tvInconsistencia.setText(inconsistencia);
            tvInconsistencia.setTextColor(0xFFAAAAAA);
            tvInconsistencia.setTextSize(13f);
            tvInconsistencia.setPadding(0, 6, 0, 6);

            TextView tvEstado = new TextView(this);
            tvEstado.setText("● " + estado);
            tvEstado.setTextColor(0xFF4CAF50); // verde = enviada
            tvEstado.setTextSize(12f);

            TextView tvFecha = new TextView(this);
            tvFecha.setText(fecha);
            tvFecha.setTextColor(0xFF2196F3);
            tvFecha.setTextSize(11f);

            layout.addView(tvEstacion);
            layout.addView(tvInconsistencia);
            layout.addView(tvEstado);
            layout.addView(tvFecha);
            card.addView(layout);
            contenedorHistorial.addView(card);
        }
    }
}