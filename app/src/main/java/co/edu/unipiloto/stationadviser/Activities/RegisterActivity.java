package co.edu.unipiloto.stationadviser.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextNombre, editTextUsuario, editTextEmail,
            editTextPassword, editTextConfirmPassword, editTextDireccion;

    private AutoCompleteTextView spinnerRole;
    private RadioGroup radioGroupGenero;
    private MaterialButton buttonRegister, buttonFecha;

    private DatabaseHelper dbHelper;
    private Calendar fechaNacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        editTextNombre          = findViewById(R.id.editTextNombre);
        editTextUsuario         = findViewById(R.id.editTextUsuario);
        editTextEmail           = findViewById(R.id.editTextEmail);
        editTextPassword        = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextDireccion       = findViewById(R.id.editTextDireccion);

        spinnerRole      = findViewById(R.id.spinnerRole);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        buttonRegister   = findViewById(R.id.buttonRegister);
        buttonFecha      = findViewById(R.id.buttonFecha);

        // Roles en el dropdown Material
        String[] roles = {"Cliente", "Empleado de estación", "Equipo técnico", "Entidad reguladora", "Distribuidor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, roles);
        spinnerRole.setAdapter(adapter);

        // Evita que el teclado abra al tocar el dropdown
        spinnerRole.setInputType(0);
        spinnerRole.setOnClickListener(v -> spinnerRole.showDropDown());

        buttonFecha.setOnClickListener(v -> mostrarDatePicker());
        buttonRegister.setOnClickListener(v -> registrarUsuario());
    }

    private void mostrarDatePicker() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dp = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    fechaNacimiento = Calendar.getInstance();
                    fechaNacimiento.set(year, month, day);
                    buttonFecha.setText(day + "/" + (month + 1) + "/" + year);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }

    private void registrarUsuario() {
        String nombre          = getText(editTextNombre);
        String usuario         = getText(editTextUsuario);
        String email           = getText(editTextEmail);
        String password        = getText(editTextPassword);
        String confirmPassword = getText(editTextConfirmPassword);
        String direccion       = getText(editTextDireccion);
        String role            = spinnerRole.getText().toString().trim();

        if (nombre.isEmpty() || usuario.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()
                || direccion.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fechaNacimiento == null || !esMayorDeEdad(fechaNacimiento)) {
            Toast.makeText(this, "Debe ser mayor de 18 años", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupGenero.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Seleccione un género", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radio = findViewById(selectedId);
        String genero = radio.getText().toString();

        boolean success = dbHelper.addUser(
                nombre, usuario, email, password,
                direccion, genero,
                fechaNacimiento.getTimeInMillis(),
                role
        );

        if (success) {
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error: el correo ya existe", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper para evitar NPE con TextInputEditText
    private String getText(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }

    private boolean esMayorDeEdad(Calendar fecha) {
        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - fecha.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < fecha.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return edad >= 18;
    }
}