package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class ConsultarNotificacionActivity extends AppCompatActivity {

    private LinearLayout contenedorNotificaciones;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_notificaciones);

        contenedorNotificaciones = findViewById(R.id.contenedorNotificaciones);
        db = new DatabaseHelper(this);

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        List<String[]> notificaciones = db.obtenerNotificaciones();

        if (notificaciones.isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("No hay notificaciones enviadas.");
            tvVacio.setTextColor(0xFFAAAAAA);
            tvVacio.setPadding(0, 8, 0, 8);
            contenedorNotificaciones.addView(tvVacio);
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
            tvEstado.setTextColor(0xFF4CAF50);
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
            contenedorNotificaciones.addView(card);
        }
    }
}