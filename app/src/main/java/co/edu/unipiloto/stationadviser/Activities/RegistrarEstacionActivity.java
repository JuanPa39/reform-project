package co.edu.unipiloto.stationadviser.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import co.edu.unipiloto.stationadviser.BD.DatabaseHelper;
import co.edu.unipiloto.stationadviser.R;

public class RegistrarEstacionActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private DatabaseHelper dbHelper;
    private EditText editTextNombre, editTextNit, editTextUbicacion, editTextLatitud, editTextLongitud;
    private Button buttonObtenerUbicacion, buttonRegistrar;
    private TextView textViewMensaje;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_estacion);

        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextNit = findViewById(R.id.editTextNit);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);
        editTextLatitud = findViewById(R.id.editTextLatitud);
        editTextLongitud = findViewById(R.id.editTextLongitud);
        buttonObtenerUbicacion = findViewById(R.id.buttonObtenerUbicacion);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        textViewMensaje = findViewById(R.id.textViewMensaje);

        buttonObtenerUbicacion.setOnClickListener(v -> obtenerUbicacionActual());

        buttonRegistrar.setOnClickListener(v -> registrarEstacion());
    }

    private void obtenerUbicacionActual() {
        // Verificar permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Obtener última ubicación conocida
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitud = location.getLatitude();
                            double longitud = location.getLongitude();
                            editTextLatitud.setText(String.valueOf(latitud));
                            editTextLongitud.setText(String.valueOf(longitud));
                            Toast.makeText(RegistrarEstacionActivity.this,
                                    "Ubicación obtenida: " + latitud + ", " + longitud,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistrarEstacionActivity.this,
                                    "No se pudo obtener la ubicación. Activa el GPS.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registrarEstacion() {
        String nombre = editTextNombre.getText().toString().trim();
        String nit = editTextNit.getText().toString().trim();
        String ubicacion = editTextUbicacion.getText().toString().trim();
        String latitud = editTextLatitud.getText().toString().trim();
        String longitud = editTextLongitud.getText().toString().trim();

        if (nombre.isEmpty() || nit.isEmpty() || ubicacion.isEmpty()) {
            textViewMensaje.setText("Nombre, NIT y ubicación son obligatorios");
            return;
        }

        if (latitud.isEmpty() || longitud.isEmpty()) {
            textViewMensaje.setText("Debe obtener la ubicación GPS");
            return;
        }

        boolean exito = dbHelper.addEstacion(nombre, nit, ubicacion, latitud, longitud);

        if (exito) {
            Toast.makeText(this, "Estación registrada con éxito", Toast.LENGTH_SHORT).show();
            editTextNombre.setText("");
            editTextNit.setText("");
            editTextUbicacion.setText("");
            editTextLatitud.setText("");
            editTextLongitud.setText("");
            textViewMensaje.setText("");
        } else {
            textViewMensaje.setText("Error: El NIT ya existe o hubo un problema");
        }
    }
}