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

        // Obtener datos del intent
        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("email");
            userRole = intent.getStringExtra("role");
        }

        Log.d(TAG, "Email recibido: " + userEmail);
        Log.d(TAG, "Rol recibido: " + userRole);

        // Vincular vistas
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewEmail = findViewById(R.id.textViewEmail);

        // Mostrar correo
        if (userEmail != null) {
            textViewEmail.setText("Correo: " + userEmail);
        } else {
            textViewEmail.setText("Correo: No disponible");
        }

        // Si el rol es nulo, asignar uno por defecto
        if (userRole == null) {
            userRole = "Cliente";
            Log.e(TAG, "Rol era nulo, asignado valor por defecto: Cliente");
        }

        // Configurar botones según el rol (esto ya asigna los listeners específicos)
        configurarBotonesPorRol();

        // ÚNICO LISTENER FUERA DEL SWITCH: botón de cerrar sesión
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

        if (userRole != null) {
            switch (userRole) {
                case "Cliente":
                    button1.setVisibility(View.VISIBLE);
                    button1.setText("Consultar precio combustible");
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Función de cliente (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case "Empleado de estación":
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button1.setText("Registrar estación");
                    button2.setText("Atender cliente");
                    button3.setText("Ver estaciones");

                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RoleBaseActivity.this, RegistrarEstacionActivity.class);
                            startActivity(intent);
                        }
                    });

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Función Atender cliente (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });

                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RoleBaseActivity.this, ListaEstacionesActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;

                case "Equipo técnico":
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button1.setText("Diagnosticar surtidor");
                    button2.setText("Reparar equipo");
                    button3.setText("Mantenimiento preventivo");

                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Diagnóstico (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Reparación (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Mantenimiento (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
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

                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Normativas (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Inspección (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Multas (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RoleBaseActivity.this, "Reportes (próximamente)", Toast.LENGTH_SHORT).show();
                        }
                    });
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