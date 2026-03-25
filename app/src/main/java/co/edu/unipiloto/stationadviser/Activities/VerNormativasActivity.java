package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class VerNormativasActivity extends AppCompatActivity {

    private LinearLayout contenedorNormativas;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_normativas);

        contenedorNormativas = findViewById(R.id.contenedorNormativas);
        db = new DatabaseHelper(this);

        cargarNormativas();
    }

    private void cargarNormativas() {
        List<String[]> normativas = db.obtenerNormativas();

        if (normativas.isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("No hay normativas registradas.");
            tvVacio.setTextColor(0xFFCCCCCC);
            tvVacio.setPadding(16, 16, 16, 16);
            contenedorNormativas.addView(tvVacio);
            return;
        }

        for (String[] normativa : normativas) {
            String titulo = normativa[0];
            String descripcion = normativa[1];
            String fecha = normativa[2];

            // Card contenedor
            CardView card = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            card.setLayoutParams(cardParams);
            card.setCardBackgroundColor(0xFF1A2D42);
            card.setRadius(16f);
            card.setCardElevation(4f);

            // Layout interno
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(32, 24, 32, 24);

            // Título
            TextView tvTitulo = new TextView(this);
            tvTitulo.setText(titulo);
            tvTitulo.setTextColor(0xFFFFFFFF);
            tvTitulo.setTextSize(15f);
            tvTitulo.setPadding(0, 0, 0, 8);

            // Descripción
            TextView tvDescripcion = new TextView(this);
            tvDescripcion.setText(descripcion);
            tvDescripcion.setTextColor(0xFFAAAAAA);
            tvDescripcion.setTextSize(13f);
            tvDescripcion.setPadding(0, 0, 0, 8);

            // Fecha
            TextView tvFecha = new TextView(this);
            tvFecha.setText("Vigente desde: " + fecha);
            tvFecha.setTextColor(0xFF2196F3);
            tvFecha.setTextSize(12f);

            layout.addView(tvTitulo);
            layout.addView(tvDescripcion);
            layout.addView(tvFecha);
            card.addView(layout);
            contenedorNormativas.addView(card);
        }
    }
}