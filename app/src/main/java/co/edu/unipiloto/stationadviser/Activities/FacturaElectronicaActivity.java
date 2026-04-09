package co.edu.unipiloto.stationadviser.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

import co.edu.unipiloto.stationadviser.R;

public class FacturaElectronicaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_electronica);

        // Recibir datos
        String numeroFactura = getIntent().getStringExtra("numeroFactura");
        String fecha         = getIntent().getStringExtra("fecha");
        String tipo          = getIntent().getStringExtra("tipo");
        String estacion      = getIntent().getStringExtra("estacion");
        double litros        = getIntent().getDoubleExtra("litros", 0);
        double precio        = getIntent().getDoubleExtra("precioUnitario", 0);

        // Calcular valores
        double subtotal = litros * precio;
        double iva      = subtotal * 0.19;
        double total    = subtotal + iva;

        // Conectar TextViews
        TextView tvNumero   = findViewById(R.id.tvNumeroFactura);
        TextView tvEstacion = findViewById(R.id.tvEstacion);        // NUEVO
        TextView tvFecha    = findViewById(R.id.tvFecha);
        TextView tvTipo     = findViewById(R.id.tvTipoCombustible);
        TextView tvLitros   = findViewById(R.id.tvLitros);
        TextView tvPrecio   = findViewById(R.id.tvPrecioUnitario);
        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        TextView tvIva      = findViewById(R.id.tvIva);
        TextView tvTotal    = findViewById(R.id.tvTotal);

        // Asignar valores a la pantalla
        tvNumero.setText("Número: " + numeroFactura);
        tvEstacion.setText("Estación: " + estacion);               // NUEVO
        tvFecha.setText("Fecha: " + fecha);
        tvTipo.setText("Tipo: " + tipo);
        tvLitros.setText(String.format("Litros: %.2f L", litros));
        tvPrecio.setText(String.format("Precio unitario: $ %.2f", precio));
        tvSubtotal.setText(String.format("Subtotal: $ %.2f", subtotal));
        tvIva.setText(String.format("IVA (19%%): $ %.2f", iva));
        tvTotal.setText(String.format("TOTAL: $ %.2f", total));

        // Botón compartir PDF
        Button btnCompartir = findViewById(R.id.buttonCompartirPdf);
        btnCompartir.setOnClickListener(v -> {
            try {
                File pdfFile = PdfGenerator.generarFacturaPdf(
                        this, numeroFactura, fecha, estacion, tipo, litros, precio // ACTUALIZADO
                );

                Uri pdfUri = FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        pdfFile
                );

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Factura " + numeroFactura);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(shareIntent, "Compartir factura via..."));

            } catch (Exception e) {
                Toast.makeText(this, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Botón finalizar
        Button btnFinalizar = findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(v -> finish());
    }
}