package modelo;

public class Revista extends Material {

    private int numeroEdicion;

    public Revista(String codigo, String titulo, int anio, int numeroEdicion, int totalCopias) {
        super(codigo, titulo, anio, totalCopias);
        setNumeroEdicion(numeroEdicion);
    }

    @Override
    public int diasPrestamoMaximo() {
        return 3;
    }

    public int getNumeroEdicion() { return numeroEdicion; }
    public void setNumeroEdicion(int numeroEdicion) {
        if(numeroEdicion <= 0) throw new IllegalArgumentException("Edicion invalida");
        this.numeroEdicion = numeroEdicion;
    }

    @Override
    public String toString() {
        return String.format("Revista: %s Edicion %d (%d) | Stock Disp: %d/%d", 
                getTitulo(), numeroEdicion, getAnio(), getCopiasDisponibles(), getTotalCopias());
    }
}