package co.edu.unipiloto.stationadviser;

public class Estacion {
    private int id;
    private String nombre;
    private String nit;
    private String ubicacion;

    public Estacion(int id, String nombre, String nit, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.ubicacion = ubicacion;
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
}