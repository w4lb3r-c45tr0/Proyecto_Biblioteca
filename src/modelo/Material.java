package modelo;

public abstract class Material {

    private String codigo;
    private String titulo;
    private int anio;
    private boolean disponible;

    public Material(String codigo, String titulo, int anio) {

        setCodigo(codigo);
        setTitulo(titulo);
        setAnio(anio);

        this.disponible = true;
    }

    public abstract int diasPrestamoMaximo();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {

        if(codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("Codigo invalido");
        }

        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {

        if(titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("Titulo invalido");
        }

        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {

        if(anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException("Anio invalido");
        }

        this.anio = anio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

}