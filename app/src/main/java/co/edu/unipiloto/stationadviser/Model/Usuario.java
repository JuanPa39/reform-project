package co.edu.unipiloto.stationadviser.Model;

public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String correo;
    private String contrasena;
    private String direccion;
    private String genero;
    private long fechaNacimiento;
    private String rol;

    public Usuario(int id, String nombre, String username, String correo,
                   String contrasena, String direccion, String genero,
                   long fechaNacimiento, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.correo = correo;
        this.contrasena = contrasena;
        this.direccion = direccion;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public long getFechaNacimiento() { return fechaNacimiento; }
    public String getRol() { return rol; }
}