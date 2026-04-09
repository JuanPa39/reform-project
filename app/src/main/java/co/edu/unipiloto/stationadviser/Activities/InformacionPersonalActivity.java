package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.Model.Usuario;
import co.edu.unipiloto.stationadviser.R;

public class InformacionPersonalActivity extends AppCompatActivity {

    private EditText editNombre, editUsername, editDireccion, editGenero;
    private TextView tvCorreo, tvRol, tvFechaNac;
    private Button btnGuardar;
    private DatabaseHelper db;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);

        db = new DatabaseHelper(this);

        editNombre   = findViewById(R.id.editNombre);
        editUsername = findViewById(R.id.editUsername);
        editDireccion = findViewById(R.id.editDireccion);
        editGenero   = findViewById(R.id.editGenero);
        tvCorreo     = findViewById(R.id.tvCorreo);
        tvRol        = findViewById(R.id.tvRol);
        tvFechaNac   = findViewById(R.id.tvFechaNac);
        btnGuardar   = findViewById(R.id.btnGuardarInfo);

        String email = getIntent().getStringExtra("email");
        usuario = db.obtenerUsuarioPorCorreo(email);

        if (usuario != null) {
            editNombre.setText(usuario.getNombre());
            editUsername.setText(usuario.getUsername());
            editDireccion.setText(usuario.getDireccion());
            editGenero.setText(usuario.getGenero());
            tvCorreo.setText("Correo: " + usuario.getCorreo());
            tvRol.setText("Rol: " + usuario.getRol());

            String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(usuario.getFechaNacimiento()));
            tvFechaNac.setText("Fecha de nacimiento: " + fecha);
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre    = editNombre.getText().toString().trim();
            String username  = editUsername.getText().toString().trim();
            String direccion = editDireccion.getText().toString().trim();
            String genero    = editGenero.getText().toString().trim();

            if (nombre.isEmpty() || username.isEmpty() || direccion.isEmpty() || genero.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.actualizarInfoPersonal(
                    usuario.getId(), nombre, username, direccion, genero
            );

            if (ok) {
                Toast.makeText(this, "Información actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}