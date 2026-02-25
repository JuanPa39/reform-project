package co.edu.unipiloto.stationadviser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_HISTORY = "history";
    private static ArrayList<String> historialMensajes = new ArrayList<>();
    private TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        historyTextView = findViewById(R.id.history_cliente);

        // Si venimos de StationExpert, recibimos el historial actualizado
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_HISTORY)) {
            historialMensajes = (ArrayList<String>) intent.getSerializableExtra(EXTRA_HISTORY);
        }
        actualizarHistorial();
    }

    public void onSendMessage(View view) {
        EditText messageView = findViewById(R.id.message);
        String messageText = "Cliente: " + messageView.getText().toString();

        // Agregar al historial
        historialMensajes.add(messageText);

        // Iniciar StationExpert y pasarle el mensaje y el historial
        Intent intent = new Intent(this, StationExpert.class);
        intent.putExtra(EXTRA_MESSAGE, messageText);
        intent.putExtra(EXTRA_HISTORY, historialMensajes);
        startActivity(intent);
    }

    private void actualizarHistorial() {
        StringBuilder builder = new StringBuilder();
        for (String mensaje : historialMensajes) {
            builder.append(mensaje).append("\n\n");
        }
        historyTextView.setText(builder.toString());
    }
}