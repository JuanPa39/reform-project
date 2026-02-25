package co.edu.unipiloto.stationadviser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RoleBaseActivity extends AppCompatActivity {

    private static final String TAG = "RoleBaseActivity";
    private Button button1, button2, button3, button4, buttonLogout;
    private TextView textViewEmail;
    private String userEmail;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_base);

        // Obtener datos del intent con manejo de nulos
        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("email");
            userRole = intent.getStringExtra("role");
        }

        // Depuración: mostrar en logs qué recibimos
        Log.d(TAG, "Email recibido: " + userEmail);
        Log.d(TAG, "Rol recibido: " + userRole);

        // Vincular vistas
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewEmail = findViewById(R.id.textViewEmail);

        // Mostrar correo (con valor por defecto si es nulo)
        if (userEmail != null) {
            textViewEmail.setText("Correo: " + userEmail);
        } else {
            textViewEmail.setText("Correo: No disponible");
        }

        // Si el rol es nulo, asignar uno por defecto para evitar crash
        if (userRole == null) {
            userRole = "Cliente"; // Valor por defecto
            Log.e(TAG, "Rol era nulo, asignado valor por defecto: Cliente");
        }

        // Configurar botones según rol
        configurarBotonesPorRol();

        // Asignar acciones a los botones
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RoleBaseActivity.this, "Rol: " + userRole + " - Botón 1", Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RoleBaseActivity.this, "Rol: " + userRole + " - Botón 2", Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RoleBaseActivity.this, "Rol: " + userRole + " - Botón 3", Toast.LENGTH_SHORT).show();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RoleBaseActivity.this, "Rol: " + userRole + " - Botón 4", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón de cerrar sesión
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleBaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void configurarBotonesPorRol() {
        // Primero ocultar todos
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);

        Log.d(TAG, "Configurando botones para rol: " + userRole);

        // Según el rol, mostrar ciertos botones
        if (userRole != null) {
            switch (userRole) {
                case "Cliente":
                    button1.setVisibility(View.VISIBLE);
                    button1.setText("Consultar precio combustible");
                    break;
                case "Empleado de estación":
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button1.setText("Ver inventario");
                    button2.setText("Atender cliente");
                    break;
                case "Equipo técnico":
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button1.setText("Diagnosticar surtidor");
                    button2.setText("Reparar equipo");
                    button3.setText("Mantenimiento preventivo");
                    break;
                case "Entidad reguladora":
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button4.setVisibility(View.VISIBLE);
                    button1.setText("Ver normativas");
                    button2.setText("Programar inspección");
                    button3.setText("Registrar multa");
                    button4.setText("Generar reporte");
                    break;
                default:
                    Log.e(TAG, "Rol desconocido: " + userRole);
                    button1.setVisibility(View.VISIBLE);
                    button1.setText("Rol no reconocido");
                    break;
            }
        }
    }
}