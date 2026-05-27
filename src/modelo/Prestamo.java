package modelo;
 
import java.time.LocalDate;
 
public class Prestamo {
 
    private Material  material;
    private Usuario   usuario;
    private LocalDate fechaPrestamo;
    private boolean   activo;
 
    /** Constructor normal: se usa al hacer un préstamo nuevo */
    public Prestamo(Material material, Usuario usuario) {
        this.material      = material;
        this.usuario       = usuario;
        this.fechaPrestamo = LocalDate.now();
        this.activo        = true;
    }
 
    /**
     * Constructor para cargar desde archivo CSV.
     * Permite restaurar la fecha y el estado exactos que estaban guardados.
     */
    public Prestamo(Material material, Usuario usuario, LocalDate fecha, boolean activo) {
        this.material      = material;
        this.usuario       = usuario;
        this.fechaPrestamo = fecha;
        this.activo        = activo;
    }
 
    public Material getMaterial() {
        return material;
    }
 
    public Usuario getUsuario() {
        return usuario;
    }
 
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
 
    public boolean isActivo() {
        return activo;
    }
 
    public void cerrarPrestamo() {
        activo = false;
    }
 
    @Override
    public String toString() {
        return usuario.getNombre()
            + " -> "
            + material.getTitulo()
            + " Fecha: "
            + fechaPrestamo;
    }
}