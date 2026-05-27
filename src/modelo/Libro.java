package modelo;

public class Libro extends Material {

    private String autor;

    public Libro(String codigo, String titulo, int anio, String autor, int totalCopias) {
        super(codigo, titulo, anio, totalCopias);
        setAutor(autor);
    }

    @Override
    public int diasPrestamoMaximo() {
        return 7;
    }

    public String getAutor() { return autor; }
    public void setAutor(String autor) {
        if(autor == null || autor.isEmpty()) throw new IllegalArgumentException("Autor invalido");
        this.autor = autor;
    }

    @Override
    public String toString() {
        return String.format("Libro: %s — %s (%d) | Stock Disp: %d/%d", 
                getTitulo(), autor, getAnio(), getCopiasDisponibles(), getTotalCopias());
    }
}