package modelo;

public class Libro extends Material {

    private String autor;

    public Libro(String codigo, String titulo, int anio, String autor) {
        super(codigo, titulo, anio);
        setAutor(autor);
    }

    @Override
    public int diasPrestamoMaximo() {
        return 7;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {

        if(autor == null || autor.isEmpty()) {
            throw new IllegalArgumentException("Autor invalido");
        }

        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro: " + getTitulo() + " - " + autor;
    }

}