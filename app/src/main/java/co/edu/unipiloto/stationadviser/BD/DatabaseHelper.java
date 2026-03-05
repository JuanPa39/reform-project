package co.edu.unipiloto.stationadviser.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import co.edu.unipiloto.stationadviser.Model.Estacion;
import co.edu.unipiloto.stationadviser.Model.Usuario;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserManager.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CORREO = "correo";
    private static final String COLUMN_CONTRASENA = "contrasena";
    private static final String COLUMN_ROL = "rol";
    private static final String TABLE_ESTACIONES = "estaciones";
    private static final String COLUMN_ID_EST = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_NIT = "nit";
    private static final String COLUMN_UBICACION = "ubicacion";

    private static final String TABLE_PRECIOS = "precios";

    private static final String COLUMN_ID_PRECIO = "id";
    private static final String COLUMN_TIPO_COMBUSTIBLE = "tipo_combustible";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_ESTACION_ID = "estacion_id";
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CORREO + " TEXT UNIQUE,"
            + COLUMN_CONTRASENA + " TEXT,"
            + COLUMN_ROL + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREAR_TABLA_ESTACIONES);
        db.execSQL(CREAR_TABLA_PRECIOS);

        insertarUsuariosDePrueba(db);
        insertarEstacionesIniciales(db);
        insertarPreciosIniciales(db);
    }

    private void insertarUsuariosDePrueba(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Usuario 1: Cliente
        values.put(COLUMN_CORREO, "cliente@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_ROL, "Cliente");
        db.insert(TABLE_USERS, null, values);

        // Usuario 2: Empleado
        values.clear();
        values.put(COLUMN_CORREO, "empleado@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_ROL, "Empleado de estación");
        db.insert(TABLE_USERS, null, values);

        // Usuario 3: Técnico
        values.clear();
        values.put(COLUMN_CORREO, "tecnico@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_ROL, "Equipo técnico");
        db.insert(TABLE_USERS, null, values);

        // Usuario 4: Regulador
        values.clear();
        values.put(COLUMN_CORREO, "regulador@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_ROL, "Entidad reguladora");
        db.insert(TABLE_USERS, null, values);
    }

    private void insertarEstacionesIniciales(SQLiteDatabase db){

        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE, "Terpel Norte");
        values.put(COLUMN_NIT, "900123");
        values.put(COLUMN_UBICACION, "Bogotá");
        db.insert(TABLE_ESTACIONES, null, values);

        values.clear();
        values.put(COLUMN_NOMBRE, "Primax Centro");
        values.put(COLUMN_NIT, "900456");
        values.put(COLUMN_UBICACION, "Medellín");
        db.insert(TABLE_ESTACIONES, null, values);

        values.clear();
        values.put(COLUMN_NOMBRE, "Texaco Sur");
        values.put(COLUMN_NIT, "900789");
        values.put(COLUMN_UBICACION, "Cali");
        db.insert(TABLE_ESTACIONES, null, values);
    }

    private void insertarPreciosIniciales(SQLiteDatabase db){

        ContentValues values = new ContentValues();

        // Estación 1
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_PRECIO, 12000);
        values.put(COLUMN_ESTACION_ID, 1);
        db.insert(TABLE_PRECIOS, null, values);

        values.clear();
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Extra");
        values.put(COLUMN_PRECIO, 14000);
        values.put(COLUMN_ESTACION_ID, 1);
        db.insert(TABLE_PRECIOS, null, values);

        // Estación 2
        values.clear();
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_PRECIO, 11800);
        values.put(COLUMN_ESTACION_ID, 2);
        db.insert(TABLE_PRECIOS, null, values);

        values.clear();
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_PRECIO, 11000);
        values.put(COLUMN_ESTACION_ID, 2);
        db.insert(TABLE_PRECIOS, null, values);

        // Estación 3
        values.clear();
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_PRECIO, 11950);
        values.put(COLUMN_ESTACION_ID, 3);
        db.insert(TABLE_PRECIOS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRECIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    public Usuario validarUsuario(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_CORREO + " = ? AND " + COLUMN_CONTRASENA + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{correo, contrasena});

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String correoDb = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO));
            String contrasenaDb = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA));
            String rolDb = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROL));

            usuario = new Usuario(id, correoDb, contrasenaDb, rolDb);
        }
        cursor.close();
        db.close();

        return usuario;
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_CORREO + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{correo});

        Usuario usuario = null;

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String correoDb = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO));
            String contrasenaDb = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA));
            String rolDb = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROL));

            usuario = new Usuario(id, correoDb, contrasenaDb, rolDb);
        }

        cursor.close();
        db.close();

        return usuario;
    }
    // Método para agregar estación
    public boolean addEstacion(String nombre, String nit, String ubicacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_NIT, nit);
        values.put(COLUMN_UBICACION, ubicacion);

        long resultado = db.insert(TABLE_ESTACIONES, null, values);
        db.close();
        return resultado != -1; // false si el NIT ya existe (por la restricción UNIQUE)
    }
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO));
                String contrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA));
                String rol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROL));

                Usuario usuario = new Usuario(id, correo, contrasena, rol);
                listaUsuarios.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return listaUsuarios;
    }
    public Estacion getEstacionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Estacion estacion = null;

        String query = "SELECT * FROM " + TABLE_ESTACIONES + " WHERE " + COLUMN_ID_EST + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE));
            String nit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIT));
            String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICACION));
            estacion = new Estacion(id, nombre, nit, ubicacion);
        }
        cursor.close();
        db.close();
        return estacion;
    }
    public boolean updateEstacion(int id, String nombre, String nit, String ubicacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_NIT, nit);
        values.put(COLUMN_UBICACION, ubicacion);

        int filasAfectadas = db.update(TABLE_ESTACIONES, values, COLUMN_ID_EST + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }
    public List<Estacion> obtenerTodasLasEstaciones() {
        List<Estacion> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ESTACIONES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_EST));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE));
                String nit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIT));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICACION));
                lista.add(new Estacion(id, nombre, nit, ubicacion));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }


    public boolean addUser(String correo, String contrasena, String rol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CORREO, correo);
        values.put(COLUMN_CONTRASENA, contrasena);
        values.put(COLUMN_ROL, rol);

        long resultado = db.insert(TABLE_USERS, null, values);
        db.close();

        return resultado != -1;
    }

    public boolean actualizarContrasena(String correo, String nuevaContrasena) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTRASENA, nuevaContrasena);

        int filas = db.update(
                TABLE_USERS,
                values,
                COLUMN_CORREO + " = ?",
                new String[]{correo}
        );

        db.close();

        return filas > 0;
    }

    public boolean registrarPrecio(String tipo, double precio, int idEstacion) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_PRECIOS +
                        " WHERE " + COLUMN_TIPO_COMBUSTIBLE + " = ? AND " + COLUMN_ESTACION_ID + " = ?",
                new String[]{tipo, String.valueOf(idEstacion)}
        );

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIPO_COMBUSTIBLE, tipo);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_ESTACION_ID, idEstacion);

        long resultado;

        if (cursor.moveToFirst()) {

            // UPDATE
            resultado = db.update(
                    TABLE_PRECIOS,
                    values,
                    COLUMN_TIPO_COMBUSTIBLE + " = ? AND " + COLUMN_ESTACION_ID + " = ?",
                    new String[]{tipo, String.valueOf(idEstacion)}
            );

        } else {

            // INSERT
            resultado = db.insert(TABLE_PRECIOS, null, values);
        }

        cursor.close();
        db.close();

        return resultado != -1;
    }

    public List<String> obtenerPreciosCombustible() {

        List<String> lista = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e.nombre, p.tipo_combustible, p.precio " +
                "FROM " + TABLE_PRECIOS + " p " +
                "JOIN " + TABLE_ESTACIONES + " e " +
                "ON p.estacion_id = e.id";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                String estacion = cursor.getString(0);
                String combustible = cursor.getString(1);
                double precio = cursor.getDouble(2);

                lista.add(estacion + " - " + combustible + " - $" + precio);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lista;
    }

    private static final String CREAR_TABLA_ESTACIONES = "CREATE TABLE " + TABLE_ESTACIONES + "("
            + COLUMN_ID_EST + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOMBRE + " TEXT,"
            + COLUMN_NIT + " TEXT UNIQUE,"
            + COLUMN_UBICACION + " TEXT" + ")";

    private static final String CREAR_TABLA_PRECIOS = "CREATE TABLE " + TABLE_PRECIOS + "("
            + COLUMN_ID_PRECIO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TIPO_COMBUSTIBLE + " TEXT,"
            + COLUMN_PRECIO + " REAL,"
            + COLUMN_ESTACION_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_ESTACION_ID + ") REFERENCES " + TABLE_ESTACIONES + "(" + COLUMN_ID_EST + ")"
            + ")";
}

