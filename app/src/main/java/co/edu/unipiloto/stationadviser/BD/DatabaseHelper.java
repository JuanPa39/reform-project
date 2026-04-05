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
    private static final int DATABASE_VERSION = 8;

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
    private static final String TABLE_INVENTARIO = "inventario";
    private static final String TABLE_VENTAS = "ventas";

    private static final String COLUMN_TIPO = "tipo";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String COLUMN_LITROS = "litros";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_NOMBRE_USUARIO = "nombre";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_DIRECCION = "direccion";
    private static final String COLUMN_GENERO = "genero";
    private static final String COLUMN_FECHA_NAC = "fecha_nacimiento";
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOMBRE_USUARIO + " TEXT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_CORREO + " TEXT UNIQUE,"
            + COLUMN_CONTRASENA + " TEXT,"
            + COLUMN_DIRECCION + " TEXT,"
            + COLUMN_GENERO + " TEXT,"
            + COLUMN_FECHA_NAC + " INTEGER,"
            + COLUMN_ROL + " TEXT" + ")";

    private static final String TABLE_NORMATIVAS = "normativas";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_FECHA_VIGENCIA = "fecha_vigencia";

    private static final String CREAR_TABLA_NORMATIVAS =
            "CREATE TABLE " + TABLE_NORMATIVAS + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITULO + " TEXT,"
                    + COLUMN_DESCRIPCION + " TEXT,"
                    + COLUMN_FECHA_VIGENCIA + " TEXT"
                    + ")";

    private static final String TABLE_NOTIFICACIONES = "notificaciones";
    private static final String COLUMN_ESTACION_NOMBRE = "estacion_nombre";
    private static final String COLUMN_INCONSISTENCIA = "inconsistencia";
    private static final String COLUMN_ESTADO = "estado"; // "Pendiente" o "Enviada"

    private static final String CREAR_TABLA_NOTIFICACIONES =
            "CREATE TABLE " + TABLE_NOTIFICACIONES + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ESTACION_NOMBRE + " TEXT,"
                    + COLUMN_INCONSISTENCIA + " TEXT,"
                    + COLUMN_ESTADO + " TEXT,"
                    + COLUMN_FECHA + " TEXT"
                    + ")";
    private static final String TABLE_REGLAS_SUBSIDIO = "reglas_subsidio";
    private static final String COLUMN_TIPO_VEHICULO = "tipo_vehiculo";
    private static final String COLUMN_LITROS_MAX = "litros_max";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String CREAR_TABLA_REGLAS_SUBSIDIO =
            "CREATE TABLE " + TABLE_REGLAS_SUBSIDIO + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TIPO_VEHICULO + " TEXT,"
                    + COLUMN_TIPO_COMBUSTIBLE + " TEXT,"
                    + COLUMN_LITROS_MAX + " REAL"
                    + ")";
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREAR_TABLA_ESTACIONES);
        db.execSQL(CREAR_TABLA_PRECIOS);
        db.execSQL(CREAR_TABLA_INVENTARIO);
        db.execSQL(CREAR_TABLA_VENTAS);
        db.execSQL(CREAR_TABLA_NORMATIVAS);
        db.execSQL(CREAR_TABLA_NOTIFICACIONES);
        db.execSQL(CREAR_TABLA_REGLAS_SUBSIDIO);

        insertarUsuariosDePrueba(db);
        insertarEstacionesIniciales(db);
        insertarPreciosIniciales(db);
        insertarNormativasIniciales(db);
        insertarVentasIniciales(db);
        insertarReglasSubsidioIniciales(db);
    }
    private void insertarReglasSubsidioIniciales(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // ==================== GASOLINA CORRIENTE ====================
        // Servicio Público (Taxis, Buses) - hasta 50 litros
        values.put(COLUMN_TIPO_VEHICULO, "Taxi");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 50.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Servicio Público (Bus)");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 50.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Camión de carga");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 50.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        // Particulares - hasta 10 litros
        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Particular");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 10.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Oficial");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 10.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Diplomático");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 10.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        // Moto - hasta 5 litros
        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Moto");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Corriente");
        values.put(COLUMN_LITROS_MAX, 5.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        // ==================== ACPM (DIÉSEL) ====================
        // Servicio Público (Taxis, Buses) - hasta 30 litros
        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Taxi");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 30.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Servicio Público (Bus)");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 30.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Camión de carga");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 30.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        // Particular, Oficial, Diplomático - SIN SUBSIDIO
        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Particular");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 0.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Oficial");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 0.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Diplomático");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 0.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        // Moto con Diesel - sin subsidio (poco común)
        values.clear();
        values.put(COLUMN_TIPO_VEHICULO, "Moto");
        values.put(COLUMN_TIPO_COMBUSTIBLE, "Diesel");
        values.put(COLUMN_LITROS_MAX, 0.0);
        db.insert(TABLE_REGLAS_SUBSIDIO, null, values);

        // ==================== GASOLINA EXTRA ====================
        // Sin subsidio para todos los tipos
        String[] todosLosTipos = {"Particular", "Taxi", "Servicio Público (Bus)", "Camión de carga", "Oficial", "Diplomático", "Moto"};
        for (String tipo : todosLosTipos) {
            values.clear();
            values.put(COLUMN_TIPO_VEHICULO, tipo);
            values.put(COLUMN_TIPO_COMBUSTIBLE, "Extra");
            values.put(COLUMN_LITROS_MAX, 0.0);
            db.insert(TABLE_REGLAS_SUBSIDIO, null, values);
        }
    }
    private void insertarUsuariosDePrueba(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE_USUARIO, "Carlos Ramírez");
        values.put(COLUMN_USERNAME, "carlosr");
        values.put(COLUMN_CORREO, "cliente@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_DIRECCION, "Bogotá");
        values.put(COLUMN_GENERO, "Masculino");
        values.put(COLUMN_FECHA_NAC, System.currentTimeMillis() - (25L * 365 * 24 * 60 * 60 * 1000));
        values.put(COLUMN_ROL, "Cliente");
        db.insert(TABLE_USERS, null, values);

        values.clear();
        values.put(COLUMN_NOMBRE_USUARIO, "Laura Gómez");
        values.put(COLUMN_USERNAME, "laurag");
        values.put(COLUMN_CORREO, "empleado@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_DIRECCION, "Medellín");
        values.put(COLUMN_GENERO, "Femenino");
        values.put(COLUMN_FECHA_NAC, System.currentTimeMillis() - (30L * 365 * 24 * 60 * 60 * 1000));
        values.put(COLUMN_ROL, "Empleado de estación");
        db.insert(TABLE_USERS, null, values);

        values.clear();
        values.put(COLUMN_NOMBRE_USUARIO, "Andrés Torres");
        values.put(COLUMN_USERNAME, "andrest");
        values.put(COLUMN_CORREO, "tecnico@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_DIRECCION, "Cali");
        values.put(COLUMN_GENERO, "Masculino");
        values.put(COLUMN_FECHA_NAC, System.currentTimeMillis() - (28L * 365 * 24 * 60 * 60 * 1000));
        values.put(COLUMN_ROL, "Equipo técnico");
        db.insert(TABLE_USERS, null, values);

        values.clear();
        values.put(COLUMN_NOMBRE_USUARIO, "Sofía Martínez");
        values.put(COLUMN_USERNAME, "sofiam");
        values.put(COLUMN_CORREO, "regulador@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_DIRECCION, "Barranquilla");
        values.put(COLUMN_GENERO, "Femenino");
        values.put(COLUMN_FECHA_NAC, System.currentTimeMillis() - (35L * 365 * 24 * 60 * 60 * 1000));
        values.put(COLUMN_ROL, "Entidad reguladora");
        db.insert(TABLE_USERS, null, values);

        values.clear();
        values.put(COLUMN_NOMBRE_USUARIO, "Pedro Díaz");
        values.put(COLUMN_USERNAME, "pedrod");
        values.put(COLUMN_CORREO, "distribuidor@test.com");
        values.put(COLUMN_CONTRASENA, "123456");
        values.put(COLUMN_DIRECCION, "Bucaramanga");
        values.put(COLUMN_GENERO, "Masculino");
        values.put(COLUMN_FECHA_NAC, System.currentTimeMillis() - (32L * 365 * 24 * 60 * 60 * 1000));
        values.put(COLUMN_ROL, "Distribuidor");
        db.insert(TABLE_USERS, null, values);
    }

    public List<String[]> obtenerNormativas() {
        List<String[]> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NORMATIVAS, null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_VIGENCIA));
                lista.add(new String[]{titulo, descripcion, fecha});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
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
    private void insertarVentasIniciales(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // Venta 1
        values.put(COLUMN_TIPO, "Corriente");
        values.put(COLUMN_LITROS, 20.5);
        values.put(COLUMN_PRECIO, 12000);
        values.put(COLUMN_FECHA, "2025-03-25");
        db.insert(TABLE_VENTAS, null, values);

        values.clear();
        // Venta 2
        values.put(COLUMN_TIPO, "Extra");
        values.put(COLUMN_LITROS, 15.0);
        values.put(COLUMN_PRECIO, 14000);
        values.put(COLUMN_FECHA, "2025-03-24");
        db.insert(TABLE_VENTAS, null, values);

        values.clear();
        // Venta 3
        values.put(COLUMN_TIPO, "Diesel");
        values.put(COLUMN_LITROS, 30.2);
        values.put(COLUMN_PRECIO, 11000);
        values.put(COLUMN_FECHA, "2025-03-23");
        db.insert(TABLE_VENTAS, null, values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRECIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NORMATIVAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGLAS_SUBSIDIO);

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


    public boolean addUser(String nombre, String username, String correo,
                           String contrasena, String direccion,
                           String genero, long fechaNacimiento, String rol) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE_USUARIO, nombre);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_CORREO, correo);
        values.put(COLUMN_CONTRASENA, contrasena);
        values.put(COLUMN_DIRECCION, direccion);
        values.put(COLUMN_GENERO, genero);
        values.put(COLUMN_FECHA_NAC, fechaNacimiento);
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

    public boolean registrarInventario(String tipo, int cantidad){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIPO, tipo);
        values.put(COLUMN_CANTIDAD, cantidad);

        long resultado = db.insert(TABLE_INVENTARIO, null, values);

        db.close();

        return resultado != -1;
    }

    public boolean registrarVenta(String tipo, double litros, double precio, String fecha){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIPO, tipo);
        values.put(COLUMN_LITROS, litros);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_FECHA, fecha);

        long resultado = db.insert(TABLE_VENTAS, null, values);

        db.close();

        return resultado != -1;
    }

    public List<String> obtenerHistorialVentas(){

        List<String> lista = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VENTAS,null);

        if(cursor.moveToFirst()){

            do{

                String tipo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO));
                double litros = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LITROS));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRECIO));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));

                lista.add(tipo + " - " + litros + "L - $" + precio + " - " + fecha);

            }while(cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return lista;
    }

    public int obtenerCantidadDisponible(String tipo){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COLUMN_CANTIDAD + ") FROM " + TABLE_INVENTARIO +
                        " WHERE " + COLUMN_TIPO + " = ?",
                new String[]{tipo}
        );

        int cantidad = 0;

        if(cursor.moveToFirst()){
            cantidad = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return cantidad;
    }

    public boolean aplicaSubsidio(String tipoVehiculo, String tipoCombustible, double litros) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_LITROS_MAX + " FROM " + TABLE_REGLAS_SUBSIDIO
                + " WHERE " + COLUMN_TIPO_VEHICULO + " = ? AND " + COLUMN_TIPO_COMBUSTIBLE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{tipoVehiculo, tipoCombustible});

        boolean aplica = false;
        if (cursor.moveToFirst()) {
            double litrosMax = cursor.getDouble(0);
            aplica = litros <= litrosMax && litrosMax > 0;
        }

        cursor.close();
        db.close();
        return aplica;
    }

    private void insertarNormativasIniciales(SQLiteDatabase db) {
        ContentValues v = new ContentValues();

        v.put(COLUMN_TITULO, "Resolución 40066 de 2022");
        v.put(COLUMN_DESCRIPCION, "Regula los precios máximos de venta de combustibles líquidos en Colombia.");
        v.put(COLUMN_FECHA_VIGENCIA, "2022-03-01");
        db.insert(TABLE_NORMATIVAS, null, v);

        v.clear();
        v.put(COLUMN_TITULO, "Decreto 1073 de 2015");
        v.put(COLUMN_DESCRIPCION, "Decreto Único Reglamentario del Sector Administrativo de Minas y Energía.");
        v.put(COLUMN_FECHA_VIGENCIA, "2015-05-26");
        db.insert(TABLE_NORMATIVAS, null, v);

        v.clear();
        v.put(COLUMN_TITULO, "Resolución CREG 023 de 2020");
        v.put(COLUMN_DESCRIPCION, "Establece condiciones de calidad y control en la distribución de combustibles.");
        v.put(COLUMN_FECHA_VIGENCIA, "2020-06-15");
        db.insert(TABLE_NORMATIVAS, null, v);
    }


    public boolean registrarNotificacion(String estacionNombre, String inconsistencia, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ESTACION_NOMBRE, estacionNombre);
        values.put(COLUMN_INCONSISTENCIA, inconsistencia);
        values.put(COLUMN_ESTADO, "Enviada");
        values.put(COLUMN_FECHA, fecha);

        long resultado = db.insert(TABLE_NOTIFICACIONES, null, values);
        db.close();
        return resultado != -1;
    }

    public List<String[]> obtenerNotificaciones() {
        List<String[]> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTIFICACIONES + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String estacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTACION_NOMBRE));
                String inconsistencia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INCONSISTENCIA));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
                lista.add(new String[]{estacion, inconsistencia, estado, fecha});
            } while (cursor.moveToNext());
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

    private static final String CREAR_TABLA_INVENTARIO =
            "CREATE TABLE " + TABLE_INVENTARIO + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TIPO + " TEXT,"
                    + COLUMN_CANTIDAD + " INTEGER"
                    + ")";

    private static final String CREAR_TABLA_VENTAS =
            "CREATE TABLE " + TABLE_VENTAS + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TIPO + " TEXT,"
                    + COLUMN_LITROS + " REAL,"
                    + COLUMN_PRECIO + " REAL,"
                    + COLUMN_FECHA + " TEXT"
                    + ")";
}



