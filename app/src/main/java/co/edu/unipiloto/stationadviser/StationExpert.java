package co.edu.unipiloto.stationadviser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class StationExpert extends Activity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_RESPONSE = "response";
    public static final String EXTRA_HISTORY = "history";
    private static ArrayList<String> historialRespuestas = new ArrayList<>();
    private TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_expert);

        historyTextView = findViewById(R.id.history_empleado);

        Intent intent = getIntent();
        String messageText = intent.getStringExtra(EXTRA_MESSAGE);

        // Recuperar historial si existe
        if (intent.hasExtra(EXTRA_HISTORY)) {
            historialRespuestas = (ArrayList<String>) intent.getSerializableExtra(EXTRA_HISTORY);
        }

        // Agregar el mensaje del cliente al historial
        if (messageText != null && !messageText.isEmpty()) {
            historialRespuestas.add("Recibido: " + messageText);
        }
        actualizarHistorial();
    }

    public void onRespondMessage(View view) {
        EditText responseView = findViewById(R.id.response_message);
        String responseText = "Estación: " + responseView.getText().toString();

        // Agregar respuesta al historial
        historialRespuestas.add(responseText);

        // Volver a MainActivity con la respuesta y el historial
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, responseText);
        intent.putExtra(EXTRA_HISTORY, historialRespuestas);
        startActivity(intent);
    }

    private void actualizarHistorial() {
        StringBuilder builder = new StringBuilder();
        for (String mensaje : historialRespuestas) {
            builder.append(mensaje).append("\n\n");
        }
        historyTextView.setText(builder.toString());
    }
}