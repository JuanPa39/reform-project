package co.edu.unipiloto.stationadviser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity"; // Para logs
    private DatabaseHelper dbHelper;
    private EditText editTextCorreo, editTextContrasena;
    private Button buttonLogin;
    private TextView textViewMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Iniciando LoginActivity");

        // Inicializar base de datos
        dbHelper = new DatabaseHelper(this);
        verificarUsuariosEnBD();
        // Conectar con los elementos del layout
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewMensaje = findViewById(R.id.textViewMensaje);

        // Verificar que los elementos no sean null
        if (editTextCorreo == null) Log.e(TAG, "editTextCorreo es NULL");
        if (editTextContrasena == null) Log.e(TAG, "editTextContrasena es NULL");
        if (buttonLogin == null) Log.e(TAG, "buttonLogin es NULL");

        // Configurar botón de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botón login clickeado");
                iniciarSesion();
            }
        });
    }
    // Agrega este método después de onCreate
    private void verificarUsuariosEnBD() {
        List<Usuario> usuarios = dbHelper.obtenerTodosLosUsuarios();
        Log.d(TAG, "=== USUARIOS EN BD ===");
        for (Usuario u : usuarios) {
            Log.d(TAG, "ID: " + u.getId() +
                    ", Correo: " + u.getCorreo() +
                    ", Contraseña: " + u.getContrasena() +
                    ", Rol: " + u.getRol());
        }
        Log.d(TAG, "========================");
    }
    private void iniciarSesion() {
        Log.d(TAG, "iniciarSesion() llamado");

        String correo = editTextCorreo.getText().toString().trim();
        String contrasena = editTextContrasena.getText().toString().trim();

        Log.d(TAG, "Correo ingresado: '" + correo + "'");
        Log.d(TAG, "Contraseña ingresada: '" + contrasena + "'");

        // Validar campos vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            textViewMensaje.setText("Por favor ingrese correo y contraseña");
            Log.d(TAG, "Campos vacíos");
            return;
        }

        // Validar en base de datos
        Log.d(TAG, "Buscando usuario en BD...");
        Usuario usuario = dbHelper.validarUsuario(correo, contrasena);

        if (usuario != null) {
            // Login exitoso
            Log.d(TAG, "Login EXITOSO para: " + usuario.getCorreo() + " - Rol: " + usuario.getRol());

            textViewMensaje.setText("");
            Toast.makeText(this, "Bienvenido " + usuario.getRol(), Toast.LENGTH_SHORT).show();

            // Ir a RoleBaseActivity
            Intent intent = new Intent(LoginActivity.this, RoleBaseActivity.class);
            intent.putExtra("email", usuario.getCorreo());   // Cambia "correo" por "email"
            intent.putExtra("role", usuario.getRol());       // Cambia "rol" por "role"

            Log.d(TAG, "Iniciando MainActivity...");
            startActivity(intent);
            finish();
        } else {
            // Login fallido
            Log.d(TAG, "Login FALLIDO - Usuario no encontrado");
            textViewMensaje.setText("Correo o contraseña incorrectos");
        }
    }
}