package co.edu.unipiloto.stationadviser.Activities;

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

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.Model.Usuario;
import co.edu.unipiloto.stationadviser.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity"; // Para logs
    private DatabaseHelper dbHelper;
    private EditText editTextCorreo, editTextContrasena;
    private Button buttonLogin;
    private TextView textViewMensaje;
    private TextView textRegistro;

    private TextView textRecuperar;

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
        textRegistro = findViewById(R.id.textRegistro);

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

        //Configurar boton de registro
        textRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Ir a pantalla de registro");

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        //Configurar boton recuperar contraseña
        textRecuperar = findViewById(R.id.textRecuperar);

        textRecuperar.setOnClickListener(v -> {

            Toast.makeText(this, "Función de recuperación próximamente", Toast.LENGTH_SHORT).show();

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

        String correo = editTextCorreo.getText().toString().trim();
        String contrasena = editTextContrasena.getText().toString().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            textViewMensaje.setText("Ingrese correo y contraseña");
            return;
        }

        // Buscar usuario solo por correo
        Usuario usuario = dbHelper.obtenerUsuarioPorCorreo(correo);

        if (usuario == null) {

            textViewMensaje.setText("El correo no está registrado");

        } else {

            if (!usuario.getContrasena().equals(contrasena)) {

                textViewMensaje.setText("La contraseña es incorrecta");

            } else {

                // LOGIN CORRECTO
                Toast.makeText(this, "Bienvenido " + usuario.getRol(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, RoleBaseActivity.class);
                intent.putExtra("email", usuario.getCorreo());
                intent.putExtra("role", usuario.getRol());

                startActivity(intent);
                finish();
            }
        }
    }

}