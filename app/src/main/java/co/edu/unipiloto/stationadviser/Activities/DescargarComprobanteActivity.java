package co.edu.unipiloto.stationadviser.Activities;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class DescargarComprobanteActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listViewComprobantes;
    List<String[]> ventas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descargar_comprobante);

        db = new DatabaseHelper(this);
        listViewComprobantes = findViewById(R.id.listViewComprobantes);

        ventas = db.obtenerVentasConFactura();

        if (ventas.isEmpty()) {
            Toast.makeText(this, "No hay comprobantes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar en lista: "INV-xxx | Corriente | 2025-03-25"
        String[] items = new String[ventas.size()];
        for (int i = 0; i < ventas.size(); i++) {
            String[] v = ventas.get(i);
            items[i] = v[0] + " | " + v[1] + " | " + v[4];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, items
        );
        listViewComprobantes.setAdapter(adapter);

        // Al tocar una venta, descargar su PDF
        listViewComprobantes.setOnItemClickListener((parent, view, position, id) -> {
            String[] venta = ventas.get(position);
            String numeroFactura = venta[0];
            String tipo          = venta[1];
            double litros        = Double.parseDouble(venta[2]);
            double precio        = Double.parseDouble(venta[3]);
            String fecha         = venta[4];
            String estacion      = venta[5];

            descargarPdf(numeroFactura, fecha, estacion, tipo, litros, precio);
        });
    }

    private void descargarPdf(String numeroFactura, String fecha, String estacion,
                              String tipo, double litros, double precio) {
        try {
            File pdfCache = PdfGenerator.generarFacturaPdf(
                    this, numeroFactura, fecha, estacion, tipo, litros, precio
            );

            String nombreArchivo = "Comprobante_" + numeroFactura + ".pdf";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, nombreArchivo);
                values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
                );

                if (uri != null) {
                    try (OutputStream os = getContentResolver().openOutputStream(uri);
                         FileInputStream fis = new FileInputStream(pdfCache)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = fis.read(buffer)) != -1) os.write(buffer, 0, len);
                    }
                    Toast.makeText(this, "✅ Guardado: " + nombreArchivo, Toast.LENGTH_LONG).show();
                }
            } else {
                File destino = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        nombreArchivo
                );
                try (FileInputStream fis = new FileInputStream(pdfCache);
                     OutputStream os = new FileOutputStream(destino)) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = fis.read(buffer)) != -1) os.write(buffer, 0, len);
                }
                Toast.makeText(this, "✅ Guardado: " + nombreArchivo, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}