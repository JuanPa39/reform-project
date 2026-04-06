package co.edu.unipiloto.stationadviser.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.R;

public class FacturaElectronicaActivity extends AppCompatActivity {

    private TextView tvNumeroFactura, tvFecha, tvTipoCombustible, tvLitros,
            tvPrecioUnitario, tvSubtotal, tvIva, tvTotal;
    private Button btnCompartir, btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_electronica);

        tvNumeroFactura = findViewById(R.id.tvNumeroFactura);
        tvFecha = findViewById(R.id.tvFecha);
        tvTipoCombustible = findViewById(R.id.tvTipoCombustible);
        tvLitros = findViewById(R.id.tvLitros);
        tvPrecioUnitario = findViewById(R.id.tvPrecioUnitario);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvIva = findViewById(R.id.tvIva);
        tvTotal = findViewById(R.id.tvTotal);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        // Recibir datos del Intent
        Intent intent = getIntent();
        String numeroFactura = intent.getStringExtra("numeroFactura");
        String fecha = intent.getStringExtra("fecha");
        String tipo = intent.getStringExtra("tipo");
        double litros = intent.getDoubleExtra("litros", 0);
        double precioUnitario = intent.getDoubleExtra("precioUnitario", 0);

        // Calcular valores
        double subtotal = litros * precioUnitario;
        double iva = subtotal * 0.19; // 19% de IVA
        double total = subtotal + iva;

        // Mostrar datos
        tvNumeroFactura.setText("Número de factura: " + numeroFactura);
        tvFecha.setText("Fecha: " + fecha);
        tvTipoCombustible.setText("Tipo de combustible: " + tipo);
        tvLitros.setText(String.format("Litros: %.2f", litros));
        tvPrecioUnitario.setText(String.format("Precio unitario: $%.2f", precioUnitario));
        tvSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));
        tvIva.setText(String.format("IVA (19%%): $%.2f", iva));
        tvTotal.setText(String.format("TOTAL: $%.2f", total));

        // Compartir factura como texto
        btnCompartir.setOnClickListener(v -> {
            String facturaTexto = generarTextoFactura(numeroFactura, fecha, tipo,
                    litros, precioUnitario, subtotal, iva, total);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, facturaTexto);
            startActivity(Intent.createChooser(shareIntent, "Compartir factura"));
        });

        // Finalizar y volver a la pantalla principal
        btnFinalizar.setOnClickListener(v -> {
            finish();
        });
    }

    private String generarTextoFactura(String numero, String fecha, String tipo,
                                       double litros, double precioUnitario,
                                       double subtotal, double iva, double total) {
        return "=== FACTURA ELECTRÓNICA ===\n" +
                "Número: " + numero + "\n" +
                "Fecha: " + fecha + "\n" +
                "Combustible: " + tipo + "\n" +
                "Litros: " + String.format("%.2f", litros) + "\n" +
                "Precio unitario: $" + String.format("%.2f", precioUnitario) + "\n" +
                "Subtotal: $" + String.format("%.2f", subtotal) + "\n" +
                "IVA (19%): $" + String.format("%.2f", iva) + "\n" +
                "TOTAL: $" + String.format("%.2f", total) + "\n" +
                "¡Gracias por su compra!";
    }
}