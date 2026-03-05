package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editCorreo, editNuevaContrasena;
    private Button buttonCambiar;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editCorreo = findViewById(R.id.editCorreoReset);
        editNuevaContrasena = findViewById(R.id.editNuevaContrasena);
        buttonCambiar = findViewById(R.id.buttonCambiarContrasena);

        dbHelper = new DatabaseHelper(this);

        buttonCambiar.setOnClickListener(v -> cambiarContrasena());
    }

    private void cambiarContrasena() {

        String correo = editCorreo.getText().toString().trim();
        String nuevaPass = editNuevaContrasena.getText().toString().trim();

        if (correo.isEmpty() || nuevaPass.isEmpty()) {

            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean actualizado = dbHelper.actualizarContrasena(correo, nuevaPass);

        if (actualizado) {

            Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show();
            finish();

        } else {

            Toast.makeText(this, "El correo no existe", Toast.LENGTH_LONG).show();
        }
    }
}