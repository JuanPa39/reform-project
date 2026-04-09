package co.edu.unipiloto.stationadviser.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.Model.Usuario;
import co.edu.unipiloto.stationadviser.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private DatabaseHelper dbHelper;
    private TextInputEditText editTextCorreo, editTextContrasena;
    private Button buttonLogin;
    private TextView textViewMensaje, textRegistro, textRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        editTextCorreo    = findViewById(R.id.editTextCorreo);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        buttonLogin       = findViewById(R.id.buttonLogin);
        textViewMensaje   = findViewById(R.id.textViewMensaje);
        textRegistro      = findViewById(R.id.textRegistro);
        textRecuperar     = findViewById(R.id.textRecuperar);

        verificarUsuariosEnBD();

        buttonLogin.setOnClickListener(v -> iniciarSesion());

        textRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        textRecuperar.setOnClickListener(v ->
                startActivity(new Intent(this, ResetPasswordActivity.class)));
    }

    private void verificarUsuariosEnBD() {
        List<Usuario> usuarios = dbHelper.obtenerTodosLosUsuarios();
        for (Usuario u : usuarios) {
            Log.d(TAG, "Correo: " + u.getCorreo() + " Rol: " + u.getRol());
        }
    }

    private void iniciarSesion() {
        String correo    = editTextCorreo.getText() != null
                ? editTextCorreo.getText().toString().trim() : "";
        String contrasena = editTextContrasena.getText() != null
                ? editTextContrasena.getText().toString().trim() : "";

        if (correo.isEmpty() || contrasena.isEmpty()) {
            textViewMensaje.setText("Ingrese correo y contraseña");
            return;
        }

        Usuario usuario = dbHelper.obtenerUsuarioPorCorreo(correo);

        if (usuario == null) {
            textViewMensaje.setText("El correo no está registrado");
            return;
        }

        if (!usuario.getContrasena().equals(contrasena)) {
            textViewMensaje.setText("La contraseña es incorrecta");
            return;
        }

        Toast.makeText(this, "Bienvenido " + usuario.getRol(), Toast.LENGTH_SHORT).show();

        int estacionId = dbHelper.obtenerEstacionIdPorCorreo(usuario.getCorreo());

        Intent intent = new Intent(this, RoleBaseActivity.class);
        intent.putExtra("email", usuario.getCorreo());
        intent.putExtra("role", usuario.getRol());
        intent.putExtra("estacionId", estacionId);
        startActivity(intent);
        finish();
    }
}