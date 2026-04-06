package co.edu.unipiloto.stationadviser.Model;

public class Estacion {
    private int id;
    private String nombre;
    private String nit;
    private String ubicacion;
    private String latitud;
    private String longitud;

    // Constructor completo
    public Estacion(int id, String nombre, String nit, String ubicacion, String latitud, String longitud) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Constructor sin ID (para nuevas estaciones)
    public Estacion(String nombre, String nit, String ubicacion, String latitud, String longitud) {
        this.nombre = nombre;
        this.nit = nit;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getLatitud() { return latitud; }
    public void setLatitud(String latitud) { this.latitud = latitud; }

    public String getLongitud() { return longitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }
}