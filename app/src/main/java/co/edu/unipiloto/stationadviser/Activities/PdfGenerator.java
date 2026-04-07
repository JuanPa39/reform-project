package co.edu.unipiloto.stationadviser.Activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static File generarFacturaPdf(Context context, String numeroFactura,
                                         String fecha, String tipo,
                                         double litros, double precioUnitario) throws IOException {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        int margin = 50;
        int y = 80;

        // — Título
        Paint paintTitle = new Paint();
        paintTitle.setColor(Color.parseColor("#1565C0"));
        paintTitle.setTextSize(24f);
        paintTitle.setFakeBoldText(true);
        canvas.drawText("FACTURA ELECTRÓNICA", margin, y, paintTitle);
        y += 10;

        // — Línea divisoria
        Paint paintLine = new Paint();
        paintLine.setColor(Color.parseColor("#1565C0"));
        paintLine.setStrokeWidth(2f);
        canvas.drawLine(margin, y, 595 - margin, y, paintLine);
        y += 30;

        // — Datos de la factura
        Paint paintLabel = new Paint();
        paintLabel.setColor(Color.DKGRAY);
        paintLabel.setTextSize(11f);
        paintLabel.setFakeBoldText(true);

        Paint paintValue = new Paint();
        paintValue.setColor(Color.BLACK);
        paintValue.setTextSize(11f);

        canvas.drawText("N° Factura:", margin, y, paintLabel);
        canvas.drawText(numeroFactura, 200, y, paintValue);
        y += 25;

        canvas.drawText("Fecha:", margin, y, paintLabel);
        canvas.drawText(fecha, 200, y, paintValue);
        y += 40;

        // — Línea separadora sección
        Paint paintGray = new Paint();
        paintGray.setColor(Color.LTGRAY);
        paintGray.setStrokeWidth(1f);
        canvas.drawLine(margin, y, 595 - margin, y, paintGray);
        y += 25;

        // — Detalle de la venta
        Paint paintSection = new Paint();
        paintSection.setColor(Color.parseColor("#1565C0"));
        paintSection.setTextSize(13f);
        paintSection.setFakeBoldText(true);
        canvas.drawText("DETALLE DE VENTA", margin, y, paintSection);
        y += 30;

        canvas.drawText("Tipo de combustible:", margin, y, paintLabel);
        canvas.drawText(tipo, 250, y, paintValue);
        y += 25;

        canvas.drawText("Litros:", margin, y, paintLabel);
        canvas.drawText(String.format("%.2f L", litros), 250, y, paintValue);
        y += 25;

        canvas.drawText("Precio por litro:", margin, y, paintLabel);
        canvas.drawText(String.format("$ %.2f", precioUnitario), 250, y, paintValue);
        y += 40;

        // — Línea total
        canvas.drawLine(margin, y, 595 - margin, y, paintGray);
        y += 25;

        double total = litros * precioUnitario;
        Paint paintTotal = new Paint();
        paintTotal.setColor(Color.parseColor("#1565C0"));
        paintTotal.setTextSize(16f);
        paintTotal.setFakeBoldText(true);
        canvas.drawText("TOTAL:", margin, y, paintTotal);
        canvas.drawText(String.format("$ %.2f", total), 250, y, paintTotal);
        y += 50;

        // — Pie de página
        Paint paintFooter = new Paint();
        paintFooter.setColor(Color.GRAY);
        paintFooter.setTextSize(9f);
        canvas.drawText("Generado por StationAdviser", margin, 800, paintFooter);

        document.finishPage(page);

        // — Guardar en cache
        File dir = new File(context.getCacheDir(), "pdfs");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "factura_" + System.currentTimeMillis() + ".pdf");

        FileOutputStream fos = new FileOutputStream(file);
        document.writeTo(fos);
        document.close();
        fos.close();

        return file;
    }
}